package com.cinecascade.core.remote

import com.cinecascade.core.model.DiscoveryFilters
import com.cinecascade.core.model.MediaItem
import com.cinecascade.core.model.MediaType
import com.cinecascade.core.model.ThemeVibe
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody

/** One structured recommendation returned by Gemini. */
data class AiSuggestion(
    val title: String = "",
    val year: Int = 0,
    val imdbId: String? = null,
    val type: String = "movie",
    val genres: List<String> = emptyList(),
    val director: String = "",
    val cast: List<String> = emptyList(),
    val synopsis: String = "",
    val reason: String = "",
) {
    val mediaType: MediaType get() = if (type.contains("series", true) || type.contains("show", true)) MediaType.SERIES else MediaType.MOVIE
}

internal data class AiEnvelope(val recommendations: List<AiSuggestion> = emptyList())

internal data class GeminiResponse(val candidates: List<GeminiCandidate>? = null)
internal data class GeminiCandidate(val content: GeminiContent? = null)
internal data class GeminiContent(val parts: List<GeminiPart>? = null)
internal data class GeminiPart(val text: String? = null)

/**
 * Generative expansion via the Gemini REST API (GDD §4.3). Returns structured JSON with similar
 * titles and IMDb ids, which the repository registers in the catalog and enriches through OMDb.
 */
class GeminiClient(
    private val apiKey: String,
    private val http: OkHttpClient,
    val model: String = DEFAULT_MODEL,
    private val baseUrl: String = "https://generativelanguage.googleapis.com/v1beta/",
) {
    private val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
    private val requestAdapter = moshi.adapter<Map<String, Any?>>(
        Types.newParameterizedType(Map::class.java, String::class.java, Any::class.java),
    )
    private val responseAdapter = moshi.adapter(GeminiResponse::class.java)
    private val envelopeAdapter = moshi.adapter(AiEnvelope::class.java)

    suspend fun similarTitles(
        source: MediaItem,
        theme: ThemeVibe?,
        filters: DiscoveryFilters,
        knownTitles: List<String>,
        count: Int = 12,
    ): List<AiSuggestion> = withContext(Dispatchers.IO) {
        val body = requestAdapter.toJson(requestBody(buildPrompt(source, theme, filters, knownTitles, count)))
        // Try the working model first, then the configured one, then known-good fallbacks.
        val candidates = (listOfNotNull(workingModel) + model + FALLBACK_MODELS).distinct()
        for (name in candidates) {
            val request = Request.Builder()
                .url("${baseUrl}models/$name:generateContent")
                .header("x-goog-api-key", apiKey)
                .post(body.toRequestBody("application/json".toMediaType()))
                .build()
            val outcome = runCatching {
                http.newCall(request).execute().use { response ->
                    when {
                        response.isSuccessful -> parseResponse(response.body?.string().orEmpty())
                        response.code == 404 || response.code == 400 -> null // unknown model: try the next
                        else -> emptyList() // rate limit, bad key, server error: give up quietly
                    }
                }
            }.getOrDefault(emptyList())
            if (outcome != null) {
                workingModel = name
                return@withContext outcome
            }
        }
        emptyList()
    }

    @Volatile private var workingModel: String? = null

    internal fun parseResponse(raw: String): List<AiSuggestion> {
        val text = runCatching { responseAdapter.fromJson(raw) }.getOrNull()
            ?.candidates?.firstOrNull()?.content?.parts
            ?.mapNotNull { it.text }?.joinToString("")
            ?: return emptyList()
        val json = text.trim().removePrefix("```json").removePrefix("```").removeSuffix("```").trim()
        return runCatching { envelopeAdapter.fromJson(json)?.recommendations }.getOrNull().orEmpty()
            .filter { it.title.isNotBlank() && it.year in 1880..2100 }
    }

    internal fun buildPrompt(
        source: MediaItem,
        theme: ThemeVibe?,
        filters: DiscoveryFilters,
        knownTitles: List<String>,
        count: Int,
    ): String = buildString {
        appendLine("You are the recommendation brain of CineCascade, a movie discovery app.")
        appendLine("The user is focused on: \"${source.title}\" (${source.year}), a ${source.type.name.lowercase()}.")
        appendLine("Genres: ${source.genres.joinToString()}. Director/creator: ${source.director}.")
        if (source.cast.isNotEmpty()) appendLine("Lead cast: ${source.cast.take(4).joinToString()}.")
        theme?.let { appendLine("Current vibe: ${it.name} — ${it.moodTag}.") }
        if (filters.mediaTypes.isNotEmpty()) {
            appendLine("Only recommend: ${filters.mediaTypes.joinToString { if (it == MediaType.SERIES) "TV series" else "movies" }}.")
        }
        if (filters.genres.isNotEmpty()) appendLine("Each pick must include at least one of these genres: ${filters.genres.joinToString()}.")
        appendLine("Recommend $count real, existing titles a fan would love next. Mix well-known favourites with hidden gems, and vary decades, countries and tone so the user keeps discovering new corners.")
        if (knownTitles.isNotEmpty()) appendLine("Do not include: ${knownTitles.take(60).joinToString("; ")}.")
        appendLine("Use exact IMDb ids (tt followed by digits). type is \"movie\" or \"series\". Keep synopsis under 160 characters.")
    }

    private fun requestBody(prompt: String): Map<String, Any?> {
        val str = mapOf("type" to "STRING")
        val schema = mapOf(
            "type" to "OBJECT",
            "properties" to mapOf(
                "recommendations" to mapOf(
                    "type" to "ARRAY",
                    "items" to mapOf(
                        "type" to "OBJECT",
                        "properties" to mapOf(
                            "title" to str,
                            "year" to mapOf("type" to "INTEGER"),
                            "imdbId" to str,
                            "type" to mapOf("type" to "STRING", "enum" to listOf("movie", "series")),
                            "genres" to mapOf("type" to "ARRAY", "items" to str),
                            "director" to str,
                            "cast" to mapOf("type" to "ARRAY", "items" to str),
                            "synopsis" to str,
                            "reason" to str,
                        ),
                        "required" to listOf("title", "year", "imdbId", "type", "genres"),
                    ),
                ),
            ),
            "required" to listOf("recommendations"),
        )
        return mapOf(
            "contents" to listOf(mapOf("role" to "user", "parts" to listOf(mapOf("text" to prompt)))),
            "generationConfig" to mapOf(
                "responseMimeType" to "application/json",
                "responseSchema" to schema,
                "temperature" to 0.8,
            ),
        )
    }

    companion object {
        const val DEFAULT_MODEL = "gemini-3.5-flash"
        private val FALLBACK_MODELS = listOf("gemini-2.5-flash", "gemini-2.0-flash")
    }
}
