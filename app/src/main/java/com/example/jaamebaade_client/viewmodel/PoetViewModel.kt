package com.example.jaamebaade_client.viewmodel


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jaamebaade_client.repository.PoetRepository
import com.example.jaamebaade_client.utility.DownloadStatus
import kotlinx.coroutines.launch
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.jaamebaade_client.model.Poet
import android.util.Log
import androidx.compose.runtime.mutableStateMapOf
import kotlinx.coroutines.Dispatchers
import java.io.File

class PoetViewModel : ViewModel() {
    private val poetRepository = PoetRepository()

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
        val statuses = poetRepository.getAllDownloadStatuses()
        downloadStatus.putAll(statuses)
    }

    private fun fetchPoets() {
        viewModelScope.launch {
            try {
                isLoading = true
                val response = poetRepository.getPoets()
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
        // Set status to downloading
        downloadStatus[id] = DownloadStatus.Downloading

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = poetRepository.downloadPoet(id)
                if (response.isSuccessful) {
                    response.body()?.let { body ->
                        val zipFile = File(targetDirectory, "poet_$id.zip")
                        poetRepository.saveFile(body, zipFile)
                        val extractDir = File(targetDirectory, "poet_$id")
                        poetRepository.extractZipFile(zipFile, extractDir)
                        zipFile.delete() // Clean up ZIP file after extraction
                        downloadStatus[id] = DownloadStatus.Downloaded

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