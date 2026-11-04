package com.cinecascade.app.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest

/** OMDb poster via Coil (memory + disk cache). Saturation < 1 renders it toward monochrome (Slot 1). */
@Composable
internal fun PosterImage(
    url: String?,
    contentDescription: String?,
    saturation: Float,
    modifier: Modifier = Modifier,
    placeholder: @Composable () -> Unit,
) {
    if (url == null) {
        placeholder()
        return
    }
    val context = LocalContext.current
    val request = remember(url) { ImageRequest.Builder(context).data(url).crossfade(250).build() }
    // Quantise so the filter isn't rebuilt on every animation frame.
    val step = (saturation.coerceIn(0f, 1f) * 20).toInt()
    val filter = remember(step) {
        if (step >= 20) null else ColorFilter.colorMatrix(ColorMatrix().apply { setToSaturation(step / 20f) })
    }
    SubcomposeAsyncImage(
        model = request,
        contentDescription = contentDescription,
        modifier = modifier,
        loading = { placeholder() },
        error = { placeholder() },
        contentScale = ContentScale.Crop,
        colorFilter = filter,
    )
}
