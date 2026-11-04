package com.cinecascade.core

import com.cinecascade.core.cascade.CascadeNavigator
import com.cinecascade.core.cascade.RepositoryNodeFactory
import com.cinecascade.core.model.DiscoveryFilters
import com.cinecascade.core.model.MediaType
import com.cinecascade.core.repository.MovieRepository
import kotlinx.coroutines.test.TestScope
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals
import kotlin.test.assertTrue

class CascadeNavigatorTest {
    private var filters = DiscoveryFilters()
    private val repo = MovieRepository(TestScope())
    private val nav = CascadeNavigator(RepositoryNodeFactory(repo) { filters })

    @Test
    fun initialTreeHasThemeRowAndPreview() {
        val tree = nav.initial()
        assertEquals(2, tree.levels.size)
        assertEquals(0, tree.activeLevelIndex)
        assertTrue(tree.active.isThemeRow)
        assertEquals("space", tree.preview!!.parentTheme!!.id)
        assertTrue(tree.preview!!.items.all { it.themeId == "space" })
    }

    @Test
    fun focusUpdatesPreviewLive() {
        val tree = nav.focus(nav.initial(), 0, 1)
        assertEquals("nordic", tree.active.selectedTheme!!.id)
        assertEquals("nordic", tree.preview!!.parentTheme!!.id)
        assertEquals(2, tree.levels.size)
    }

    @Test
    fun advanceBackAndForwardHistory() {
        var tree = nav.advance(nav.initial()) // theme -> titles
        assertEquals(1, tree.activeLevelIndex)
        assertEquals(3, tree.levels.size)
        val first = tree.active.selectedItem!!
        assertEquals(first.title, tree.preview!!.parentItem!!.title)

        tree = nav.advance(tree)
        assertEquals(2, tree.activeLevelIndex)
        assertTrue(tree.active.items.none { it.id == first.id }, "chain items are excluded")
        val deep = tree.levels.size

        tree = nav.back(tree)
        assertEquals(1, tree.activeLevelIndex)
        assertEquals(deep, tree.levels.size, "forward history kept after going back")

        // choosing another card truncates deeper levels and grows a new branch
        tree = nav.focus(tree, 1, 2)
        assertEquals(3, tree.levels.size)
        assertEquals(tree.active.selectedItem!!.title, tree.preview!!.parentItem!!.title)
    }

    @Test
    fun advanceToPicksPreviewCard() {
        val tree = nav.advanceTo(nav.initial(), 3)
        assertEquals(1, tree.activeLevelIndex)
        assertEquals(3, tree.active.selectedIndex)
        assertEquals(tree.active.selectedItem!!.title, tree.preview!!.parentItem!!.title)
    }

    @Test
    fun branchFromHistory() {
        var tree = nav.advance(nav.advance(nav.initial()))
        tree = nav.branch(tree, 0, 4)
        assertEquals(0, tree.activeLevelIndex)
        assertEquals(2, tree.levels.size)
        assertEquals(tree.active.selectedTheme!!.id, tree.preview!!.parentTheme!!.id)
    }

    @Test
    fun rebuildAppliesFiltersAndKeepsDepth() {
        var tree = nav.advance(nav.advance(nav.initial()))
        val oldId = tree.levels[1].id
        filters = DiscoveryFilters(mediaTypes = setOf(MediaType.SERIES))
        tree = nav.rebuild(tree)
        assertEquals(2, tree.activeLevelIndex)
        assertNotEquals(oldId, tree.levels[1].id)
        tree.levels.drop(1).forEach { node -> assertTrue(node.items.all { it.type == MediaType.SERIES }) }
        assertTrue(tree.levels.drop(1).all { it.items.isNotEmpty() }, "zero dead ends")
    }

    @Test
    fun neverDeadEndsDeepIntoTheTree() {
        var tree = nav.initial()
        repeat(40) {
            assertTrue(tree.canAdvance, "dead end at depth ${tree.activeLevelIndex}")
            tree = nav.advance(tree)
        }
        assertEquals(40, tree.activeLevelIndex)
    }
}
