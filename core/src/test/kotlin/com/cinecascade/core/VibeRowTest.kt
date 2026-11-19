package com.cinecascade.core

import com.cinecascade.core.cascade.CascadeNavigator
import com.cinecascade.core.cascade.RepositoryNodeFactory
import com.cinecascade.core.data.Catalog
import com.cinecascade.core.model.DiscoveryFilters
import com.cinecascade.core.model.MediaType
import com.cinecascade.core.repository.MovieRepository
import kotlinx.coroutines.test.TestScope
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotEquals
import kotlin.test.assertTrue

class VibeRowTest {
    private val repo = MovieRepository(TestScope())

    @Test
    fun manyVibesWithoutFilters() {
        val vibes = repo.themesFor(DiscoveryFilters())
        assertTrue(vibes.size >= 24, "only ${vibes.size}: ${vibes.map { it.name }}")
        vibes.forEach { assertTrue(repo.titlesForTheme(it, DiscoveryFilters()).size >= 3, it.name) }
    }

    @Test
    fun genreFilterDropsUnrelatedVibes() {
        val filters = DiscoveryFilters(genres = setOf("Comedy", "Romance"))
        val ids = repo.themesFor(filters).map { it.id }
        assertFalse("horror" in ids)
        assertFalse("space" in ids)
        assertFalse("postapoc" in ids)
        assertTrue("cozy" in ids && "romance" in ids && "laugh" in ids, ids.toString())
        assertTrue(ids.indexOf("cozy") < 3, "best-matching vibes come first: $ids")
        // every offered vibe really has titles under the filter
        repo.themesFor(filters).forEach { vibe ->
            assertTrue(repo.titlesForTheme(vibe, filters).all(filters::matches), vibe.name)
        }
    }

    @Test
    fun uncoveredGenreGetsGeneratedVibe() {
        val filters = DiscoveryFilters(genres = setOf("Film-Noir"))
        val vibes = repo.themesFor(filters)
        assertTrue(vibes.any { it.id == "genre:film-noir" }, vibes.map { it.id }.toString())
        assertEquals("The Maltese Falcon", repo.titlesForTheme(vibes.first { it.id == "genre:film-noir" }, filters).first().title)
    }

    @Test
    fun formatFilterHidesSeriesOnlyVibe() {
        val ids = repo.themesFor(DiscoveryFilters(mediaTypes = setOf(MediaType.MOVIE))).map { it.id }
        assertFalse("binge" in ids)
        assertTrue("binge" in repo.themesFor(DiscoveryFilters(mediaTypes = setOf(MediaType.SERIES))).map { it.id })
    }

    @Test
    fun shuffleIsStablePerSeed() {
        val a = repo.themesFor(DiscoveryFilters(), shuffleSeed = 42)
        assertEquals(a, repo.themesFor(DiscoveryFilters(), shuffleSeed = 42))
        assertNotEquals(a.map { it.id }, Catalog.themes.map { it.id })
    }

    @Test
    fun rebuildUpdatesVibeRowAndKeepsSelectionWhenPossible() {
        var filters = DiscoveryFilters()
        val nav = CascadeNavigator(RepositoryNodeFactory(repo) { filters })
        var tree = nav.initial()
        val cozyIndex = tree.active.themes.indexOfFirst { it.id == "cozy" }
        tree = nav.focus(tree, 0, cozyIndex)

        filters = DiscoveryFilters(genres = setOf("Comedy"))
        tree = nav.rebuild(tree)
        assertFalse(tree.levels[0].themes.any { it.id == "horror" })
        assertEquals("cozy", tree.active.selectedTheme!!.id, "selection survives")
        assertTrue(tree.preview!!.items.all { "Comedy" in it.genres })

        filters = DiscoveryFilters(genres = setOf("Horror"))
        tree = nav.rebuild(tree)
        assertFalse(tree.levels[0].themes.any { it.id == "cozy" })
        assertEquals(tree.active.selectedTheme!!.id, tree.preview!!.parentTheme!!.id)
    }
}
