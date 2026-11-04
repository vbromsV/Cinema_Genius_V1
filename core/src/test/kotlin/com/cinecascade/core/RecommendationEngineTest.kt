package com.cinecascade.core

import com.cinecascade.core.data.Catalog
import com.cinecascade.core.engine.RecommendationEngine
import com.cinecascade.core.model.DiscoveryFilters
import com.cinecascade.core.model.MediaItem
import com.cinecascade.core.model.MediaType
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class RecommendationEngineTest {
    private fun item(title: String, year: Int) = Catalog.items.first { it.id == MediaItem.slug(title, year) }

    @Test
    fun catalogIdsAreUnique() {
        val ids = Catalog.items.map { it.id }
        assertEquals(ids.size, ids.toSet().size, "duplicate ids: ${ids.groupBy { it }.filter { it.value.size > 1 }.keys}")
        assertTrue(Catalog.items.all { item -> Catalog.themes.any { it.id == item.themeId } })
    }

    @Test
    fun directorCastThemeAndGenreAreWeighted() {
        val interstellar = item("Interstellar", 2014)
        val inception = item("Inception", 2010)
        val breakdown = RecommendationEngine.score(interstellar, inception)
        // director 15 + caine 10 (top-5 cast) + adventure genre 3 + year within 5 → 1 + rating 0.1 → 3 + format 2
        assertTrue("Same director" in breakdown.reasons)
        assertTrue(breakdown.total >= 15 + 3 + 2)
    }

    @Test
    fun interstellarLeadsToSpaceAndNolan() {
        val interstellar = item("Interstellar", 2014)
        val recs = RecommendationEngine.rank(interstellar, Catalog.items, DiscoveryFilters(), emptySet())
        val titles = recs.map { it.title }
        assertEquals(12, recs.size)
        assertTrue(interstellar !in recs)
        assertTrue(titles.take(6).any { it in setOf("The Martian", "Contact", "Inception", "Gravity", "Arrival") }, titles.toString())
    }

    @Test
    fun filtersAreRespected() {
        val filters = DiscoveryFilters(mediaTypes = setOf(MediaType.SERIES), genres = setOf("Mystery"))
        val recs = RecommendationEngine.rank(item("Arrival", 2016), Catalog.items, filters, emptySet())
        assertTrue(recs.isNotEmpty())
        assertTrue(recs.all { it.type == MediaType.SERIES && "Mystery" in it.genres })
    }

    @Test
    fun excludedAndAiBoost() {
        val source = item("The Matrix", 1999)
        val target = item("Paddington 2", 2017)
        val boosted = RecommendationEngine.rank(source, Catalog.items, DiscoveryFilters(), setOf("blade-runner-1982"), setOf(target.id))
        assertTrue(boosted.none { it.id == "blade-runner-1982" })
        val plain = RecommendationEngine.score(source, target).total
        assertEquals(plain + RecommendationEngine.AI_BOOST, RecommendationEngine.score(source, target, aiSuggested = true).total)
    }

    @Test
    fun titleTokensLinkSequels() {
        val fellowship = item("The Lord of the Rings: The Fellowship of the Ring", 2001)
        val recs = RecommendationEngine.rank(fellowship, Catalog.items, DiscoveryFilters(), emptySet(), limit = 3).map { it.title }
        assertTrue(recs.take(2).all { it.startsWith("The Lord of the Rings") }, recs.toString())
        assertTrue("The Hobbit: An Unexpected Journey" in recs, recs.toString())
    }
}

class DiscoveryVarietyTest {
    @Test
    fun catalogIsLarge() {
        assertTrue(Catalog.items.size >= 250, "only ${Catalog.items.size} titles")
        assertTrue(Catalog.themes.size >= 12)
    }

    @Test
    fun rowsAreLongAndIncludeWildcards() {
        val src = Catalog.items.first { it.title == "Interstellar" }
        val strict = RecommendationEngine.rank(src, Catalog.items, DiscoveryFilters(), emptySet(), limit = 6)
        val row = RecommendationEngine.rank(src, Catalog.items, DiscoveryFilters(), emptySet(), limit = 20)
        assertEquals(20, row.size)
        assertEquals(strict, row.take(6), "closest matches stay first")
        assertEquals(row, RecommendationEngine.rank(src, Catalog.items, DiscoveryFilters(), emptySet(), limit = 20), "stable per source")
    }

    @Test
    fun seenTitlesDropDown() {
        val src = Catalog.items.first { it.title == "Interstellar" }
        val first = RecommendationEngine.rank(src, Catalog.items, DiscoveryFilters(), emptySet(), limit = 20).first()
        val after = RecommendationEngine.rank(src, Catalog.items, DiscoveryFilters(), emptySet(), limit = 20, seenIds = setOf(first.id))
        assertTrue(after.indexOf(first) != 0)
    }

    @Test
    fun longWalkVisitsManyDifferentTitles() {
        val repo = com.cinecascade.core.repository.MovieRepository(kotlinx.coroutines.test.TestScope())
        var current = Catalog.items.first { it.title == "Interstellar" }
        val visited = linkedSetOf(current.id)
        repeat(60) {
            repo.markSeen(current.id)
            current = repo.findRecommendations(current, DiscoveryFilters(), visited).first()
            visited += current.id
        }
        assertEquals(61, visited.size)
        assertTrue(visited.map { id -> Catalog.items.first { it.id == id }.themeId }.toSet().size >= 4, "walk should cross vibes")
    }
}
