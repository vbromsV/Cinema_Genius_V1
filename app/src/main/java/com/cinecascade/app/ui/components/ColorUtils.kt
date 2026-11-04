package com.cinecascade.app.ui.components

import androidx.compose.ui.graphics.Color
import com.cinecascade.core.model.ColorProfile

internal fun lerpF(start: Float, stop: Float, fraction: Float): Float = start + (stop - start) * fraction

/** Blend toward luminance grey. saturation 1 = original, 0 = monochrome. */
internal fun Color.desaturate(saturation: Float): Color {
    if (saturation >= 0.999f) return this
    val l = 0.299f * red + 0.587f * green + 0.114f * blue
    return Color(lerpF(l, red, saturation), lerpF(l, green, saturation), lerpF(l, blue, saturation), alpha)
}

internal val ColorProfile.primaryColor: Color get() = Color(primary.toInt())
internal val ColorProfile.secondaryColor: Color get() = Color(secondary.toInt())
