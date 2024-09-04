package ir.jaamebaade.jaamebaade_client.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ir.jaamebaade.jaamebaade_client.model.BookmarkPoemCategoriesPoet
import ir.jaamebaade.jaamebaade_client.model.Comment
import ir.jaamebaade.jaamebaade_client.model.CommentPoemCategoriesPoet
import ir.jaamebaade.jaamebaade_client.model.Highlight
import ir.jaamebaade.jaamebaade_client.model.HighlightVersePoemCategoriesPoet
import ir.jaamebaade.jaamebaade_client.model.VersePoemCategoriesPoet
import ir.jaamebaade.jaamebaade_client.repository.BookmarkRepository
import ir.jaamebaade.jaamebaade_client.repository.CategoryRepository
import ir.jaamebaade.jaamebaade_client.repository.CommentRepository
import ir.jaamebaade.jaamebaade_client.repository.HighlightRepository
import ir.jaamebaade.jaamebaade_client.utility.SharedPrefManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    private val bookmarkRepository: BookmarkRepository,
    private val highlightRepository: HighlightRepository,
    private val commentRepository: CommentRepository,
    private val categoryRepository: CategoryRepository,
    private val sharedPrefManager: SharedPrefManager,
) : ViewModel() {
    var bookmarks by mutableStateOf<List<BookmarkPoemCategoriesPoet>>(emptyList())
        private set
    var highlights by mutableStateOf<List<HighlightVersePoemCategoriesPoet>>(emptyList())
        private set
    var comments by mutableStateOf<List<CommentPoemCategoriesPoet>>(emptyList())
        private set

    init {
        getAllBookmarks()
        getAllHighlights()
        getAllComments()
    }

    fun deleteHighlight(highlightVersePoemPoet: HighlightVersePoemCategoriesPoet) {
        viewModelScope.launch {
            highlights = highlights.toMutableList().also {
                it.remove(highlightVersePoemPoet)
            }
            deleteHighlightFromRepository(highlightVersePoemPoet.highlight)
        }
    }

    fun deleteHighlight(highlight: Highlight) {
        viewModelScope.launch {
            highlights = highlights.toMutableList().filterNot { it.highlight.id == highlight.id }
            deleteHighlightFromRepository(highlight)
        }
    }

    fun deleteComment(commentPoemCategoriesPoet: CommentPoemCategoriesPoet) {
        viewModelScope.launch {
            comments = comments.toMutableList().also {
                it.remove(commentPoemCategoriesPoet)
            }
            deleteCommentFromRepository(commentPoemCategoriesPoet.comment)
        }
    }

    fun saveMergeHighlightsToggleState(mergeHighlights: Boolean) {
        sharedPrefManager.setHighlightMergeToggleState(mergeHighlights)
    }

    fun getMergeHighlightsToggleState(): Boolean {
        return sharedPrefManager.getHighlightMergeToggleState()
    }

    private suspend fun deleteHighlightFromRepository(highlight: Highlight) {
        withContext(Dispatchers.IO) {
            highlightRepository.deleteHighlight(highlight)
        }
    }

    private suspend fun deleteCommentFromRepository(comment: Comment) {
        withContext(Dispatchers.IO) {
            commentRepository.deleteComment(comment)
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

    private fun getAllComments() {
        viewModelScope.launch {
            comments = getAllCommentsWithVersePoemPoetFromRepository()
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

    private suspend fun getAllCommentsWithVersePoemPoetFromRepository(): List<CommentPoemCategoriesPoet> {
        val res = withContext(Dispatchers.IO) {
            commentRepository.getAllCommentsWithPoemPoet().map {
                CommentPoemCategoriesPoet(
                    comment = it.comment,
                    path = VersePoemCategoriesPoet(
                        verse = null,
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
