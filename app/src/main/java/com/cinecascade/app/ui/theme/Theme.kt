package com.cinecascade.app.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

object CineColors {
    val Background = Color(0xFF0B0B0F)
    val Panel = Color(0xFF131317)
    val PanelHigh = Color(0xFF1C1C23)
    val Outline = Color(0x26FFFFFF)
    val TextPrimary = Color(0xFFF2F2F5)
    val TextSecondary = Color(0xFF9A9AA6)
    val TextMuted = Color(0xFF64646E)
    val ImdbYellow = Color(0xFFF5C518)
}

private val scheme = darkColorScheme(
    primary = CineColors.ImdbYellow,
    onPrimary = Color.Black,
    secondary = Color(0xFF8A7BFF),
    background = CineColors.Background,
    onBackground = CineColors.TextPrimary,
    surface = CineColors.Panel,
    onSurface = CineColors.TextPrimary,
    surfaceVariant = CineColors.PanelHigh,
    onSurfaceVariant = CineColors.TextSecondary,
    outline = CineColors.Outline,
    secondaryContainer = Color(0xFF2E2A4F),
    onSecondaryContainer = CineColors.TextPrimary,
)

@Composable
fun CineCascadeTheme(content: @Composable () -> Unit) {
    MaterialTheme(colorScheme = scheme, content = content)
}
