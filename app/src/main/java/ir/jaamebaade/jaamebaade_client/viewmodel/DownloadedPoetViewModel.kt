package ir.jaamebaade.jaamebaade_client.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ir.jaamebaade.jaamebaade_client.model.Poet
import ir.jaamebaade.jaamebaade_client.repository.CategoryRepository
import ir.jaamebaade.jaamebaade_client.repository.PoetRepository
import ir.jaamebaade.jaamebaade_client.utility.DownloadStatus
import ir.jaamebaade.jaamebaade_client.utility.DownloadStatusManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class DownloadedPoetViewModel @Inject constructor(
    private val poetRepository: PoetRepository,
    private val categoryRepository: CategoryRepository,
    private val downloadStatusManager: DownloadStatusManager,
) : ViewModel() {
    var poets by mutableStateOf<List<Poet>?>(null)
        private set

    init {
        getAllPoets()
    }

    suspend fun getPoetCategoryId(poetId: Int): Int {
        return withContext(Dispatchers.IO) {
            categoryRepository.getPoetCategoryId(poetId)
        }
    }

    fun deletePoets(selectedPoets: List<Poet>, onSuccess: () -> Unit) {
        if (selectedPoets.isEmpty()) {
            return
        }
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                poetRepository.deletePoets(selectedPoets)
                selectedPoets.forEach { changeDownloadStatusToNotDownloaded(it.id.toString()) }
                poets = poets!!.toMutableList().also { it.removeAll(selectedPoets) }
                onSuccess()
            }
        }
    }

    private fun changeDownloadStatusToNotDownloaded(poetId: String) {
        downloadStatusManager.saveDownloadStatus(poetId, DownloadStatus.NotDownloaded)
    }

    private fun getAllPoets() {
        viewModelScope.launch {
            poets = getAllDownloadedPoets()
        }
    }

    private suspend fun getAllDownloadedPoets(): List<Poet> {
        val res = withContext(Dispatchers.IO) {
            poetRepository.getAllPoets()
        }
        return res
    }
}
