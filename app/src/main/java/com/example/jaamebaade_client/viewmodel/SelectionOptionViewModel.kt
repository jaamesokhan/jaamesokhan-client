package com.example.jaamebaade_client.viewmodel

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jaamebaade_client.api.DictionaryApiClient
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
    private val dictionaryApiClient: DictionaryApiClient,
) : ViewModel() {

    var verse by mutableStateOf<VerseWithHighlights?>(null)
        private set

    var highlights = MutableLiveData<List<Highlight>>()
        private set

    private val _apiResult = mutableStateOf("")
    val apiResult: State<String> = _apiResult

    init {
        findVerse(verseId)
    }

    private fun findVerse(verseId: Int) {
        viewModelScope.launch {
            verse = getVerseFromRepository(verseId)
            highlights.value = verse?.highlights
        }
    }

    private suspend fun getVerseFromRepository(verseId: Int): VerseWithHighlights {
        val res = withContext(Dispatchers.IO) {
            verseRepository.getVerseWithHighlights(verseId)
        }
        return res
    }

    fun getWordMeaning(word: String) {
        viewModelScope.launch {
            val res = dictionaryApiClient.getMeaning(word)
            Log.e("fuck", "getWordMeaning: $res", )
            _apiResult.value = res?: "هیچی"
        }
    }

    fun highlight(verseId: Int, startIndex: Int, endIndex: Int) {
        viewModelScope.launch {
            val highlight = addHighlightToRepository(verseId, startIndex, endIndex)
            highlights.value = highlights.value?.toMutableList()?.apply {
                add(highlight)
            }
            Log.e("shit", "highlight: $highlight")
        }
    }

//    fun checkHighlighted(index: Int, verse: VerseWithHighlights?): Highlight? {
//        val highlights = verse?.highlights
//        if (highlights != null) {
//            for (highlight in highlights) {
//                if (index >= highlight.startIndex && index <= highlight.endIndex) {
//                    return highlight
//                }
//            }
//        }
//        return null
//    }

    @AssistedFactory
    interface SelectionOptionViewModelFactory {
        fun create(
            @Assisted verseId: Int,
        ): SelectionOptionViewModel
    }

    private suspend fun addHighlightToRepository(
        verseId: Int, startIndex: Int, endIndex: Int
    ): Highlight {
        val highlight = Highlight(verseId = verseId, startIndex = startIndex, endIndex = endIndex)
        withContext(Dispatchers.IO) {
            highlightRepository.insertHighlight(highlight)
        }
        return highlight
    }
}