package com.cinecascade.core

import com.cinecascade.core.data.Catalog
import com.cinecascade.core.model.DiscoveryFilters
import com.cinecascade.core.model.ItemSource
import com.cinecascade.core.model.MediaItem
import com.cinecascade.core.model.MediaType
import com.cinecascade.core.remote.GeminiClient
import com.cinecascade.core.remote.OmdbClient
import com.cinecascade.core.repository.MovieRepository
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

class RemoteClientsTest {
    private val http = OkHttpClient()
    private val interstellar = Catalog.items.first { it.id == MediaItem.slug("Interstellar", 2014) }

    private val omdbJson = """
        {"Title":"Interstellar","Year":"2014","Rated":"PG-13","Genre":"Adventure, Drama, Sci-Fi",
         "Director":"Christopher Nolan","Writer":"Jonathan Nolan, Christopher Nolan",
         "Actors":"Matthew McConaughey, Anne Hathaway, Jessica Chastain",
         "Plot":"When Earth becomes uninhabitable in the future, a farmer and ex-NASA pilot...",
         "Poster":"https://m.media-amazon.com/images/M/poster.jpg","imdbRating":"8.7",
         "imdbID":"tt0816692","Type":"movie","Response":"True"}
    """.trimIndent()

    @Test
    fun omdbLookupByTitleAndMerge() = runBlocking {
        MockWebServer().use { server ->
            server.enqueue(MockResponse().setBody(omdbJson))
            val client = OmdbClient("test-key", http, baseUrl = server.url("/").toString())
            val merged = client.enrich(interstellar)!!
            val req = server.takeRequest()
            assertEquals("test-key", req.requestUrl!!.queryParameter("apikey"))
            assertEquals("Interstellar", req.requestUrl!!.queryParameter("t"))
            assertEquals("2014", req.requestUrl!!.queryParameter("y"))
            assertEquals("movie", req.requestUrl!!.queryParameter("type"))
            assertEquals("tt0816692", merged.imdbId)
            assertEquals("https://m.media-amazon.com/images/M/poster.jpg", merged.imageUrl)
            assertEquals(listOf("Adventure", "Drama", "Sci-Fi"), merged.genres)
            assertTrue(merged.enriched)
            assertEquals(interstellar.id, merged.id, "id stays stable")
        }
    }

    @Test
    fun omdbHandlesNotFoundAndNa() = runBlocking {
        MockWebServer().use { server ->
            repeat(2) { server.enqueue(MockResponse().setBody("""{"Response":"False","Error":"Movie not found!"}""")) }
            val client = OmdbClient("k", http, baseUrl = server.url("/").toString())
            assertNull(client.enrich(interstellar))

            val series = Catalog.items.first { it.title == "The Expanse" }
            val merged = client.merge(series, com.cinecascade.core.remote.OmdbTitle(
                title = "The Expanse", year = "2015–2022", director = "N/A", poster = "N/A",
                imdbId = "tt3230854", imdbRating = "8.5", response = "True",
            ))
            assertEquals("Mark Fergus", merged.director)
            assertEquals(client.posterUrl("tt3230854"), merged.imageUrl)
        }
    }

    private fun geminiBody(inner: String): String {
        val escaped = inner.replace("\\", "\\\\").replace("\"", "\\\"").replace("\n", "\\n")
        return """{"candidates":[{"content":{"parts":[{"text":"$escaped"}],"role":"model"}}]}"""
    }

    @Test
    fun geminiRequestAndParsing() = runBlocking {
        val inner = """```json
            {"recommendations":[
              {"title":"Gravity","year":2013,"imdbId":"tt1454468","type":"movie","genres":["Sci-Fi"]},
              {"title":"Sunshine Odyssey","year":2019,"imdbId":"tt9999999","type":"series","genres":["Sci-Fi","Drama"],"director":"Someone","synopsis":"A ship."}
            ]}
            ```"""
        MockWebServer().use { server ->
            server.enqueue(MockResponse().setBody(geminiBody(inner)))
            val client = GeminiClient("g-key", http, baseUrl = server.url("/").toString())
            val result = client.similarTitles(interstellar, Catalog.themes.first(), DiscoveryFilters(), listOf("Dune"))
            val req = server.takeRequest()
            assertEquals("/models/gemini-3.5-flash:generateContent", req.path)
            assertEquals("g-key", req.getHeader("x-goog-api-key"))
            val sent = req.body.readUtf8()
            assertTrue("responseSchema" in sent && "Interstellar" in sent)
            assertEquals(2, result.size)
            assertEquals(MediaType.SERIES, result[1].mediaType)
        }
    }

    @Test
    fun repositoryRegistersAiSuggestions() = runBlocking {
        val inner = """{"recommendations":[
              {"title":"Gravity","year":2013,"imdbId":"tt1454468","type":"movie","genres":["Sci-Fi"]},
              {"title":"Hidden Gem Voyage","year":2016,"imdbId":"tt7654321","type":"movie","genres":["Sci-Fi","Drama"],"director":"Jane Doe","synopsis":"A quiet voyage."}
            ]}"""
        MockWebServer().use { server ->
            server.enqueue(MockResponse().setBody(geminiBody(inner)))
            val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
            val repo = MovieRepository(scope, gemini = GeminiClient("k", http, baseUrl = server.url("/").toString()))
            val before = repo.catalog.value.size
            assertEquals(2, repo.expandWithAi(interstellar, DiscoveryFilters()))
            assertEquals(2, repo.expandWithAi(interstellar, DiscoveryFilters()), "memoised result")
            assertEquals(before + 1, repo.catalog.value.size)
            val gem = repo.catalog.value.values.first { it.title == "Hidden Gem Voyage" }
            assertEquals(ItemSource.AI, gem.source)
            assertEquals("space", gem.themeId)
            val recs = repo.findRecommendations(interstellar, DiscoveryFilters()).map { it.title }
            assertTrue("Hidden Gem Voyage" in recs.take(4), recs.toString())
            assertTrue("AI pick" in repo.explain(interstellar, gem).reasons)
            assertEquals(1, server.requestCount, "second call must not hit Gemini again")
        }
    }
}
