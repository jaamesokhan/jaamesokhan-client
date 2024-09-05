package ir.jaamebaade.jaamebaade_client.viewmodel

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import ir.jaamebaade.jaamebaade_client.model.PoemWithPoet
import ir.jaamebaade.jaamebaade_client.repository.PoemRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject
@HiltViewModel
class PoemHistoryViewModel @Inject constructor(
    @ApplicationContext context: Context,
    private val poemRepository: PoemRepository
) : ViewModel() {

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("poem_history", Context.MODE_PRIVATE)


    // List of PoemWithPoet objects for the history
    var poemHistory by mutableStateOf<List<PoemWithPoet>>(emptyList())
        private set

    init {
        loadPoemHistory()
    }

    // Fetch PoemWithPoet from the repository
    private suspend fun fetchPoemWithPoet(poetId: String, poemId: String): PoemWithPoet? {
        return withContext(Dispatchers.IO) {
            try {
                poemRepository.getPoemWithPoet(poemId.toInt())
            } catch (e: Exception) {
                null
            }
        }
    }

    private fun loadPoemHistory() {
        viewModelScope.launch {
            val historyList = mutableListOf<Pair<Long, PoemWithPoet>>()

            sharedPreferences.all.forEach { entry ->
                val timestamp = entry.key.toLongOrNull() // Convert timestamp key to Long
                val path = entry.value as String
                val poemDetails = path.split("/")

                val poetId = poemDetails.getOrNull(1) ?: ""
                val poemId = poemDetails.getOrNull(2) ?: ""
                val verseId = poemDetails.getOrNull(3)?.toIntOrNull()

                // Fetch the PoemWithPoet object using the parsed poemId
                val poemWithPoet = fetchPoemWithPoet(poetId, poemId)

                poemWithPoet?.let {
                    timestamp?.let {
                        historyList.add(Pair(timestamp, poemWithPoet))
                    }
                }
            }

            // Sort by timestamp in descending order (most recent first)
            val sortedHistoryList = historyList.sortedByDescending { it.first }.map { it.second }

            // Update the poem history with the sorted list of PoemWithPoet objects
            poemHistory = sortedHistoryList
        }
    }

    // Optional: Function to clear the history from SharedPreferences
    fun clearPoemHistory() {
        sharedPreferences.edit().clear().apply()
        poemHistory = emptyList()
    }
}
