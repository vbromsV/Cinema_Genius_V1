package com.cinecascade.app.update

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.provider.Settings
import androidx.core.content.FileProvider
import com.cinecascade.app.BuildConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import java.io.File

/**
 * Self-update from GitHub Releases. On an unmetered connection (Wi-Fi) it asks GitHub for the
 * latest release, and when its build number is higher than this app's it downloads the APK in
 * the background. Android always asks the user to confirm the install itself.
 */
class AppUpdater(private val context: Context, private val http: OkHttpClient) {

    data class Update(val versionCode: Int, val apk: File)

    /** Returns a downloaded, newer APK ready to install, or null. Never throws. */
    suspend fun checkAndDownload(): Update? = withContext(Dispatchers.IO) {
        if (BuildConfig.UPDATE_REPO.isBlank() || !isUnmetered()) return@withContext null
        runCatching {
            val release = http.newCall(
                Request.Builder()
                    .url("https://api.github.com/repos/${BuildConfig.UPDATE_REPO}/releases/latest")
                    .header("Accept", "application/vnd.github+json")
                    .build(),
            ).execute().use { response ->
                if (!response.isSuccessful) return@runCatching null
                JSONObject(response.body?.string().orEmpty())
            }

            // Tags look like "v42": 42 is the CI build number, used as versionCode.
            val remoteVersion = release.optString("tag_name").filter(Char::isDigit).toIntOrNull()
                ?: return@runCatching null
            if (remoteVersion <= BuildConfig.VERSION_CODE) return@runCatching null

            val assets = release.optJSONArray("assets") ?: return@runCatching null
            val apkUrl = (0 until assets.length())
                .map { assets.getJSONObject(it) }
                .firstOrNull { it.optString("name").endsWith(".apk") }
                ?.optString("browser_download_url")
                ?.takeIf { it.isNotBlank() }
                ?: return@runCatching null

            val dir = File(context.cacheDir, "updates").apply { mkdirs() }
            val target = File(dir, "cinecascade-$remoteVersion.apk")
            dir.listFiles()?.filter { it != target }?.forEach { it.delete() }
            if (!target.exists()) {
                val partial = File(dir, "download.part")
                http.newCall(Request.Builder().url(apkUrl).build()).execute().use { response ->
                    val body = response.body
                    if (!response.isSuccessful || body == null) return@runCatching null
                    body.byteStream().use { input -> partial.outputStream().use { input.copyTo(it) } }
                }
                if (!partial.renameTo(target)) return@runCatching null
            }
            Update(remoteVersion, target)
        }.getOrNull()
    }

    /**
     * Opens the system installer. The first time, Android needs permission to install apps from
     * CineCascade: then its settings page opens instead, and the user taps Install again after.
     */
    fun install(update: Update) {
        if (!context.packageManager.canRequestPackageInstalls()) {
            context.startActivity(
                Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES, Uri.parse("package:${context.packageName}"))
                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK),
            )
            return
        }
        val uri = FileProvider.getUriForFile(context, "${context.packageName}.updates", update.apk)
        context.startActivity(
            Intent(Intent.ACTION_VIEW)
                .setDataAndType(uri, "application/vnd.android.package-archive")
                .addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_ACTIVITY_NEW_TASK),
        )
    }

    private fun isUnmetered(): Boolean {
        val cm = context.getSystemService(ConnectivityManager::class.java) ?: return false
        val caps = cm.getNetworkCapabilities(cm.activeNetwork) ?: return false
        return caps.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) &&
            caps.hasCapability(NetworkCapabilities.NET_CAPABILITY_NOT_METERED)
    }
}
