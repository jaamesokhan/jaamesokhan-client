package ir.jaamebaade.jaamebaade_client.viewmodel


import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import ir.jaamebaade.jaamebaade_client.R
import ir.jaamebaade.jaamebaade_client.api.PoetApiClient
import ir.jaamebaade.jaamebaade_client.datamanager.PoetDataManager
import ir.jaamebaade.jaamebaade_client.model.Poet
import ir.jaamebaade.jaamebaade_client.repository.CategoryRepository
import ir.jaamebaade.jaamebaade_client.repository.PoemRepository
import ir.jaamebaade.jaamebaade_client.repository.PoetRepository
import ir.jaamebaade.jaamebaade_client.repository.VerseRepository
import ir.jaamebaade.jaamebaade_client.utility.DownloadStatus
import ir.jaamebaade.jaamebaade_client.utility.importCategoryData
import ir.jaamebaade.jaamebaade_client.utility.importPoemData
import ir.jaamebaade.jaamebaade_client.utility.importVerseData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import javax.inject.Inject

@HiltViewModel
class PoetViewModel @Inject constructor(
    private val poetDataManager: PoetDataManager,
    private val poetApiClient: PoetApiClient,
    @ApplicationContext private val context: Context,
    private val categoryRepository: CategoryRepository,
    private val poetRepository: PoetRepository,
    private val poemRepository: PoemRepository,
    private val verseRepository: VerseRepository

) : ViewModel() {
    var poets by mutableStateOf<List<Poet>>(emptyList())
        private set

    var isLoading by mutableStateOf(false)
        private set

    var currentPage by mutableStateOf(0)
        private set

    private val pageSize = 20

    var searchQuery by mutableStateOf("")

    private var searchJob: Job? = null

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
                val response = poetApiClient.getPoets(currentPage, pageSize, searchQuery)
                if (response != null) {
                    poets = poets + response
                    currentPage++
                    isLoading = false
                }
            } catch (e: Exception) {
                e.printStackTrace()
                isLoading = false
            }
        }
    }

    fun loadMorePoets() {
        if (!isLoading) {
            fetchPoets()
        }
    }

    fun updateSearchQuery(query: String) {
        searchQuery = query
        currentPage = 0
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(500) // debounce time
            poets = emptyList()
            fetchPoets()
        }
    }

    fun importPoetData(id: String, targetDir: File) {

        downloadAndExtractPoet(id, targetDir, {
            try {
                if (downloadStatus[id] == DownloadStatus.Downloaded) {
                    var dir = targetDir.path + "/poet_$id" + "/category_$id.csv"
                    importCategoryData(dir, categoryRepository)
                    dir = targetDir.path + "/poet_$id" + "/poems_$id.csv"
                    importPoemData(dir, poemRepository)
                    dir = targetDir.path + "/poet_$id" + "/verses_$id.csv"
                    importVerseData(dir, verseRepository)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }, {})
    }

    private fun downloadAndExtractPoet(
        id: String,
        targetDirectory: File,
        onSuccess: () -> Unit,
        onFailure: () -> Unit
    ) {
        if (downloadStatus[id] == DownloadStatus.Downloaded) {
            Toast.makeText(
                context,
                context.getString(R.string.ALREADY_DOWNLOADED), Toast.LENGTH_SHORT
            ).show()
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
                        insertDownloadedPoet(poets.find { it.id.toString() == id }!!)
                        onSuccess()
                    }
                } else {
                    // Handle the error
                    Log.e(
                        PoetViewModel::class.simpleName,
                        "download poet failed with ${response.body()}",
                    )
                    downloadStatus[id] = DownloadStatus.Failed
                }
            } catch (e: Exception) {
                e.printStackTrace()
                // Handle exception
                downloadStatus[id] = DownloadStatus.Failed
                onFailure()
            }
        }
    }


    private suspend fun insertDownloadedPoet(poet: Poet) {
        withContext(Dispatchers.IO) {
            poetRepository.insertPoet(poet)
        }

    }

}