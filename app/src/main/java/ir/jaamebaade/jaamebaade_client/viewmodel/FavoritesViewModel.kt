package ir.jaamebaade.jaamebaade_client.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ir.jaamebaade.jaamebaade_client.model.BookmarkPoemPoet
import ir.jaamebaade.jaamebaade_client.model.Highlight
import ir.jaamebaade.jaamebaade_client.model.HighlightVersePoemPoet
import ir.jaamebaade.jaamebaade_client.repository.BookmarkRepository
import ir.jaamebaade.jaamebaade_client.repository.HighlightRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    private val bookmarkRepository: BookmarkRepository,
    private val highlightRepository: HighlightRepository,
) : ViewModel() {
    var bookmarks by mutableStateOf<List<BookmarkPoemPoet>>(emptyList())
        private set
    var highlights by mutableStateOf<List<HighlightVersePoemPoet>>(emptyList())
        private set

    init {
        getAllBookmarks()
        getAllHighlights()
    }

    fun deleteHighlight(highlightVersePoemPoet: HighlightVersePoemPoet) {
        viewModelScope.launch {
            highlights = highlights.toMutableList().also {
                it.remove(highlightVersePoemPoet)
            }
            deleteHighlightFromRepository(highlightVersePoemPoet.highlight)
        }
    }

    private suspend fun deleteHighlightFromRepository(highlight: Highlight) {
        withContext(Dispatchers.IO) {
            highlightRepository.deleteHighlight(highlight)
        }
    }


    private fun getAllBookmarks() {
        viewModelScope.launch {
            bookmarks = getBookmarksFromRepository()
        }
    }

    private fun getAllHighlights() {
        viewModelScope.launch {
            highlights = getHighlightsWithVersePoemPoetFromRepository()

        }
    }

    private suspend fun getBookmarksFromRepository(): List<BookmarkPoemPoet> {
        val res = withContext(Dispatchers.IO) {
            bookmarkRepository.getAllBookmarksWithPoemAndPoet()
        }
        return res
    }

    private suspend fun getHighlightsWithVersePoemPoetFromRepository(): List<HighlightVersePoemPoet> {
        val res = withContext(Dispatchers.IO) {
            highlightRepository.getAllHighlightsWithVersePoemPoet()
        }
        return res
    }
}
