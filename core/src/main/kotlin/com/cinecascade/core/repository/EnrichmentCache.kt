package com.cinecascade.core.repository

import com.cinecascade.core.remote.OmdbTitle
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import java.io.File
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

/** Persists OMDb payloads so each title is fetched once (the free OMDb tier allows 1,000 calls/day). */
interface EnrichmentCache {
    fun load(): Map<String, OmdbTitle>
    fun put(id: String, title: OmdbTitle)
}

class FileEnrichmentCache(private val file: File) : EnrichmentCache {
    private val adapter = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
        .adapter<Map<String, OmdbTitle>>(
            Types.newParameterizedType(Map::class.java, String::class.java, OmdbTitle::class.java),
        )
    private val entries = ConcurrentHashMap<String, OmdbTitle>()
    private val writer = Executors.newSingleThreadScheduledExecutor { r -> Thread(r, "omdb-cache").apply { isDaemon = true } }
    @Volatile private var pending = false

    override fun load(): Map<String, OmdbTitle> {
        val loaded = runCatching { if (file.exists()) adapter.fromJson(file.readText()) else null }.getOrNull().orEmpty()
        entries.putAll(loaded)
        return loaded
    }

    override fun put(id: String, title: OmdbTitle) {
        entries[id] = title
        if (!pending) {
            pending = true
            writer.schedule({
                pending = false
                runCatching {
                    val tmp = File(file.parentFile, file.name + ".tmp")
                    tmp.writeText(adapter.toJson(HashMap(entries)))
                    tmp.renameTo(file)
                }
            }, 2, TimeUnit.SECONDS)
        }
    }
}
