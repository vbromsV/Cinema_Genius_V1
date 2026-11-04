package com.cinecascade.core.remote

import com.cinecascade.core.model.MediaItem
import com.cinecascade.core.model.MediaType
import com.squareup.moshi.Json
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.OkHttpClient
import okhttp3.Request
import kotlin.math.abs

/** Raw OMDb title payload (only the fields CineCascade uses). */
data class OmdbTitle(
    @Json(name = "Title") val title: String? = null,
    @Json(name = "Year") val year: String? = null,
    @Json(name = "Genre") val genre: String? = null,
    @Json(name = "Director") val director: String? = null,
    @Json(name = "Writer") val writer: String? = null,
    @Json(name = "Actors") val actors: String? = null,
    @Json(name = "Plot") val plot: String? = null,
    @Json(name = "Poster") val poster: String? = null,
    @Json(name = "imdbRating") val imdbRating: String? = null,
    @Json(name = "imdbID") val imdbId: String? = null,
    @Json(name = "Type") val type: String? = null,
    @Json(name = "Response") val response: String? = null,
    @Json(name = "Error") val error: String? = null,
) {
    val isSuccess: Boolean get() = response.equals("True", ignoreCase = true)
    val startYear: Int? get() = year?.let { Regex("\\d{4}").find(it)?.value?.toIntOrNull() }
}

/**
 * OMDb metadata pipeline (GDD §4.4).
 *
 * Data requests:   https://www.omdbapi.com/?apikey=KEY&i=tt...   or  &t=Title&y=Year&type=movie
 * Poster requests: https://img.omdbapi.com/?apikey=KEY&i=tt...
 *
 * HTTPS is used because Android blocks clear-text HTTP by default.
 */
class OmdbClient(
    private val apiKey: String,
    private val http: OkHttpClient,
    private val baseUrl: String = "https://www.omdbapi.com/",
    private val posterBaseUrl: String = "https://img.omdbapi.com/",
) {
    private val adapter = Moshi.Builder().add(KotlinJsonAdapterFactory()).build().adapter(OmdbTitle::class.java)

    suspend fun byImdbId(imdbId: String): OmdbTitle? = request(mapOf("i" to imdbId, "plot" to "short"))

    suspend fun byTitle(title: String, year: Int?, type: MediaType?): OmdbTitle? {
        val params = buildMap {
            put("t", title)
            year?.let { put("y", it.toString()) }
            type?.let { put("type", if (it == MediaType.SERIES) "series" else "movie") }
            put("plot", "short")
        }
        return request(params)
    }

    /** Looks an item up: by IMDb id when known, otherwise by title + year + type. */
    suspend fun lookup(item: MediaItem): OmdbTitle? =
        item.imdbId?.let { byImdbId(it) }
            ?: byTitle(item.title, item.year, item.type)?.takeIf { isPlausible(item, it) }
            // Release years sometimes differ by one between sources: retry without year.
            ?: byTitle(item.title, null, item.type)?.takeIf { isPlausible(item, it) }

    /** [lookup] + [merge]. */
    suspend fun enrich(item: MediaItem): MediaItem? = lookup(item)?.let { merge(item, it) }

    fun posterUrl(imdbId: String): String = "$posterBaseUrl?apikey=$apiKey&i=$imdbId&h=600"

    fun merge(item: MediaItem, t: OmdbTitle): MediaItem {
        val imdbId = t.imdbId.clean() ?: item.imdbId
        val poster = t.poster.clean()?.takeIf { it.startsWith("http") }
            ?: imdbId?.let { posterUrl(it) }
        // Series have no director on OMDb: keep the curated creator, else fall back to the first writer.
        val director = t.director.clean()
            ?: item.director.takeIf { it.isNotBlank() }
            ?: t.writer.clean()?.substringBefore(',')?.substringBefore('(')?.trim()
            ?: ""
        return item.copy(
            title = t.title.clean() ?: item.title,
            rating = t.imdbRating.clean()?.toDoubleOrNull() ?: item.rating,
            genres = t.genre.clean()?.split(',')?.map { it.trim() }?.filter { it.isNotEmpty() } ?: item.genres,
            director = director,
            cast = t.actors.clean()?.split(',')?.map { it.trim() } ?: item.cast,
            synopsis = t.plot.clean() ?: item.synopsis,
            imageUrl = poster ?: item.imageUrl,
            imdbId = imdbId,
            enriched = true,
        )
    }

    private fun isPlausible(item: MediaItem, t: OmdbTitle): Boolean {
        if (!t.isSuccess) return false
        val y = t.startYear ?: return true
        return abs(y - item.year) <= 1
    }

    private suspend fun request(params: Map<String, String>): OmdbTitle? = withContext(Dispatchers.IO) {
        val url = baseUrl.toHttpUrl().newBuilder()
            .addQueryParameter("apikey", apiKey)
            .apply { params.forEach { (k, v) -> addQueryParameter(k, v) } }
            .build()
        runCatching {
            http.newCall(Request.Builder().url(url).get().build()).execute().use { response ->
                if (!response.isSuccessful) return@use null
                response.body?.string()?.let(adapter::fromJson)?.takeIf { it.isSuccess }
            }
        }.getOrNull()
    }

    private fun String?.clean(): String? = this?.trim()?.takeIf { it.isNotEmpty() && !it.equals("N/A", true) }
}
