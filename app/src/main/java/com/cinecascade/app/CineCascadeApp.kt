package com.cinecascade.app

import android.app.Application
import coil.ImageLoader
import coil.ImageLoaderFactory
import coil.disk.DiskCache
import coil.memory.MemoryCache
import com.cinecascade.core.remote.GeminiClient
import com.cinecascade.core.remote.OmdbClient
import com.cinecascade.core.repository.FileEnrichmentCache
import com.cinecascade.core.repository.MovieRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import okhttp3.OkHttpClient
import java.io.File
import java.util.concurrent.TimeUnit

class CineCascadeApp : Application(), ImageLoaderFactory {

    private val http by lazy {
        OkHttpClient.Builder()
            .connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    /** Manual DI: one repository for the whole app. */
    val repository: MovieRepository by lazy {
        val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
        MovieRepository(
            scope = scope,
            omdb = BuildConfig.OMDB_API_KEY.takeIf { it.isNotBlank() }?.let { OmdbClient(it, http) },
            gemini = BuildConfig.GEMINI_API_KEY.takeIf { it.isNotBlank() }
                ?.let { GeminiClient(it, http, model = BuildConfig.GEMINI_MODEL) },
            enrichmentCache = FileEnrichmentCache(File(filesDir, "omdb-cache.json")),
        )
    }

    /** Posters are cached in memory and on disk (GDD §4.4). */
    override fun newImageLoader(): ImageLoader =
        ImageLoader.Builder(this)
            .okHttpClient(http)
            .memoryCache { MemoryCache.Builder(this).maxSizePercent(0.25).build() }
            .diskCache { DiskCache.Builder().directory(cacheDir.resolve("posters")).maxSizeBytes(150L * 1024 * 1024).build() }
            .crossfade(true)
            .build()
}
