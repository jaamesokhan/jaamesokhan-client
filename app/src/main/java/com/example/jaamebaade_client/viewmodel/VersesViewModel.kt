package com.example.jaamebaade_client.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jaamebaade_client.model.Verse
import com.example.jaamebaade_client.repository.VerseRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel(assistedFactory = VersesViewModel.VerseViewModelFactory::class)
class VersesViewModel @AssistedInject constructor(
    @Assisted("poemId") val poemId: Int,
    val repository: VerseRepository
) : ViewModel() {

    @AssistedFactory
    interface VerseViewModelFactory {
        fun create(
            @Assisted("poemId") poemId: Int,
        ): VersesViewModel
    }

    private val _verses = MutableStateFlow<List<Verse>>(emptyList())
    val verses: StateFlow<List<Verse>> = _verses

    init {
        fetchPoemVerses()
    }

    private fun fetchPoemVerses() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val verses = repository.getPoemVerses(poemId)
                _verses.value = verses
            }
        }
    }
}