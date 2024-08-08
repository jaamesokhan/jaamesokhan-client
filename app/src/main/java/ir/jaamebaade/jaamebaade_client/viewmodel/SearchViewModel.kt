package ir.jaamebaade.jaamebaade_client.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ir.jaamebaade.jaamebaade_client.model.Poet
import ir.jaamebaade.jaamebaade_client.model.VersePoemCategoryPoet
import ir.jaamebaade.jaamebaade_client.repository.PoetRepository
import ir.jaamebaade.jaamebaade_client.repository.VerseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val verseRepository: VerseRepository,
    private val poetRepository: PoetRepository,
) : ViewModel() {
    var query by mutableStateOf("")

    var poetFilter by mutableStateOf<Poet?>(null)
    var results by mutableStateOf<List<VersePoemCategoryPoet>>(emptyList())
        private set

    private val _allPoets = MutableStateFlow<List<Poet>>(emptyList())
    val allPoets = _allPoets.asStateFlow()


    init {
        viewModelScope.launch {
            getAllPoets()
        }
    }

    fun search(callBack: () -> Unit) {
        viewModelScope.launch {
            if (query.isEmpty()) {
                callBack()
                return@launch
            }
            runSearchOnDatabase(callBack)
        }
    }

    private suspend fun runSearchOnDatabase(callBack: () -> Unit) {
        withContext(Dispatchers.IO) {
            results = verseRepository.searchVerses(query, poetFilter?.id)
            callBack()
        }
    }

    private suspend fun getAllPoets() {
        withContext(Dispatchers.IO) {
            _allPoets.value = poetRepository.getAllPoets()
        }

    }
}