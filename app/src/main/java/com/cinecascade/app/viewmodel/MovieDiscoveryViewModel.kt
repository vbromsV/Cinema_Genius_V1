package com.cinecascade.app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cinecascade.core.cascade.CascadeNavigator
import com.cinecascade.core.cascade.CascadeTree
import com.cinecascade.core.cascade.RepositoryNodeFactory
import com.cinecascade.core.model.DiscoveryFilters
import com.cinecascade.core.model.MediaItem
import com.cinecascade.core.model.MediaType
import com.cinecascade.core.repository.MovieRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MovieDiscoveryViewModel(private val repository: MovieRepository) : ViewModel(), DiscoveryActions {

    private var filters = DiscoveryFilters()
    private val navigator = CascadeNavigator(RepositoryNodeFactory(repository) { filters })

    private val _uiState = MutableStateFlow(
        DiscoveryUiState(
            tree = navigator.initial(),
            availableGenres = repository.availableGenres(),
            isAiEnabled = repository.hasAi,
        ),
    )
    val uiState: StateFlow<DiscoveryUiState> = _uiState.asStateFlow()

    /** Live catalog: posters and OMDb ratings flow into the cards as they arrive. */
    val catalog: StateFlow<Map<String, MediaItem>> = repository.catalog

    private var aiJob: Job? = null

    init {
        afterTreeChange(_uiState.value.tree)
    }

    // ---- Cascade navigation --------------------------------------------------------------

    override fun onFocus(levelIndex: Int, index: Int) = updateTree { navigator.focus(it, levelIndex, index) }
    override fun onAdvance() = updateTree(navigator::advance)
    override fun onAdvanceTo(previewIndex: Int) = updateTree { navigator.advanceTo(it, previewIndex) }
    override fun onBack() = updateTree(navigator::back)
    override fun onBranch(levelIndex: Int, index: Int) = updateTree { navigator.branch(it, levelIndex, index) }

    // ---- Curtain filters -----------------------------------------------------------------

    override fun onToggleMediaType(type: MediaType) =
        applyFilters(filters.copy(mediaTypes = filters.mediaTypes.toggle(type)))

    override fun onToggleGenre(genre: String) =
        applyFilters(filters.copy(genres = filters.genres.toggle(genre)))

    override fun onResetFilters() = applyFilters(DiscoveryFilters())

    override fun onToggleFormatCurtain() = _uiState.update {
        it.copy(isFormatCurtainOpen = !it.isFormatCurtainOpen, isGenreCurtainOpen = false)
    }

    override fun onToggleGenreCurtain() = _uiState.update {
        it.copy(isGenreCurtainOpen = !it.isGenreCurtainOpen, isFormatCurtainOpen = false)
    }

    override fun onCloseCurtains() = _uiState.update {
        it.copy(isFormatCurtainOpen = false, isGenreCurtainOpen = false)
    }

    // ---- Internals -----------------------------------------------------------------------

    private fun updateTree(transform: (CascadeTree) -> CascadeTree) {
        val old = _uiState.value.tree
        val new = transform(old)
        if (new == old) return
        _uiState.update { it.copy(tree = new, focusedReasons = reasonsFor(new)) }
        afterTreeChange(new)
    }

    private fun applyFilters(newFilters: DiscoveryFilters) {
        if (newFilters == filters) return
        filters = newFilters
        val tree = navigator.rebuild(_uiState.value.tree)
        _uiState.update { it.copy(filters = newFilters, tree = tree, focusedReasons = reasonsFor(tree)) }
        afterTreeChange(tree)
    }

    private fun afterTreeChange(tree: CascadeTree) {
        tree.active.selectedItem?.let { repository.markSeen(it.id) }
        // OMDb: active row first, then the live preview.
        repository.enrich(tree.active.items + tree.preview?.items.orEmpty())
        scheduleAiExpansion(tree)
    }

    /** GDD §4.3 — debounce 450 ms while the user is scrolling, then ask Gemini for more titles. */
    private fun scheduleAiExpansion(tree: CascadeTree) {
        aiJob?.cancel()
        if (!repository.hasAi) return
        val focused = tree.active.selectedItem ?: return
        aiJob = viewModelScope.launch {
            delay(AI_DEBOUNCE_MS)
            _uiState.update { it.copy(isAiLoading = true) }
            try {
                val matched = repository.expandWithAi(repository.item(focused.id) ?: focused, filters)
                val current = _uiState.value.tree
                if (matched > 0 && current.active.selectedItem?.id == focused.id) {
                    val refreshed = navigator.refreshPreview(current)
                    if (refreshed != current) {
                        _uiState.update { it.copy(tree = refreshed, availableGenres = repository.availableGenres()) }
                        repository.enrich(refreshed.preview?.items.orEmpty())
                    }
                }
            } finally {
                _uiState.update { it.copy(isAiLoading = false) }
            }
        }
    }

    private fun reasonsFor(tree: CascadeTree): List<String> {
        val parent = tree.active.parentItem ?: return emptyList()
        val focused = tree.active.selectedItem ?: return emptyList()
        val latestParent = repository.item(parent.id) ?: parent
        val latestFocused = repository.item(focused.id) ?: focused
        return repository.explain(latestParent, latestFocused).reasons
    }

    private fun <T> Set<T>.toggle(value: T): Set<T> = if (value in this) this - value else this + value

    private companion object {
        const val AI_DEBOUNCE_MS = 450L
    }
}
