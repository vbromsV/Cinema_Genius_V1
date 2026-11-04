package com.cinecascade.app.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cinecascade.app.ui.theme.CineColors
import com.cinecascade.core.model.DiscoveryFilters
import com.cinecascade.core.model.MediaType

@Composable
internal fun TopBar(
    filters: DiscoveryFilters,
    depth: Int,
    isFormatOpen: Boolean,
    isGenreOpen: Boolean,
    isAiLoading: Boolean,
    onFormatClick: () -> Unit,
    onGenreClick: () -> Unit,
    onReset: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier.fillMaxWidth().height(56.dp).padding(horizontal = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        val formatLabel = when (filters.mediaTypes) {
            setOf(MediaType.MOVIE) -> "Movies"
            setOf(MediaType.SERIES) -> "Shows"
            else -> "Movies/Shows"
        }
        CurtainPill(formatLabel, isOpen = isFormatOpen, isActive = filters.mediaTypes.isNotEmpty(), onClick = onFormatClick)
        val genreLabel = if (filters.genres.isEmpty()) "Genres" else "Genres · ${filters.genres.size}"
        CurtainPill(genreLabel, isOpen = isGenreOpen, isActive = filters.genres.isNotEmpty(), onClick = onGenreClick)
        Spacer(Modifier.weight(1f))
        if (isAiLoading) {
            Text("✦", color = Color(0xFF8A7BFF), fontSize = 14.sp)
        }
        Text(
            if (depth == 0) "VIBES" else "DEPTH $depth",
            color = CineColors.TextMuted,
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.2.sp,
        )
        IconButton(onClick = onReset, enabled = filters.isActive, modifier = Modifier.size(36.dp)) {
            Icon(
                Icons.Filled.Refresh,
                contentDescription = "Reset filters",
                tint = if (filters.isActive) CineColors.TextPrimary else CineColors.TextMuted.copy(alpha = 0.5f),
            )
        }
    }
}

@Composable
private fun CurtainPill(label: String, isOpen: Boolean, isActive: Boolean, onClick: () -> Unit) {
    val rotation by animateFloatAsState(if (isOpen) 180f else 0f, spring(stiffness = Spring.StiffnessMediumLow), label = "pill-arrow")
    Row(
        Modifier
            .clip(RoundedCornerShape(50))
            .background(if (isActive) CineColors.ImdbYellow.copy(alpha = 0.16f) else CineColors.Panel)
            .border(1.dp, if (isActive || isOpen) CineColors.ImdbYellow.copy(alpha = 0.7f) else CineColors.Outline, RoundedCornerShape(50))
            .clickable(onClick = onClick)
            .padding(start = 14.dp, end = 8.dp, top = 7.dp, bottom = 7.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(label, color = CineColors.TextPrimary, fontSize = 13.sp, fontWeight = FontWeight.Medium)
        Icon(
            Icons.Filled.ArrowDropDown,
            contentDescription = null,
            tint = CineColors.TextSecondary,
            modifier = Modifier.size(20.dp).rotate(rotation),
        )
    }
}

/** A curtain that unrolls from under the top bar (GDD §2.1 "Globala Filterridåer"). */
@Composable
internal fun Curtain(visible: Boolean, modifier: Modifier = Modifier, content: @Composable ColumnScope.() -> Unit) {
    AnimatedVisibility(
        visible = visible,
        modifier = modifier,
        enter = expandVertically(spring(dampingRatio = 0.82f, stiffness = Spring.StiffnessMediumLow), expandFrom = Alignment.Top) +
            fadeIn(),
        exit = shrinkVertically(spring(stiffness = Spring.StiffnessMedium), shrinkTowards = Alignment.Top) + fadeOut(),
    ) {
        Column(
            Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp))
                .background(CineColors.PanelHigh)
                .padding(horizontal = 18.dp, vertical = 14.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            content = content,
        )
    }
}

@Composable
internal fun FormatCurtainContent(selected: Set<MediaType>, onToggle: (MediaType) -> Unit, onDone: () -> Unit) {
    CurtainTitle("Show me")
    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
        MediaType.entries.forEach { type ->
            CurtainChip(type.label, selected = type in selected) { onToggle(type) }
        }
    }
    CurtainFooter(hint = if (selected.isEmpty()) "Nothing selected = everything" else "Multi-select", onDone = onDone)
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
internal fun GenreCurtainContent(genres: List<String>, selected: Set<String>, onToggle: (String) -> Unit, onDone: () -> Unit) {
    CurtainTitle("Genres")
    FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(2.dp)) {
        genres.forEach { genre ->
            CurtainChip(genre, selected = genre in selected) { onToggle(genre) }
        }
    }
    CurtainFooter(hint = if (selected.isEmpty()) "Combine several, e.g. Sci-Fi + Mystery" else "${selected.size} selected", onDone = onDone)
}

@Composable
private fun CurtainTitle(text: String) {
    Text(text.uppercase(), color = CineColors.TextMuted, fontSize = 10.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.4.sp)
}

@Composable
private fun CurtainChip(label: String, selected: Boolean, onClick: () -> Unit) {
    FilterChip(
        selected = selected,
        onClick = onClick,
        label = { Text(label) },
        leadingIcon = if (selected) {
            { Icon(Icons.Filled.Check, contentDescription = null, modifier = Modifier.size(16.dp)) }
        } else {
            null
        },
        colors = FilterChipDefaults.filterChipColors(
            selectedContainerColor = CineColors.ImdbYellow.copy(alpha = 0.2f),
            selectedLabelColor = CineColors.TextPrimary,
            selectedLeadingIconColor = CineColors.ImdbYellow,
            labelColor = CineColors.TextSecondary,
        ),
        shape = RoundedCornerShape(50),
    )
}

@Composable
private fun CurtainFooter(hint: String, onDone: () -> Unit) {
    Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        Text(hint, color = CineColors.TextMuted, fontSize = 11.sp, modifier = Modifier.weight(1f))
        TextButton(onClick = onDone) { Text("Done", color = CineColors.ImdbYellow, fontWeight = FontWeight.Bold) }
    }
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
        Spacer(Modifier.width(36.dp).height(4.dp).clip(CircleShape).background(CineColors.Outline).alpha(0.8f))
    }
}
