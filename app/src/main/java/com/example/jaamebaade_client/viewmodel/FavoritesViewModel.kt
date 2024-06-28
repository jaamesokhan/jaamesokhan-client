package com.example.jaamebaade_client.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jaamebaade_client.model.BookmarkPoemPoet
import com.example.jaamebaade_client.model.Highlight
import com.example.jaamebaade_client.model.HighlightVersePoemPoet
import com.example.jaamebaade_client.repository.BookmarkRepository
import com.example.jaamebaade_client.repository.HighlightRepository
import com.example.jaamebaade_client.repository.VerseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    private val bookmarkRepository: BookmarkRepository,
    private val highlightRepository: HighlightRepository,
    private val verseRepository: VerseRepository
) : ViewModel() {
    var bookmarks by mutableStateOf<List<BookmarkPoemPoet>>(emptyList())
        private set
    var highlights by mutableStateOf<List<Highlight>>(emptyList())
        private set

    var highlightedText by mutableStateOf<List<HighlightVersePoemPoet>>(emptyList())
        private set

    init {
        getAllBookmarks()
        getAllHighlights()
    }

    private fun getHighlightedText() {
        viewModelScope.launch {
            highlightedText = getHighlightVersePoemPoetsFromRepository()
        }

    }

    private suspend fun getHighlightVersePoemPoetsFromRepository(): List<HighlightVersePoemPoet> {
        val res = mutableListOf<HighlightVersePoemPoet>()
        withContext(Dispatchers.IO) {
            for (highlight in highlights) {
                res.add(verseRepository.getHighlightVersePoemPoet(highlight.verseId))
            }
        }
        return res

    }

    private fun getAllBookmarks() {
        viewModelScope.launch {
            bookmarks = getBookmarksFromRepository()
            getHighlightedText()
        }
    }

    private fun getAllHighlights() {
        viewModelScope.launch {
            highlights = getHighlightFromRepository()

        }
    }

    private suspend fun getBookmarksFromRepository(): List<BookmarkPoemPoet> {
        val res = withContext(Dispatchers.IO) {
            bookmarkRepository.getAllBookmarksWithPoemAndPoet()
        }
        return res
    }

    private suspend fun getHighlightFromRepository(): List<Highlight> {
        val res = withContext(Dispatchers.IO) {
            highlightRepository.getAllHighlights()
        }
        return res
    }
}
