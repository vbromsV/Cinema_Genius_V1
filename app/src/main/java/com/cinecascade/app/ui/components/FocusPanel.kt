package com.cinecascade.app.ui.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cinecascade.app.ui.theme.CineColors
import com.cinecascade.core.cascade.CarouselNode
import com.cinecascade.core.model.MediaItem
import com.cinecascade.core.model.MediaType
import com.cinecascade.core.model.ThemeVibe
import java.util.Locale

private val TITLE_LINE = 20.sp
private val META_LINE = 17.sp
private val PEOPLE_LINE = 15.sp
private val REASONS_LINE = 14.sp
private val SYNOPSIS_LINE = 14.sp
private val PANEL_PADDING = 12.dp
private val LINE_GAP = 3.dp
private val THUMB_HEIGHT = 96.dp

/**
 * Fixed height of the bottom panel, sized for its tallest content (five text rows, synopsis on two
 * lines) at the phone's text size. It never changes with the focused title, so the carousels
 * above keep exactly the same space while you scroll.
 */
@Composable
internal fun focusPanelHeight(): Dp = with(LocalDensity.current) {
    val text = (TITLE_LINE.value + META_LINE.value + PEOPLE_LINE.value + REASONS_LINE.value + SYNOPSIS_LINE.value * 2).sp.toDp()
    val content = maxOf(text + LINE_GAP * 4, THUMB_HEIGHT)
    content + PANEL_PADDING * 2 + 4.dp
}

/** What the bottom panel shows: the focused theme or title of Slot 2. */
private sealed interface FocusTarget {
    val key: String

    data class Theme(val theme: ThemeVibe, val titleCount: Int) : FocusTarget {
        override val key get() = "theme:" + theme.id
    }

    data class Title(val item: MediaItem, val reasons: List<String>) : FocusTarget {
        override val key get() = "item:" + item.id
    }
}

/** Bottom module (GDD §3): minimalist IMDb status panel / theme detail card. */
@Composable
internal fun FocusPanel(
    active: CarouselNode,
    preview: CarouselNode?,
    catalog: Map<String, MediaItem>,
    reasons: List<String>,
    accent: Color,
    modifier: Modifier = Modifier,
) {
    val target: FocusTarget? = active.selectedTheme?.let { FocusTarget.Theme(it, preview?.size ?: 0) }
        ?: active.selectedItem?.let { FocusTarget.Title(catalog[it.id] ?: it, reasons) }

    Box(
        modifier
            .clip(RoundedCornerShape(18.dp))
            .background(CineColors.Panel)
            .border(1.dp, CineColors.Outline, RoundedCornerShape(18.dp)),
    ) {
        AnimatedContent(
            targetState = target,
            contentKey = { it?.key },
            transitionSpec = {
                (fadeIn(tween(220)) + slideInVertically(tween(260)) { it / 5 }) togetherWith fadeOut(tween(120))
            },
            label = "focus-panel",
        ) { t ->
            when (t) {
                is FocusTarget.Theme -> ThemeDetail(t.theme, t.titleCount)
                is FocusTarget.Title -> ImdbDetail(catalog[t.item.id] ?: t.item, t.reasons, accent)
                null -> Spacer(Modifier.fillMaxSize())
            }
        }
    }
}

@Composable
private fun ImdbDetail(item: MediaItem, reasons: List<String>, accent: Color) {
    Row(Modifier.fillMaxSize().padding(PANEL_PADDING), verticalAlignment = Alignment.CenterVertically) {
        val look = CardLook(64.dp, 96.dp, 8.dp, detail = 0f, badge = 0f, saturation = 1f, glow = 0f, glowColor = accent)
        Box(Modifier.size(64.dp, 96.dp).clip(RoundedCornerShape(8.dp))) {
            PosterImage(item.imageUrl, item.title, 1f, Modifier.fillMaxSize()) { PosterPlaceholder(item, look) }
        }
        Spacer(Modifier.width(12.dp))
        Column(Modifier.fillMaxHeight(), verticalArrangement = Arrangement.spacedBy(LINE_GAP, Alignment.CenterVertically)) {
            Text(
                item.title,
                color = CineColors.TextPrimary,
                fontSize = 16.sp,
                lineHeight = TITLE_LINE,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            Row(Modifier.height(with(LocalDensity.current) { META_LINE.toDp() }), verticalAlignment = Alignment.CenterVertically) {
                Text(
                    "IMDb",
                    color = Color.Black,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Black,
                    modifier = Modifier
                        .clip(RoundedCornerShape(4.dp))
                        .background(CineColors.ImdbYellow)
                        .padding(horizontal = 5.dp, vertical = 1.dp),
                )
                Spacer(Modifier.width(6.dp))
                Text("★ " + String.format(Locale.US, "%.1f", item.rating), color = CineColors.ImdbYellow, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                Text("/10", color = CineColors.TextMuted, fontSize = 11.sp)
                Text(
                    "  ·  ${item.year}  ·  ${if (item.type == MediaType.SERIES) "Series" else "Movie"}",
                    color = CineColors.TextSecondary,
                    fontSize = 11.sp,
                    maxLines = 1,
                )
            }
            val people = buildList {
                if (item.director.isNotBlank()) add((if (item.type == MediaType.SERIES) "Created by " else "Dir. ") + item.director)
                if (item.cast.isNotEmpty()) add(item.cast.take(2).joinToString())
            }.joinToString("  ·  ")
            if (people.isNotEmpty()) {
                Text(people, color = CineColors.TextSecondary, fontSize = 11.sp, lineHeight = PEOPLE_LINE, maxLines = 1, overflow = TextOverflow.Ellipsis)
            }
            if (reasons.isNotEmpty()) {
                Text(
                    "MATCH  ·  " + reasons.take(3).joinToString("  ·  "),
                    color = accent,
                    fontSize = 10.sp,
                    lineHeight = REASONS_LINE,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
            Text(
                item.synopsis,
                color = CineColors.TextPrimary.copy(alpha = 0.78f),
                fontSize = 11.sp,
                lineHeight = SYNOPSIS_LINE,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }
}

@Composable
private fun ThemeDetail(theme: ThemeVibe, titleCount: Int) {
    Row(Modifier.fillMaxSize().padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
        Box(
            Modifier
                .size(72.dp)
                .clip(RoundedCornerShape(14.dp))
                .background(Brush.linearGradient(listOf(theme.colors.secondaryColor, theme.colors.primaryColor))),
            contentAlignment = Alignment.Center,
        ) {
            Text(theme.emoji, fontSize = 34.sp)
        }
        Spacer(Modifier.width(14.dp))
        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(theme.name, color = CineColors.TextPrimary, fontSize = 16.sp, fontWeight = FontWeight.Bold, maxLines = 1)
                Spacer(Modifier.width(8.dp))
                Text(
                    theme.moodTag.uppercase(),
                    color = Color.White,
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp,
                    modifier = Modifier
                        .clip(RoundedCornerShape(50))
                        .background(theme.colors.primaryColor.copy(alpha = 0.7f))
                        .padding(horizontal = 7.dp, vertical = 2.dp),
                )
            }
            Text(theme.description, color = CineColors.TextSecondary, fontSize = 12.sp, maxLines = 2, overflow = TextOverflow.Ellipsis)
            Text("$titleCount titles  ·  swipe up or tap to dive in  ↑", color = CineColors.TextMuted, fontSize = 11.sp)
        }
    }
}
