package com.example.jaamebaade_client.viewmodel


import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jaamebaade_client.repository.PoetDataManager
import com.example.jaamebaade_client.utility.DownloadStatus
import kotlinx.coroutines.launch
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.jaamebaade_client.model.Poet
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.mutableStateMapOf
import com.example.jaamebaade_client.api.PoetApiClient
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import java.io.File
import javax.inject.Inject

@HiltViewModel
class PoetViewModel @Inject constructor(
    private val poetDataManager: PoetDataManager,
    private val poetApiClient: PoetApiClient,
    @ApplicationContext private val context: Context
) : ViewModel() {
    var poets by mutableStateOf<List<Poet>>(emptyList())
        private set

    var isLoading by mutableStateOf(false)
        private set


    // Map to hold the download status for each poet
    val downloadStatus = mutableStateMapOf<String, DownloadStatus>()

    init {
        fetchPoets()
        loadSavedStatuses()

    }

    private fun loadSavedStatuses() {
        val statuses = poetDataManager.getAllDownloadStatuses()
        downloadStatus.putAll(statuses)
    }

    private fun fetchPoets() {
        viewModelScope.launch {
            try {
                isLoading = true
                val response = poetApiClient.getPoets()
                for (poet in response) {
                    Log.d("poets", "${poet.name}, + ${poet.id}") // TODO remove
                }
                isLoading = false
                if (response.isNotEmpty()) {
                    poets = response
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }
    }

    fun downloadAndExtractPoet(id: String, targetDirectory: File) {
        if (downloadStatus[id] == DownloadStatus.Downloaded) {
            Toast.makeText(context, "Already downloaded!", Toast.LENGTH_SHORT).show()
            return
        }
        // Set status to downloading
        downloadStatus[id] = DownloadStatus.Downloading

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = poetApiClient.downloadPoet(id)
                if (response.isSuccessful) {
                    response.body()?.let { body ->
                        val zipFile = File(targetDirectory, "poet_$id.zip")
                        poetDataManager.saveFile(body, zipFile)
                        val extractDir = File(targetDirectory, "poet_$id")
                        poetDataManager.extractZipFile(zipFile, extractDir)
                        zipFile.delete() // Clean up ZIP file after extraction
                        downloadStatus[id] = DownloadStatus.Downloaded
                        poetDataManager.saveDownloadStatus(id, DownloadStatus.Downloaded)
                    }
                } else {
                    // Handle the error
                    downloadStatus[id] = DownloadStatus.Failed

                }
            } catch (e: Exception) {
                e.printStackTrace()
                // Handle exception
                downloadStatus[id] = DownloadStatus.Failed

            }
        }
    }
}