package ir.jaamebaade.jaamebaade_client.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ir.jaamebaade.jaamebaade_client.model.Bookmark
import ir.jaamebaade.jaamebaade_client.model.BookmarkPoemCategoriesPoet
import ir.jaamebaade.jaamebaade_client.repository.BookmarkRepository
import ir.jaamebaade.jaamebaade_client.repository.CategoryRepository
import ir.jaamebaade.jaamebaade_client.utility.SharedPrefManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class MyBookmarkViewModel @Inject constructor(
    private val bookmarkRepository: BookmarkRepository,
    private val categoryRepository: CategoryRepository,
    private val sharedPrefManager: SharedPrefManager,
) : ViewModel() {
    var bookmarks by mutableStateOf<List<BookmarkPoemCategoriesPoet>>(emptyList())
        private set
    private val _showAppIntro = MutableStateFlow(true)
    val showAppIntro = _showAppIntro.asStateFlow()

    init {
        getAllBookmarks()
        getShowAppIntroState()
    }


    fun setShowAppIntroState(showIntro: Boolean) {
        viewModelScope.launch {
            sharedPrefManager.setShowAppIntroHighlight(showIntro)
            _showAppIntro.value = showIntro
        }
    }

    private fun getShowAppIntroState() {
        viewModelScope.launch {
            _showAppIntro.value = sharedPrefManager.getShowAppIntroHighlight()
        }
    }


    private suspend fun deleteBookmarkFromRepository(bookmark: Bookmark) {
        withContext(Dispatchers.IO) {
            bookmarkRepository.deleteBookmark(bookmark)
        }
    }

    private fun getAllBookmarks() {
        viewModelScope.launch {
            bookmarks = getBookmarksFromRepository()
        }
    }


    private suspend fun getBookmarksFromRepository(): List<BookmarkPoemCategoriesPoet> {
        val res = withContext(Dispatchers.IO) {
            bookmarkRepository.getAllBookmarksWithPoemAndPoet().map {
                BookmarkPoemCategoriesPoet(
                    bookmark = it.bookmark,
                    poem = it.poem,
                    poet = it.poet,
                    categories = categoryRepository.getAllParentsOfCategoryId(it.poem.categoryId),
                )
            }
        }
        return res
    }
}