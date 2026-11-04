package com.cinecascade.core.repository

import com.cinecascade.core.data.Catalog
import com.cinecascade.core.data.ColorMath
import com.cinecascade.core.engine.RecommendationEngine
import com.cinecascade.core.engine.ScoreBreakdown
import com.cinecascade.core.model.DiscoveryFilters
import com.cinecascade.core.model.ItemSource
import com.cinecascade.core.model.MediaItem
import com.cinecascade.core.model.ThemeVibe
import com.cinecascade.core.remote.AiSuggestion
import com.cinecascade.core.remote.GeminiClient
import com.cinecascade.core.remote.OmdbClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Semaphore
import kotlinx.coroutines.sync.withPermit
import java.util.concurrent.ConcurrentHashMap

/**
 * Single source of truth for titles. Holds the curated catalog, merges OMDb metadata in the
 * background and grows the catalog with Gemini suggestions.
 */
class MovieRepository(
    private val scope: CoroutineScope,
    private val omdb: OmdbClient? = null,
    private val gemini: GeminiClient? = null,
    private val enrichmentCache: EnrichmentCache? = null,
    val themes: List<ThemeVibe> = Catalog.themes,
    seed: List<MediaItem> = Catalog.items,
) {
    private val _catalog = MutableStateFlow(seed.associateBy { it.id })

    /** Every known title by id. Observed by the UI so posters/ratings appear as they arrive. */
    val catalog: StateFlow<Map<String, MediaItem>> = _catalog.asStateFlow()

    private val themesById = themes.associateBy { it.id }
    private val enrichAttempted = ConcurrentHashMap.newKeySet<String>()
    private val enrichLimiter = Semaphore(4)
    private val aiSuggestions = ConcurrentHashMap<String, Set<String>>()
    private val aiRequests = ConcurrentHashMap<String, Deferred<Int>>()

    val hasAi: Boolean get() = gemini != null

    init {
        enrichmentCache?.let { cache ->
            val cached = cache.load()
            if (cached.isNotEmpty() && omdb != null) {
                _catalog.update { current ->
                    current.mapValues { (id, item) ->
                        cached[id]?.let { omdb.merge(item, it) } ?: item
                    }
                }
                enrichAttempted += cached.keys
            }
        }
    }

    fun item(id: String): MediaItem? = _catalog.value[id]

    fun theme(id: String?): ThemeVibe? = id?.let(themesById::get)

    /** Genres present in the catalog, most common first (drives the Genres curtain). */
    fun availableGenres(): List<String> =
        _catalog.value.values.flatMap { it.genres }
            .groupingBy { it }.eachCount()
            .entries.sortedWith(compareByDescending<Map.Entry<String, Int>> { it.value }.thenBy { it.key })
            .map { it.key }

    /** Level 1 shelf for a theme. Falls back progressively so filters can never empty a row. */
    fun titlesForTheme(theme: ThemeVibe, filters: DiscoveryFilters, limit: Int = 20): List<MediaItem> {
        val all = _catalog.value.values
        val byRating = compareByDescending<MediaItem> { it.rating }.thenBy { it.title }
        val own = all.filter { it.themeId == theme.id && it.source == ItemSource.CURATED && filters.matches(it) }
        if (own.size >= 4) return own.sortedWith(byRating).take(limit)
        val related = all.filter { it.themeId != theme.id && filters.matches(it) && it.genres.any { g -> g in theme.seedGenres } }
            .sortedWith(byRating)
        val combined = (own.sortedWith(byRating) + related).distinctBy { it.id }
        if (combined.isNotEmpty()) return combined.take(limit)
        val anyFiltered = all.filter(filters::matches).sortedWith(byRating)
        if (anyFiltered.isNotEmpty()) return anyFiltered.take(limit)
        return all.filter { it.themeId == theme.id }.sortedWith(byRating).take(limit)
    }

    /** GDD §4.2 — rank the whole catalog against [source]. Never returns an empty list. */
    fun findRecommendations(
        source: MediaItem,
        filters: DiscoveryFilters,
        excludeIds: Set<String> = emptySet(),
        limit: Int = 12,
    ): List<MediaItem> {
        val all = _catalog.value.values
        val boosted = aiSuggestions[source.id].orEmpty()
        return RecommendationEngine.rank(source, all, filters, excludeIds, boosted, limit)
            .ifEmpty { RecommendationEngine.rank(source, all, filters, setOf(source.id), boosted, limit) }
            .ifEmpty { RecommendationEngine.rank(source, all, DiscoveryFilters(), excludeIds, boosted, limit) }
    }

    fun explain(source: MediaItem, candidate: MediaItem): ScoreBreakdown =
        RecommendationEngine.score(source, candidate, candidate.id in aiSuggestions[source.id].orEmpty())

    /** Fetch OMDb data for the given titles (once each, max 4 in parallel). */
    fun enrich(items: Collection<MediaItem>) {
        val client = omdb ?: return
        items.forEach { item ->
            if (item.enriched || !enrichAttempted.add(item.id)) return@forEach
            scope.launch {
                enrichLimiter.withPermit {
                    val hit = client.lookup(item) ?: return@withPermit
                    _catalog.update { current ->
                        val latest = current[item.id] ?: return@update current
                        current + (item.id to client.merge(latest, hit))
                    }
                    enrichmentCache?.put(item.id, hit)
                }
            }
        }
    }

    /**
     * Ask Gemini for titles similar to [source]; new ones are added to the catalog and boosted in
     * [findRecommendations]. Requests are memoised per source and survive caller cancellation.
     * Returns the number of suggestions that matched or were added.
     */
    suspend fun expandWithAi(source: MediaItem, filters: DiscoveryFilters): Int {
        val client = gemini ?: return 0
        // LAZY: the coroutine starts on await(), i.e. only after the map entry exists.
        val deferred = aiRequests.computeIfAbsent(source.id) {
            scope.async(start = CoroutineStart.LAZY) {
                val known = findRecommendations(source, filters, limit = 20).map { it.title } + source.title
                val suggestions = client.similarTitles(source, theme(source.themeId), filters, known)
                register(source, suggestions)
            }
        }
        return runCatching { deferred.await() }.getOrElse {
            aiRequests.remove(source.id)
            0
        }
    }

    private fun register(source: MediaItem, suggestions: List<AiSuggestion>): Int {
        if (suggestions.isEmpty()) {
            aiRequests.remove(source.id) // allow a retry later
            return 0
        }
        val theme = theme(source.themeId)
        val matchedIds = mutableSetOf<String>()
        val newItems = mutableListOf<MediaItem>()
        _catalog.update { current ->
            val byImdb = current.values.filter { it.imdbId != null }.associateBy { it.imdbId }
            val additions = mutableMapOf<String, MediaItem>()
            suggestions.forEach { s ->
                val slug = MediaItem.slug(s.title, s.year)
                val imdbId = s.imdbId?.trim()?.takeIf { Regex("tt\\d{6,10}").matches(it) }
                val existing = imdbId?.let(byImdb::get) ?: current[slug] ?: additions[slug]
                if (existing != null) {
                    matchedIds += existing.id
                } else {
                    val item = MediaItem(
                        id = slug,
                        title = s.title.trim(),
                        type = s.mediaType,
                        year = s.year,
                        rating = source.rating, // placeholder until OMDb answers
                        themeId = source.themeId,
                        genres = s.genres.ifEmpty { source.genres },
                        director = s.director,
                        cast = s.cast,
                        synopsis = s.synopsis.ifBlank { s.reason },
                        imdbId = imdbId,
                        colorProfile = ColorMath.profileFor(theme?.colors ?: source.colorProfile, s.title),
                        source = ItemSource.AI,
                    )
                    additions[slug] = item
                    matchedIds += slug
                }
            }
            newItems += additions.values
            current + additions
        }
        aiSuggestions[source.id] = aiSuggestions[source.id].orEmpty() + matchedIds
        enrich(newItems)
        return matchedIds.size
    }

}
