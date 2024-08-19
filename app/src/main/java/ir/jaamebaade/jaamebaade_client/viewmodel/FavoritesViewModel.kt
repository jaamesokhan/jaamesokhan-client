package ir.jaamebaade.jaamebaade_client.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ir.jaamebaade.jaamebaade_client.model.BookmarkPoemPoet
import ir.jaamebaade.jaamebaade_client.model.Highlight
import ir.jaamebaade.jaamebaade_client.model.HighlightVersePoemCategoriesPoet
import ir.jaamebaade.jaamebaade_client.model.VersePoemCategoriesPoet
import ir.jaamebaade.jaamebaade_client.repository.BookmarkRepository
import ir.jaamebaade.jaamebaade_client.repository.CategoryRepository
import ir.jaamebaade.jaamebaade_client.repository.HighlightRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    private val bookmarkRepository: BookmarkRepository,
    private val highlightRepository: HighlightRepository,
    private val categoryRepository: CategoryRepository,
) : ViewModel() {
    var bookmarks by mutableStateOf<List<BookmarkPoemPoet>>(emptyList())
        private set
    var highlights by mutableStateOf<List<HighlightVersePoemCategoriesPoet>>(emptyList())
        private set

    init {
        getAllBookmarks()
        getAllHighlights()
    }

    fun deleteHighlight(highlightVersePoemPoet: HighlightVersePoemCategoriesPoet) {
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

    private suspend fun getHighlightsWithVersePoemPoetFromRepository(): List<HighlightVersePoemCategoriesPoet> {
        val res = withContext(Dispatchers.IO) {
            highlightRepository.getAllHighlightsWithVersePoemPoet().map {
                HighlightVersePoemCategoriesPoet(
                    highlight = it.highlight,
                    versePath = VersePoemCategoriesPoet(
                        verse = it.verse,
                        poem = it.poem,
                        poet = it.poet,
                        categories = categoryRepository.getAllParentsOfCategoryId(it.poem.categoryId),
                    )
                )
            }
        }
        return res
    }
}
