package com.example.jaamebaade_client.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jaamebaade_client.model.Highlight
import com.example.jaamebaade_client.model.VerseWithHighlights
import com.example.jaamebaade_client.repository.HighlightRepository
import com.example.jaamebaade_client.repository.VerseRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@HiltViewModel(assistedFactory = SelectionOptionViewModel.SelectionOptionViewModelFactory::class)
class SelectionOptionViewModel @AssistedInject constructor(
    private val verseRepository: VerseRepository, @Assisted private val verseId: Int,
    private val highlightRepository: HighlightRepository,
) : ViewModel() {

    var verse by mutableStateOf<VerseWithHighlights?>(null)
        private set

    init {
        findVerse(verseId)
    }

    private fun findVerse(verseId: Int) {
        viewModelScope.launch {
            verse = getVerseFromRepository(verseId)
        }
    }

    private suspend fun getVerseFromRepository(verseId: Int): VerseWithHighlights {
        val res = withContext(Dispatchers.IO) {
            verseRepository.getVerseWithHighlights(verseId)
        }
        Log.e("fuck", "getVerseFromRepository: ${res.highlights}", )
        return res
    }

    fun highlight(verseId: Int, startIndex: Int, endIndex: Int) {
        viewModelScope.launch {
            addHighlightToRepository(verseId, startIndex, endIndex)
        }
    }

    fun checkHighlighted(index: Int, verse: VerseWithHighlights?): Highlight? {
        val highlights = verse?.highlights
        if (highlights != null) {
            for (highlight in highlights) {
                if (index >= highlight.startIndex && index <= highlight.endIndex) {
                    return highlight
                }
            }
        }
        return null
    }

    @AssistedFactory
    interface SelectionOptionViewModelFactory {
        fun create(
            @Assisted verseId: Int,
        ): SelectionOptionViewModel
    }

    private suspend fun addHighlightToRepository(verseId: Int, startIndex: Int, endIndex: Int) {
        withContext(Dispatchers.IO) {
            val highlight = Highlight(verseId = verseId, startIndex = startIndex, endIndex = endIndex)
            highlightRepository.insertHighlight(highlight)
        }
    }
}