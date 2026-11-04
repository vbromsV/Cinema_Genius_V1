package com.cinecascade.app.ui.components

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.lerp
import kotlin.math.floor

/**
 * Visual parameters of one cascade row at a given *position*:
 * -1 = Slot 1 (history), 0 = Slot 2 (active), 1 = Slot 3 (preview), ±2 = off-screen.
 * Rows animate their position continuously, so every value here is interpolated: a row moving
 * from Slot 3 to Slot 2 literally slides up while its cards grow from 85dp to 135dp.
 */
internal data class SlotSpec(
    val top: Dp,
    val height: Dp,
    val cardWidth: Dp,
    val cardHeight: Dp,
    val gap: Dp,
    val corner: Dp,
    val saturation: Float,
    val alpha: Float,
    /** Title/year overlay visibility (only on the big active cards). */
    val detail: Float,
    /** Rating tag visibility. */
    val badge: Float,
    /** How much the centre-focus scale effect applies (0.94x → 1.05x). */
    val focusWeight: Float,
)

internal class SlotGeometry(availableHeight: Dp) {

    private val specs: List<SlotSpec>

    init {
        val k = (availableHeight / DESIGN_HEIGHT).coerceIn(0.62f, 1f)
        val extra = (availableHeight - DESIGN_HEIGHT * k).coerceAtLeast(0.dp)
        val history = SlotSpec(
            top = extra * 0.18f, height = 95.dp * k, cardWidth = 44.dp * k, cardHeight = 64.dp * k,
            gap = 6.dp, corner = 8.dp, saturation = 0f, alpha = 0.72f, detail = 0f, badge = 0f, focusWeight = 0f,
        )
        val active = SlotSpec(
            top = 105.dp * k + extra * 0.42f, height = 246.dp * k, cardWidth = CARD_WIDTH * k, cardHeight = 200.dp * k,
            gap = 14.dp, corner = 16.dp, saturation = 1f, alpha = 1f, detail = 1f, badge = 1f, focusWeight = 1f,
        )
        val preview = SlotSpec(
            top = 361.dp * k + extra * 0.7f, height = 152.dp * k, cardWidth = 85.dp * k, cardHeight = 124.dp * k,
            gap = 10.dp, corner = 12.dp, saturation = 0.8f, alpha = 0.92f, detail = 0f, badge = 1f, focusWeight = 0f,
        )
        val aboveScreen = history.copy(top = history.top - history.height - 24.dp, alpha = 0f)
        val belowScreen = preview.copy(top = preview.top + preview.height + 40.dp, alpha = 0f)
        specs = listOf(aboveScreen, history, active, preview, belowScreen)
    }

    fun at(position: Float): SlotSpec {
        val p = position.coerceIn(-2f, 2f) + 2f // 0..4
        val i = floor(p).toInt().coerceAtMost(3)
        val t = p - i
        val a = specs[i]
        val b = specs[i + 1]
        return SlotSpec(
            top = lerp(a.top, b.top, t),
            height = lerp(a.height, b.height, t),
            cardWidth = lerp(a.cardWidth, b.cardWidth, t),
            cardHeight = lerp(a.cardHeight, b.cardHeight, t),
            gap = lerp(a.gap, b.gap, t),
            corner = lerp(a.corner, b.corner, t),
            saturation = lerpF(a.saturation, b.saturation, t),
            alpha = lerpF(a.alpha, b.alpha, t),
            detail = lerpF(a.detail, b.detail, t),
            badge = lerpF(a.badge, b.badge, t),
            focusWeight = lerpF(a.focusWeight, b.focusWeight, t),
        )
    }

    companion object {
        /** GDD §3/§7.2: Slot 2 card width; content padding is (width - CARD_WIDTH) / 2. */
        val CARD_WIDTH = 135.dp
        private val DESIGN_HEIGHT = 513.dp
    }
}
