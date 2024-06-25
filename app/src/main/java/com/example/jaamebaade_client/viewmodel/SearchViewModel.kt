package com.example.jaamebaade_client.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jaamebaade_client.model.Poet
import com.example.jaamebaade_client.model.Verse
import com.example.jaamebaade_client.repository.PoetRepository
import com.example.jaamebaade_client.repository.VerseRepository
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
    var results by mutableStateOf<List<Verse>>(emptyList())
        private set

    private val _allPoets = MutableStateFlow<List<Poet>>(emptyList())
    val allPoets = _allPoets.asStateFlow()


    init {
        viewModelScope.launch {
            getAllPoets()
        }
    }

    fun search() {
        viewModelScope.launch {
            if (query.isEmpty()) {
                return@launch
            }
            runSearchOnDatabase()
        }
    }

    private suspend fun runSearchOnDatabase() {
        withContext(Dispatchers.IO) {
            Log.i("shit", "query: $query and poemfilter: ${poetFilter?.id}")
            results = verseRepository.searchVerses(query, poetFilter?.id)
            Log.i("shit", "runSearchOnDatabase: $results")
        }
    }

    private suspend fun getAllPoets() {
        withContext(Dispatchers.IO) {
            _allPoets.value = poetRepository.getAllPoets()
        }

    }
}