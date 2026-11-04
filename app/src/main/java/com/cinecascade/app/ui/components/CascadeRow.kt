package com.cinecascade.app.ui.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.snapping.SnapPosition
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cinecascade.app.ui.theme.CineColors
import com.cinecascade.core.cascade.CarouselNode
import com.cinecascade.core.model.MediaItem
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch
import kotlin.math.abs

/** Callbacks a row needs; [levelIndex] is passed back so the view model knows which row spoke. */
internal class RowCallbacks(
    val onFocus: (levelIndex: Int, index: Int) -> Unit,
    val onAdvance: () -> Unit,
    val onAdvanceTo: (index: Int) -> Unit,
    val onBranch: (levelIndex: Int, index: Int) -> Unit,
)

/**
 * One level of the cascade. [role] is the row's logical slot (−1 history, 0 active, 1 preview);
 * [spec] carries the *animated* geometry for this frame.
 */
@Composable
internal fun CascadeRow(
    levelIndex: Int,
    node: CarouselNode,
    role: Int,
    spec: SlotSpec,
    viewportWidth: Dp,
    catalog: Map<String, MediaItem>,
    accent: Color,
    callbacks: RowCallbacks,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier
            .fillMaxWidth()
            .offset { IntOffset(0, spec.top.roundToPx()) }
            .height(spec.height)
            .graphicsLayer { alpha = spec.alpha },
    ) {
        RowHeader(node, role, levelIndex, callbacks)

        // A preview row whose parent changed gets a brand-new node id: crossfade its contents.
        val nodeCache = remember { LinkedHashMap<Long, CarouselNode>() }
        nodeCache[node.id] = node
        while (nodeCache.size > 3) nodeCache.remove(nodeCache.keys.first())

        AnimatedContent(
            targetState = node.id,
            transitionSpec = { fadeIn(tween(220, delayMillis = 60)) togetherWith fadeOut(tween(140)) },
            label = "row-content",
        ) { id ->
            CarouselLazyRow(
                levelIndex = levelIndex,
                node = if (id == node.id) node else nodeCache[id] ?: node,
                role = role,
                spec = spec,
                viewportWidth = viewportWidth,
                catalog = catalog,
                accent = accent,
                callbacks = callbacks,
            )
        }
    }
}

