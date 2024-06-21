package com.example.jaamebaade_client.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jaamebaade_client.model.Highlight
import com.example.jaamebaade_client.model.Verse
import com.example.jaamebaade_client.repository.BookmarkRepository
import com.example.jaamebaade_client.repository.HighlightRepository
import com.example.jaamebaade_client.repository.PoemRepository
import com.example.jaamebaade_client.repository.PoetRepository
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

@HiltViewModel(assistedFactory = VersesViewModel.VerseViewModelFactory::class)
class VersesViewModel @AssistedInject constructor(
    @Assisted("poemId") val poemId: Int,
    @Assisted("poetId") val poetId: Int,
    private val versesRepository: VerseRepository,
    val highlightRepository: HighlightRepository,
    private val poetRepository: PoetRepository,
    private val poemRepository: PoemRepository,
    private val bookmarkRepository: BookmarkRepository
) : ViewModel() {

    @AssistedFactory
    interface VerseViewModelFactory {
        fun create(
            @Assisted("poemId") poemId: Int,
            @Assisted("poetId") poetId: Int,

            ): VersesViewModel
    }

    private val _verses = MutableStateFlow<List<Verse>>(emptyList())
    val verses: StateFlow<List<Verse>> = _verses

    private var _isBookmarked = MutableStateFlow(false)
    val isBookmarked: StateFlow<Boolean> = _isBookmarked

    init {
        fetchPoemVerses()
        fetchIsBookmarked()
    }

    private fun fetchIsBookmarked() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val isBookmarked = bookmarkRepository.isPoemBookmarked(poemId)
                _isBookmarked.value = isBookmarked
            }
        }
    }

    private fun fetchPoemVerses() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val verses = versesRepository.getPoemVerses(poemId)
                _verses.value = verses
            }
        }
    }

    fun addHighlight(verseId: Int, startIndex: Int, endIndex: Int) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {

                val highlight =
                    Highlight(verseId = verseId, startIndex = startIndex, endIndex = endIndex)
                highlightRepository.insertHighlight(highlight)
            }
        }
    }

    suspend fun getPoetName(id: Int): String {
        val res = withContext(Dispatchers.IO) {
            poetRepository.getPoetById(id).name
        }
        return res
    }

    fun onBookmarkClicked() {
        viewModelScope.launch {
            if (_isBookmarked.value) {
                removeBookmark()
                _isBookmarked.value = false
            } else {
                addBookmark()
                _isBookmarked.value = true
            }
        }
    }

    private suspend fun removeBookmark() {
        withContext(Dispatchers.IO) {
            bookmarkRepository.removeBookmark(poemId)
        }
    }

    private suspend fun addBookmark() {
        withContext(Dispatchers.IO) {
            bookmarkRepository.insertBookmark(poemId)
        }
    }

    suspend fun getPoemTitle(id: Int): String {
        val res = withContext(Dispatchers.IO) {
            poemRepository.getPoemById(id).title
        }
        return res
    }
}