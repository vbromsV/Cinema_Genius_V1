package com.cinecascade.app.viewmodel

import com.cinecascade.core.cascade.CarouselNode
import com.cinecascade.core.cascade.CascadeTree
import com.cinecascade.core.model.DiscoveryFilters
import com.cinecascade.core.model.MediaType

/** GDD §4.1 DiscoveryUiState. */
data class DiscoveryUiState(
    val tree: CascadeTree,
    val filters: DiscoveryFilters = DiscoveryFilters(),
    val availableGenres: List<String> = emptyList(),
    val isFormatCurtainOpen: Boolean = false,
    val isGenreCurtainOpen: Boolean = false,
    /** Why the focused title was recommended (relative to the title that generated its row). */
    val focusedReasons: List<String> = emptyList(),
    val isAiEnabled: Boolean = false,
    val isAiLoading: Boolean = false,
) {
    val levels: List<CarouselNode> get() = tree.levels
    val activeLevelIndex: Int get() = tree.activeLevelIndex
    val selectedMediaTypes: Set<MediaType> get() = filters.mediaTypes
    val selectedGenres: Set<String> get() = filters.genres
}

/** Everything the UI can ask for. Implemented by [MovieDiscoveryViewModel]. */
interface DiscoveryActions {
    fun onFocus(levelIndex: Int, index: Int)
    fun onAdvance()
    fun onAdvanceTo(previewIndex: Int)
    fun onBack()
    fun onBranch(levelIndex: Int, index: Int)
    fun onToggleMediaType(type: MediaType)
    fun onToggleGenre(genre: String)
    fun onResetFilters()
    fun onToggleFormatCurtain()
    fun onToggleGenreCurtain()
    fun onCloseCurtains()
}
