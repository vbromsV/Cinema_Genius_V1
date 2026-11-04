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
    /** Genres that characterise the vibe; used as fallback when filters empty its own shelf. */
    val seedGenres: List<String>,
)

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
