package com.cinecascade.core.data

import com.cinecascade.core.model.ColorProfile
import com.cinecascade.core.model.ThemeRule
import com.cinecascade.core.model.ThemeVibe

/** Generated vibes for genres the user filters on that no built-in vibe covers. */
object GenreVibes {
    private val emoji = mapOf(
        "Action" to "💥", "Adventure" to "🧭", "Animation" to "🎨", "Biography" to "📜",
        "Comedy" to "😂", "Crime" to "🔫", "Drama" to "🎭", "Family" to "🍿", "Fantasy" to "🐉",
        "Film-Noir" to "🕶️", "History" to "🏛️", "Horror" to "👻", "Music" to "🎵",
        "Mystery" to "🔍", "Romance" to "💘", "Sci-Fi" to "🛸", "Sport" to "🏆",
        "Thriller" to "🔪", "War" to "🎖️", "Western" to "🤠", "Documentary" to "🎥",
    )

    private val palette = listOf(
        ColorProfile(0xFF1E3A6B, 0xFF7C4DFF), ColorProfile(0xFF6B1E3A, 0xFFFF4081),
        ColorProfile(0xFF1E6B4A, 0xFF69F0AE), ColorProfile(0xFF6B4A1E, 0xFFFFAB40),
        ColorProfile(0xFF3A1E6B, 0xFFB388FF), ColorProfile(0xFF1E5A6B, 0xFF18FFFF),
    )

    fun forGenre(genre: String): ThemeVibe = ThemeVibe(
        id = "genre:" + genre.lowercase(),
        name = genre,
        moodTag = "Your filter",
        emoji = emoji[genre] ?: "🎬",
        description = "The best $genre titles in the catalog, matched to your filters.",
        colors = palette[Math.floorMod(genre.hashCode(), palette.size)],
        seedGenres = listOf(genre),
        rule = ThemeRule(genres = listOf(genre)),
    )
}
