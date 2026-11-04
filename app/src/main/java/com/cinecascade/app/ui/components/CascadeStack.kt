package com.cinecascade.app.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.cinecascade.app.viewmodel.DiscoveryActions
import com.cinecascade.core.cascade.CascadeTree
import com.cinecascade.core.model.MediaItem
import kotlinx.coroutines.launch
import kotlin.math.abs

/** GDD §5: vertical drag threshold in px. */
internal const val SWIPE_THRESHOLD_PX = 150f

/** Spring used for every vertical cascade move: a touch of bounce, settles fast. */
internal val CascadeSpring = spring<Float>(dampingRatio = 0.78f, stiffness = Spring.StiffnessLow * 1.4f)

/**
 * Slots 1–3. Every row has an animated *position* (−1, 0, 1, off-screen ±2). Advancing shifts
 * all targets by −1, so the whole stack glides up: Slot 3 grows into Slot 2, Slot 2 shrinks and
 * greys out into Slot 1, Slot 1 slides off the top and a new preview rises from below. Going
 * back plays the exact reverse. While the finger drags vertically, rows follow it live.
 */
@Composable
internal fun CascadeStack(
    tree: CascadeTree,
    catalog: Map<String, MediaItem>,
    accent: Color,
    actions: DiscoveryActions,
    modifier: Modifier = Modifier,
) {
    val haptics = LocalHapticFeedback.current
    val scope = rememberCoroutineScope()
    val dragShift = remember { Animatable(0f) }
    val latestTree by rememberUpdatedState(tree)
    val callbacks = remember(actions) {
        RowCallbacks(
            onFocus = actions::onFocus,
            onAdvance = {
                haptics.performHapticFeedback(HapticFeedbackType.LongPress)
                actions.onAdvance()
            },
            onAdvanceTo = { index ->
                haptics.performHapticFeedback(HapticFeedbackType.LongPress)
                actions.onAdvanceTo(index)
            },
            onBranch = actions::onBranch,
        )
    }

    BoxWithConstraints(modifier.clipToBounds()) {
        val geometry = remember(maxHeight) { SlotGeometry(maxHeight) }
        val stepPx = with(LocalDensity.current) { 260.dp.toPx() }
        val viewportWidth = maxWidth

        Box(
            Modifier
                .fillMaxSize()
                .pointerInput(Unit) {
                    var total = 0f
                    detectVerticalDragGestures(
                        onDragStart = { total = 0f },
                        onVerticalDrag = { change, dragAmount ->
                            change.consume()
                            total += dragAmount
                            val t = latestTree
                            val raw = total / stepPx
                            val allowed = (raw < 0 && t.canAdvance) || (raw > 0 && t.canGoBack)
                            // Rubber-band when there is nowhere to go.
                            val shift = (if (allowed) raw else raw * 0.2f).coerceIn(-0.65f, 0.65f)
                            scope.launch { dragShift.snapTo(shift) }
                        },
                        onDragEnd = {
                            val t = latestTree
                            if (abs(total) > SWIPE_THRESHOLD_PX) {
                                if (total < 0 && t.canAdvance) callbacks.onAdvance()
                                if (total > 0 && t.canGoBack) {
                                    haptics.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                                    actions.onBack()
                                }
                            }
                            // Row targets have just moved by ±1; easing the shift to 0 at the same
                            // time keeps the motion continuous from wherever the finger left it.
                            scope.launch { dragShift.animateTo(0f, CascadeSpring) }
                        },
                        onDragCancel = { scope.launch { dragShift.animateTo(0f, CascadeSpring) } },
                    )
                },
        ) {
            val active = tree.activeLevelIndex
            val first = (active - 2).coerceAtLeast(0)
            val last = (active + 2).coerceAtMost(tree.levels.lastIndex)
            for (levelIndex in first..last) {
                key(levelIndex) {
                    val role = levelIndex - active
                    val position = remember {
                        // New rows enter from beyond their slot (a fresh preview rises from below).
                        Animatable(if (role > 0) role + 1f else if (role < 0) role - 1f else 0f)
                    }
                    LaunchedEffect(role) { position.animateTo(role.toFloat(), CascadeSpring) }
                    val p = position.value + dragShift.value
                    CascadeRow(
                        levelIndex = levelIndex,
                        node = tree.levels[levelIndex],
                        role = role,
                        spec = geometry.at(p),
                        viewportWidth = viewportWidth,
                        catalog = catalog,
                        accent = accent,
                        callbacks = callbacks,
                        modifier = Modifier.zIndex(if (role == 0) 2f else 1f - abs(role) * 0.1f),
                    )
                }
            }
        }
    }
}
