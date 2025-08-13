package ir.jaamebaade.jaamebaade_client.viewmodel

import android.content.Context
import android.content.Intent
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
import ir.jaamebaade.jaamebaade_client.repository.VerseRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class MyBookmarkViewModel @Inject constructor(
    private val bookmarkRepository: BookmarkRepository,
    private val categoryRepository: CategoryRepository,
    private val poemRepository: PoemRepository,
    private val verseRepository: VerseRepository
) : ViewModel() {
    var bookmarks by mutableStateOf<List<BookmarkPoemCategoriesPoetFirstVerse>>(emptyList())
        private set

    init {
        getAllBookmarks()
    }


    fun deleteBookmark(bookmarkPoemCategoriesPoet: BookmarkPoemCategoriesPoetFirstVerse) {
        viewModelScope.launch {
            bookmarks = bookmarks.toMutableList().also {
                it.remove(bookmarkPoemCategoriesPoet)
            }
            deleteBookmarkFromRepository(bookmarkPoemCategoriesPoet.bookmark)
        }
    }

    fun share(bookmark: BookmarkPoemCategoriesPoetFirstVerse, context: Context) {
        viewModelScope.launch {

            val verses = withContext(Dispatchers.IO) {
                verseRepository.getPoemVersesWithHighlights(bookmark.poem.id)
            }
            val textToCopy =
                verses.joinToString("\n") { it.verse.text }.plus("\n\n${bookmark.poet.name}")
            val sendIntent: Intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, textToCopy)
                type = "text/plain"
            }

            val shareIntent = Intent.createChooser(sendIntent, null)
            context.startActivity(shareIntent)
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

    fun refreshBookmarks() {
        getAllBookmarks()
    }
}