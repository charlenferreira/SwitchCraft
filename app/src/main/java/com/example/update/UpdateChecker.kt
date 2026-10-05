package com.example.update

import com.example.BuildConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL

data class UpdateInfo(
    val hasUpdate: Boolean,
    val latestVersion: String,
    val releaseNotes: String,
    val releaseUrl: String
)

object UpdateChecker {

    private const val GITHUB_API_URL = "https://api.github.com/repos/charlenferreira/SwitchCraft/releases/latest"

    suspend fun checkForUpdates(): Result<UpdateInfo> = withContext(Dispatchers.IO) {
        try {
            val url = URL(GITHUB_API_URL)
            val connection = (url.openConnection() as HttpURLConnection).apply {
                requestMethod = "GET"
                connectTimeout = 6000
                readTimeout = 6000
                setRequestProperty("Accept", "application/vnd.github.v3+json")
                setRequestProperty("User-Agent", "SwitchCraft-Android-App")
            }

            if (connection.responseCode == 200) {
                val responseText = connection.inputStream.bufferedReader().use { it.readText() }
                val json = JSONObject(responseText)

                val tagName = json.optString("tag_name", "").removePrefix("v")
                val releaseNotes = json.optString("body", "Melhorias de desempenho e novos switches mecânicos.")
                val htmlUrl = json.optString("html_url", "https://github.com/charlenferreira/SwitchCraft/releases")

                val currentVersion = BuildConfig.VERSION_NAME
                val isNewer = isVersionNewer(tagName, currentVersion)

                Result.success(
                    UpdateInfo(
                        hasUpdate = isNewer,
                        latestVersion = tagName.ifEmpty { currentVersion },
                        releaseNotes = releaseNotes,
                        releaseUrl = htmlUrl
                    )
                )
            } else {
                Result.success(
                    UpdateInfo(
                        hasUpdate = false,
                        latestVersion = BuildConfig.VERSION_NAME,
                        releaseNotes = "Você já está na versão mais recente oficial do SwitchCraft!",
                        releaseUrl = "https://github.com/charlenferreira/SwitchCraft/releases"
                    )
                )
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private fun isVersionNewer(latest: String, current: String): Boolean {
        if (latest.isBlank()) return false
        val latestParts = latest.split(".").mapNotNull { it.toIntOrNull() }
        val currentParts = current.split(".").mapNotNull { it.toIntOrNull() }

        val maxLen = maxOf(latestParts.size, currentParts.size)
        for (i in 0 until maxLen) {
            val l = latestParts.getOrElse(i) { 0 }
            val c = currentParts.getOrElse(i) { 0 }
            if (l > c) return true
            if (l < c) return false
        }
        return false
    }
}
