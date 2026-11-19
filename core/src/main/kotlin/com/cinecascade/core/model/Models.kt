package com.cinecascade.core.model

/** Movie or series. The label is what the "Movies/Shows" curtain shows. */
enum class MediaType(val label: String) {
    MOVIE("Movies"),
    SERIES("Shows"),
}

/** Two ARGB colors (0xAARRGGBB) used for gradients, placeholders and the dynamic background. */
data class ColorProfile(val primary: Long, val secondary: Long)

/** Level 0 "Theme Card": a curated vibe that seeds the cascade. */
data class ThemeVibe(
    val id: String,
    val name: String,
    val moodTag: String,
    val emoji: String,
    val description: String,
    val colors: ColorProfile,
    /**
     * The vibe's identity genres. With a genre filter active the vibe is only offered when one of
     * these is selected (vibes with none, like "Top Rated", are genre-neutral and always eligible).
     * Curated vibes also borrow titles with these genres when filters thin out their own shelf.
     */
    val seedGenres: List<String>,
    /** Rule-based vibes collect titles from the whole catalog instead of a hand-picked shelf. */
    val rule: ThemeRule? = null,
) {
    fun contains(item: MediaItem): Boolean = rule?.matches(item) ?: (item.themeId == id)
}

/**
 * Membership rule for a vibe without a curated shelf. A title must match one of [genres] or
 * [keywords] (when either is given) and every format/year/rating bound.
 */
data class ThemeRule(
    val genres: List<String> = emptyList(),
    val keywords: List<String> = emptyList(),
    val mediaType: MediaType? = null,
    val minYear: Int? = null,
    val maxYear: Int? = null,
    val minRating: Double? = null,
) {
    fun matches(item: MediaItem): Boolean {
        if (mediaType != null && item.type != mediaType) return false
        if (minYear != null && item.year < minYear) return false
        if (maxYear != null && item.year > maxYear) return false
        if (minRating != null && item.rating < minRating) return false
        if (genres.isEmpty() && keywords.isEmpty()) return true
        if (item.genres.any { it in genres }) return true
        if (keywords.isEmpty()) return false
        val words = (item.keywords + item.title.lowercase().split(Regex("[^\\p{L}\\p{N}]+"))).map { it.lowercase() }
        return words.any { it in keywords }
    }
}

enum class ItemSource { CURATED, AI }

data class MediaItem(
    /** Stable id: a slug of title + year. Never changes, even after OMDb enrichment. */
    val id: String,
    val title: String,
    val type: MediaType,
    val year: Int,
    /** IMDb rating 0.0–10.0. */
    val rating: Double,
    val themeId: String?,
    val genres: List<String>,
    /** Director, or creator for series. */
    val director: String,
    val cast: List<String>,
    val synopsis: String,
    val imageUrl: String? = null,
    val imdbId: String? = null,
    val keywords: List<String> = emptyList(),
    val colorProfile: ColorProfile,
    val source: ItemSource = ItemSource.CURATED,
    /** True once OMDb data has been merged in. */
    val enriched: Boolean = false,
) {
    val primaryGenre: String? get() = genres.firstOrNull()

    companion object {
        fun slug(title: String, year: Int): String =
            title.lowercase()
                .replace(Regex("[^a-z0-9]+"), "-")
                .trim('-') + "-" + year
    }
}

/** Global filter state from the curtain menus. Empty set = no restriction. */
data class DiscoveryFilters(
    val mediaTypes: Set<MediaType> = emptySet(),
    val genres: Set<String> = emptySet(),
) {
    val isActive: Boolean get() = mediaTypes.isNotEmpty() || genres.isNotEmpty()

    fun matches(item: MediaItem): Boolean =
        (mediaTypes.isEmpty() || item.type in mediaTypes) &&
            (genres.isEmpty() || item.genres.any { it in genres })
}
