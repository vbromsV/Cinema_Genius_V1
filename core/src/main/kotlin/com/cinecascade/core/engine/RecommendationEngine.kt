package com.cinecascade.core.engine

import com.cinecascade.core.model.DiscoveryFilters
import com.cinecascade.core.model.MediaItem
import kotlin.math.abs
import kotlin.random.Random

/** Result of scoring one candidate against a source title. */
data class ScoreBreakdown(val total: Int, val reasons: List<String>)

/**
 * The 8-dimensional local similarity algorithm (GDD §4.2), plus an AI boost for titles Gemini
 * explicitly suggested for the source.
 */
object RecommendationEngine {
    const val DIRECTOR = 15
    const val SHARED_CAST = 10
    const val SAME_THEME = 8
    const val PRIMARY_GENRE = 6
    const val EXTRA_GENRE = 3
    const val KEYWORD = 5
    const val SAME_YEAR = 4
    const val WITHIN_2_YEARS = 2
    const val WITHIN_5_YEARS = 1
    const val RATING_CLOSE = 3
    const val RATING_NEAR = 1
    const val SAME_FORMAT = 2
    const val AI_BOOST = 12
    const val SEEN_PENALTY = 8
    private const val EXPLORE_MIN_ROW = 8

    private const val CAST_DEPTH = 5

    private val stopWords = setOf(
        "the", "and", "with", "from", "part", "into", "of", "a", "an", "in", "on", "at", "to",
        "for", "saga", "mystery", "life", "secret", "story",
    )

    fun score(source: MediaItem, candidate: MediaItem, aiSuggested: Boolean = false): ScoreBreakdown {
        var total = 0
        val reasons = mutableListOf<String>()

        // 1. Director / creator
        val sourceDirectors = people(source.director)
        if (sourceDirectors.isNotEmpty() && sourceDirectors.any { it in people(candidate.director) }) {
            total += DIRECTOR
            reasons += "Same director"
        }

        // 2. Shared lead cast
        val sourceCast = source.cast.take(CAST_DEPTH).map(::norm).toSet()
        val sharedCast = candidate.cast.take(CAST_DEPTH).filter { norm(it) in sourceCast }
        if (sharedCast.isNotEmpty()) {
            total += SHARED_CAST * sharedCast.size
            reasons += if (sharedCast.size == 1) sharedCast.first() else "${sharedCast.size} shared actors"
        }

        // 3. Curated theme / vibe
        if (source.themeId != null && source.themeId == candidate.themeId) {
            total += SAME_THEME
            reasons += "Same vibe"
        }

        // 4. Genres
        val sourcePrimary = source.primaryGenre
        val candidateGenres = candidate.genres.toSet()
        if (sourcePrimary != null && sourcePrimary in candidateGenres) total += PRIMARY_GENRE
        val extraGenres = source.genres.drop(1).count { it in candidateGenres }
        total += extraGenres * EXTRA_GENRE
        if (sourcePrimary in candidateGenres || extraGenres > 0) {
            val shared = source.genres.filter { it in candidateGenres }
            reasons += shared.take(2).joinToString(" + ")
        }

        // 5. Title similarity & keywords
        val sharedWords = tokens(source).intersect(tokens(candidate))
        if (sharedWords.isNotEmpty()) {
            total += KEYWORD * sharedWords.size
            reasons += "“" + sharedWords.first() + "”"
        }

        // 6. Temporal proximity
        val yearGap = abs(source.year - candidate.year)
        total += when {
            yearGap == 0 -> SAME_YEAR
            yearGap <= 2 -> WITHIN_2_YEARS
            yearGap <= 5 -> WITHIN_5_YEARS
            else -> 0
        }

        // 7. Quality proximity
        val ratingGap = abs(source.rating - candidate.rating)
        total += when {
            ratingGap <= 0.5 -> RATING_CLOSE
            ratingGap <= 1.0 -> RATING_NEAR
            else -> 0
        }

        // 8. Format match
        if (source.type == candidate.type) total += SAME_FORMAT

        if (aiSuggested) {
            total += AI_BOOST
            reasons.add(0, "AI pick")
        }

        return ScoreBreakdown(total, reasons)
    }

    /**
     * Ranks [candidates] for [source]. Always returns something when any candidate passes the
     * filters (zero dead ends): zero-score titles are kept, just ordered last.
     */
    fun rank(
        source: MediaItem,
        candidates: Collection<MediaItem>,
        filters: DiscoveryFilters,
        excludeIds: Set<String>,
        aiSuggested: Set<String> = emptySet(),
        limit: Int = 12,
        /** Titles the user has already seen: pushed down so the journey keeps finding new ground. */
        seenIds: Set<String> = emptySet(),
    ): List<MediaItem> {
        val sorted = candidates.asSequence()
            .filter { it.id != source.id && it.id !in excludeIds && filters.matches(it) }
            .map { it to score(source, it, it.id in aiSuggested).total - if (it.id in seenIds) SEEN_PENALTY else 0 }
            .sortedWith(
                compareByDescending<Pair<MediaItem, Int>> { it.second }
                    .thenByDescending { it.first.rating }
                    .thenBy { it.first.title },
            )
            .map { it.first }
            .toList()
        if (limit < EXPLORE_MIN_ROW) return sorted.take(limit)
        // Two thirds are the closest matches; the rest are wildcards from the next-best pool, picked
        // with a seed derived from the source so the same title always shows the same row.
        val topCount = limit - limit / 3
        val wildcards = sorted.drop(topCount).take(limit * 4)
            .shuffled(Random(source.id.hashCode().toLong()))
            .take(limit - topCount)
        return sorted.take(topCount) + wildcards
    }

    private fun norm(s: String) = s.trim().lowercase()

    private fun people(s: String): Set<String> =
        s.split(',', '&').map(::norm).filter { it.isNotEmpty() && it != "n/a" }.toSet()

    internal fun tokens(item: MediaItem): Set<String> {
        val titleWords = item.title.lowercase()
            .split(Regex("[^\\p{L}\\p{N}]+"))
            .filter { it.length >= 4 && it !in stopWords }
        return (titleWords + item.keywords.map(::norm)).toSet()
    }
}
