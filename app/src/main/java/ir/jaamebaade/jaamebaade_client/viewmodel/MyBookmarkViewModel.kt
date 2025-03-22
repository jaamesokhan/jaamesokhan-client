package ir.jaamebaade.jaamebaade_client.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ir.jaamebaade.jaamebaade_client.model.Bookmark
import ir.jaamebaade.jaamebaade_client.model.BookmarkPoemCategoriesPoetFirstVerse
import ir.jaamebaade.jaamebaade_client.repository.BookmarkRepository
import ir.jaamebaade.jaamebaade_client.repository.CategoryRepository
import ir.jaamebaade.jaamebaade_client.repository.PoemRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class MyBookmarkViewModel @Inject constructor(
    private val bookmarkRepository: BookmarkRepository,
    private val categoryRepository: CategoryRepository,
    private val poemRepository: PoemRepository
) : ViewModel() {
    var bookmarks by mutableStateOf<List<BookmarkPoemCategoriesPoetFirstVerse>>(emptyList())
        private set
    init {
        getAllBookmarks()
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



    private suspend fun getBookmarksFromRepository(): List<BookmarkPoemCategoriesPoetFirstVerse> {
        val res = withContext(Dispatchers.IO) {
            bookmarkRepository.getAllBookmarksWithPoemAndPoet().map {
                BookmarkPoemCategoriesPoetFirstVerse(
                    bookmark = it.bookmark,
                    poem = it.poem,
                    poet = it.poet,
                    categories = categoryRepository.getAllParentsOfCategoryId(it.poem.categoryId),
                    firstVerse = poemRepository.getPoemFirstVerse(it.poem.id)
                    )
            }
        }
        return res
    }
}