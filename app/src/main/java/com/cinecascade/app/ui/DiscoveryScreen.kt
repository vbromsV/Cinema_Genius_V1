package com.cinecascade.app.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.unit.dp
import com.cinecascade.app.ui.components.CascadeStack
import com.cinecascade.app.ui.components.Curtain
import com.cinecascade.app.ui.components.FocusPanel
import com.cinecascade.app.ui.components.FormatCurtainContent
import com.cinecascade.app.ui.components.GenreCurtainContent
import com.cinecascade.app.ui.components.TopBar
import com.cinecascade.app.ui.theme.CineColors
import com.cinecascade.app.viewmodel.DiscoveryActions
import com.cinecascade.app.viewmodel.DiscoveryUiState
import com.cinecascade.core.model.ColorProfile
import com.cinecascade.core.model.MediaItem

/**
 * The five modules of GDD §3: top bar, Slot 1–3 cascade and the bottom IMDb panel, plus the
 * curtain menus layered on top.
 */
@Composable
fun DiscoveryScreen(state: DiscoveryUiState, catalog: Map<String, MediaItem>, actions: DiscoveryActions) {
    val tree = state.tree
    val active = tree.active
    val focusColors: ColorProfile = active.selectedTheme?.colors
        ?: active.selectedItem?.let { (catalog[it.id] ?: it).colorProfile }
        ?: ColorProfile(0xFF1B1B22, 0xFF8A7BFF)

    // Dynamic background: tint subtly toward the focused title's colour.
    val tint by animateColorAsState(Color(focusColors.primary.toInt()), tween(700), label = "bg-tint")
    val accentTarget = lerp(Color(focusColors.secondary.toInt()), Color.White, 0.35f)
    val accent by animateColorAsState(accentTarget, tween(400), label = "accent")

    Box(
        Modifier
            .fillMaxSize()
            .background(CineColors.Background)
            .background(Brush.verticalGradient(listOf(lerp(CineColors.Background, tint, 0.32f), CineColors.Background, lerp(CineColors.Background, tint, 0.12f)))),
    ) {
        Column(Modifier.fillMaxSize().safeDrawingPadding()) {
            TopBar(
                filters = state.filters,
                depth = state.activeLevelIndex,
                isFormatOpen = state.isFormatCurtainOpen,
                isGenreOpen = state.isGenreCurtainOpen,
                isAiLoading = state.isAiLoading,
                onFormatClick = actions::onToggleFormatCurtain,
                onGenreClick = actions::onToggleGenreCurtain,
                onReset = actions::onResetFilters,
            )
            CascadeStack(
                tree = tree,
                catalog = catalog,
                accent = accent,
                actions = actions,
                modifier = Modifier.weight(1f).fillMaxWidth(),
            )
            FocusPanel(
                active = active,
                preview = tree.preview,
                catalog = catalog,
                reasons = state.focusedReasons,
                accent = accent,
                modifier = Modifier
                    .padding(start = 14.dp, end = 14.dp, top = 4.dp, bottom = 12.dp)
                    .fillMaxWidth()
                    .height(132.dp),
            )
        }

        // Curtain layer
        val curtainOpen = state.isFormatCurtainOpen || state.isGenreCurtainOpen
        AnimatedVisibility(visible = curtainOpen, enter = fadeIn(), exit = fadeOut()) {
            Box(
                Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.55f))
                    .clickable(interactionSource = remember { MutableInteractionSource() }, indication = null, onClick = actions::onCloseCurtains),
            )
        }
        Column(Modifier.fillMaxWidth().safeDrawingPadding().padding(top = 56.dp)) {
            Curtain(visible = state.isFormatCurtainOpen) {
                FormatCurtainContent(state.selectedMediaTypes, actions::onToggleMediaType, actions::onCloseCurtains)
            }
            Curtain(visible = state.isGenreCurtainOpen) {
                GenreCurtainContent(state.availableGenres, state.selectedGenres, actions::onToggleGenre, actions::onCloseCurtains)
            }
        }
    }
}
