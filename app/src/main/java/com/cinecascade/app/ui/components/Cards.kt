package com.cinecascade.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cinecascade.app.ui.theme.CineColors
import com.cinecascade.core.model.ItemSource
import com.cinecascade.core.model.MediaItem
import com.cinecascade.core.model.MediaType
import com.cinecascade.core.model.ThemeVibe
import java.util.Locale

/** Shared visual parameters for a card at the current animation frame. */
internal data class CardLook(
    val width: Dp,
    val height: Dp,
    val corner: Dp,
    val detail: Float,
    val badge: Float,
    val saturation: Float,
    /** 0..1 strength of the glowing focus frame. */
    val glow: Float,
    val glowColor: Color,
)

@Composable
internal fun MediaCard(item: MediaItem, look: CardLook, modifier: Modifier = Modifier, onClick: () -> Unit) {
    val shape = RoundedCornerShape(look.corner)
    Box(
        modifier
            .size(look.width, look.height)
            .clip(shape)
            .background(CineColors.PanelHigh)
            .border(
                width = (1f + 1.5f * look.glow).dp,
                color = if (look.glow > 0.01f) look.glowColor.copy(alpha = 0.25f + 0.75f * look.glow) else CineColors.Outline,
                shape = shape,
            )
            .clickable(onClick = onClick),
    ) {
        PosterImage(
            url = item.imageUrl,
            contentDescription = item.title,
            saturation = look.saturation,
            modifier = Modifier.fillMaxSize(),
            placeholder = { PosterPlaceholder(item, look) },
        )

        if (look.detail > 0.02f) {
            Column(
                Modifier
                    .align(Alignment.BottomStart)
                    .fillMaxWidth()
                    .background(Brush.verticalGradient(listOf(Color.Transparent, Color(0xE6000000))))
                    .padding(start = 8.dp, end = 8.dp, top = 18.dp, bottom = 8.dp)
                    .alpha(look.detail),
            ) {
                Text(
                    item.title,
                    color = Color.White,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    lineHeight = 14.sp,
                )
                Text(
                    "${item.year} · ${if (item.type == MediaType.SERIES) "Series" else "Movie"}",
                    color = Color.White.copy(alpha = 0.7f),
                    fontSize = 10.sp,
                )
            }
        }

        if (look.badge > 0.02f) {
            RatingTag(
                rating = item.rating,
                compact = look.width < 100.dp,
                modifier = Modifier.align(Alignment.TopEnd).padding(5.dp).alpha(look.badge),
            )
        }

        if (item.source == ItemSource.AI && look.badge > 0.02f) {
            Text(
                "✦ AI",
                color = Color.White,
                fontSize = 8.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(5.dp)
                    .alpha(look.badge)
                    .clip(RoundedCornerShape(50))
                    .background(Color(0xCC6C4DFF))
                    .padding(horizontal = 5.dp, vertical = 1.dp),
            )
        }
    }
}

@Composable
internal fun RatingTag(rating: Double, compact: Boolean, modifier: Modifier = Modifier) {
    Text(
        text = "★ " + String.format(Locale.US, "%.1f", rating),
        color = CineColors.ImdbYellow,
        fontSize = if (compact) 8.sp else 10.sp,
        fontWeight = FontWeight.Bold,
        modifier = modifier
            .clip(RoundedCornerShape(6.dp))
            .background(Color(0xCC000000))
            .padding(horizontal = if (compact) 4.dp else 6.dp, vertical = 2.dp),
    )
}

/** Gradient stand-in shown while a poster loads, or when none exists. */
@Composable
internal fun PosterPlaceholder(item: MediaItem, look: CardLook) {
    val top = item.colorProfile.primaryColor.desaturate(look.saturation)
    val bottom = item.colorProfile.secondaryColor.desaturate(look.saturation)
    Box(
        Modifier
            .fillMaxSize()
            .background(Brush.linearGradient(listOf(top, Color.Black.copy(alpha = 0.85f).compositeOver(bottom)))),
        contentAlignment = Alignment.Center,
    ) {
        if (look.width >= 60.dp) {
            Text(
                item.title,
                color = Color.White.copy(alpha = 0.92f),
                fontSize = if (look.width >= 110.dp) 15.sp else 10.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                maxLines = 4,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(8.dp),
            )
        } else {
            Text(item.title.take(1), color = Color.White.copy(alpha = 0.85f), fontSize = 18.sp, fontWeight = FontWeight.Black)
        }
    }
}

@Composable
internal fun ThemeCard(theme: ThemeVibe, look: CardLook, modifier: Modifier = Modifier, onClick: () -> Unit) {
    val shape = RoundedCornerShape(look.corner)
    val c1 = theme.colors.primaryColor.desaturate(look.saturation)
    val c2 = theme.colors.secondaryColor.desaturate(look.saturation)
    Box(
        modifier
            .size(look.width, look.height)
            .clip(shape)
            .background(Brush.linearGradient(listOf(c2, c1, Color.Black.copy(alpha = 0.6f).compositeOver(c1))))
            .border(
                width = (1f + 1.5f * look.glow).dp,
                color = if (look.glow > 0.01f) look.glowColor.copy(alpha = 0.25f + 0.75f * look.glow) else CineColors.Outline,
                shape = shape,
            )
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            theme.emoji,
            fontSize = (look.width.value * 0.34f).sp,
            modifier = Modifier.alpha(if (look.saturation < 0.5f) 0.6f else 1f),
        )
        if (look.detail > 0.02f) {
            Column(
                Modifier
                    .align(Alignment.BottomStart)
                    .fillMaxWidth()
                    .background(Brush.verticalGradient(listOf(Color.Transparent, Color(0xAA000000))))
                    .padding(10.dp)
                    .alpha(look.detail),
            ) {
                Text(theme.name, color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.Bold, maxLines = 2, lineHeight = 15.sp)
                Text(theme.moodTag.uppercase(), color = Color.White.copy(alpha = 0.75f), fontSize = 9.sp, letterSpacing = 1.sp, maxLines = 1)
            }
        }
    }
}
