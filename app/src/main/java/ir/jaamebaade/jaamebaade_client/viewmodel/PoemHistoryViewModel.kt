package ir.jaamebaade.jaamebaade_client.viewmodel

import android.content.Context
import android.content.SharedPreferences
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

@HiltViewModel
class PoemHistoryViewModel @Inject constructor(@ApplicationContext context: Context) : ViewModel() {

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("poem_history", Context.MODE_PRIVATE)

    var poemHistory by mutableStateOf<List<Pair<String, String>>>(emptyList())
        private set

    init {
        loadPoemHistory()
    }

    private fun loadPoemHistory() {
        poemHistory = sharedPreferences.all.map {
            val timestamp = it.key
            val path = it.value as String
            timestamp to path
        }.sortedByDescending { it.first }
    }
}