@Composable
private fun RowHeader(node: CarouselNode, role: Int, levelIndex: Int, callbacks: RowCallbacks) {
    val text = when {
        role < 0 -> "▲  " + (node.selectedLabel ?: "")
        node.isThemeRow -> "CHOOSE YOUR VIBE"
        role == 0 -> "BECAUSE YOU PICKED  ·  " + (node.parentLabel ?: "").uppercase()
        else -> "UP NEXT  ·  LIKE " + (node.parentLabel ?: "").uppercase()
    }
    val clickModifier = if (role < 0) {
        Modifier.clickable(
            interactionSource = remember { MutableInteractionSource() },
            indication = null,
        ) { callbacks.onBranch(levelIndex, node.selectedIndex) }
    } else {
        Modifier
    }
    Box(
        clickModifier
            .fillMaxWidth()
            .height(22.dp)
            .padding(horizontal = 20.dp),
    ) {
        AnimatedContent(
            targetState = text,
            transitionSpec = { fadeIn(tween(200)) togetherWith fadeOut(tween(120)) },
            label = "row-header",
        ) { label ->
            Text(
                label,
                color = if (role < 0) CineColors.TextSecondary else if (role == 0) CineColors.TextPrimary else CineColors.TextMuted,
                fontSize = if (role < 0) 12.sp else 10.sp,
                fontWeight = if (role < 0) FontWeight.SemiBold else FontWeight.Bold,
                letterSpacing = if (role < 0) 0.sp else 1.4.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun CarouselLazyRow(
    levelIndex: Int,
    node: CarouselNode,
    role: Int,
    spec: SlotSpec,
    viewportWidth: Dp,
    catalog: Map<String, MediaItem>,
    accent: Color,
    callbacks: RowCallbacks,
) {
    val isActive = role == 0
    val listState = rememberLazyListState(initialFirstVisibleItemIndex = node.selectedIndex)
    val flingBehavior = rememberSnapFlingBehavior(listState, SnapPosition.Center)
    val haptics = LocalHapticFeedback.current
    val scope = rememberCoroutineScope()

    // GDD §7.2: always read the latest node/level/callback inside the long-lived effect.
    val currentNode by rememberUpdatedState(node)
    val currentLevel by rememberUpdatedState(levelIndex)
    val currentOnFocus by rememberUpdatedState(callbacks.onFocus)

    LaunchedEffect(listState, isActive) {
        // Settle on the node's selection first (e.g. after a branch switch or tap in Slot 3) …
        val target = currentNode.selectedIndex
        if (listState.centeredIndex() != target || listState.firstVisibleItemScrollOffset != 0) {
            listState.animateScrollToItem(target)
        }
        if (!isActive) return@LaunchedEffect
        // … then let the centred card drive Slot 3 in real time.
        snapshotFlow { listState.centeredIndex() }
            .filterNotNull()
            .distinctUntilChanged()
            .collect { index ->
                if (index != currentNode.selectedIndex) {
                    haptics.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                    currentOnFocus(currentLevel, index)
                }
            }
    }
    // Inactive rows stay locked on their selection.
    LaunchedEffect(node.selectedIndex, isActive) {
        if (!isActive && listState.centeredIndex() != node.selectedIndex) listState.animateScrollToItem(node.selectedIndex)
    }

    // Content padding (screenWidth − cardWidth) / 2 puts the selected card exactly in the middle
    // with scroll offset 0. Card width is animated, so the padding follows it frame by frame.
    val sidePadding = ((viewportWidth - spec.cardWidth) / 2).coerceAtLeast(0.dp)
    val glowStrength by animateFloatAsState(if (isActive) 1f else if (role < 0) 0.55f else 0f, label = "glow")

    LazyRow(
        state = listState,
        flingBehavior = flingBehavior,
        userScrollEnabled = isActive,
        contentPadding = PaddingValues(horizontal = sidePadding, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(spec.gap),
        modifier = Modifier
            .fillMaxWidth()
            .testTag(if (role in -1..1) "slot${role + 2}_carousel" else "offscreen_carousel"),
    ) {
        val onCardClick: (Int) -> Unit = { index ->
            when {
                role == 0 && index == node.selectedIndex -> callbacks.onAdvance()
                role == 0 -> scope.launch { listState.animateScrollToItem(index) }
                role > 0 -> callbacks.onAdvanceTo(index)
                else -> callbacks.onBranch(levelIndex, index)
            }
        }
        val itemModifier: (Int) -> Modifier = { index ->
            Modifier.graphicsLayer {
                val f = listState.centerFraction(index) * spec.focusWeight
                val scale = lerpF(1f, lerpF(0.94f, 1.05f, f), spec.focusWeight)
                scaleX = scale
                scaleY = scale
            }
        }
        fun look(index: Int) = CardLook(
            width = spec.cardWidth,
            height = spec.cardHeight,
            corner = spec.corner,
            detail = spec.detail,
            badge = spec.badge,
            saturation = spec.saturation,
            glow = if (index == node.selectedIndex) glowStrength else 0f,
            glowColor = if (role < 0) Color.White else accent,
        )
        if (node.isThemeRow) {
            itemsIndexed(node.themes, key = { _, t -> t.id }) { index, theme ->
                ThemeCard(theme, look(index), itemModifier(index)) { onCardClick(index) }
            }
        } else {
            itemsIndexed(node.items, key = { _, item -> item.id }) { index, item ->
                MediaCard(catalog[item.id] ?: item, look(index), itemModifier(index)) { onCardClick(index) }
            }
        }
    }
}

/** Index of the card nearest the horizontal centre of the viewport. */
internal fun LazyListState.centeredIndex(): Int? {
    val info = layoutInfo
    val visible = info.visibleItemsInfo
    if (visible.isEmpty()) return null
    val center = (info.viewportStartOffset + info.viewportEndOffset) / 2
    return visible.minByOrNull { abs(it.offset + it.size / 2 - center) }?.index
}

/** 1 when card [index] is perfectly centred, falling to 0 one card-width away. */
private fun LazyListState.centerFraction(index: Int): Float {
    val info = layoutInfo
    val item = info.visibleItemsInfo.firstOrNull { it.index == index } ?: return 0f
    val center = (info.viewportStartOffset + info.viewportEndOffset) / 2f
    val distance = abs(item.offset + item.size / 2f - center)
    return (1f - distance / item.size.coerceAtLeast(1)).coerceIn(0f, 1f)
}
