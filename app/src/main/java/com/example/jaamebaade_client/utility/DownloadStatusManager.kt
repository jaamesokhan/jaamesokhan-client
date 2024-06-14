package com.example.jaamebaade_client.utility

import android.content.Context
import android.content.SharedPreferences

class DownloadStatusManager(context: Context) {


    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("download_status_prefs", Context.MODE_PRIVATE)

    fun saveDownloadStatus(poetId: String, status: DownloadStatus) {
        sharedPreferences.edit().putString(poetId, status.name).apply()
    }

    fun getDownloadStatus(poetId: String): DownloadStatus {
        val status = sharedPreferences.getString(poetId, DownloadStatus.NotDownloaded.name)
        return DownloadStatus.valueOf(status ?: DownloadStatus.NotDownloaded.name)
    }

    fun getAllDownloadStatuses(): Map<String, DownloadStatus> {
        return sharedPreferences.all.mapValues { DownloadStatus.valueOf(it.value as String) }
    }
}

enum class DownloadStatus {
    NotDownloaded,
    Downloading,
    Downloaded,
    Failed
}