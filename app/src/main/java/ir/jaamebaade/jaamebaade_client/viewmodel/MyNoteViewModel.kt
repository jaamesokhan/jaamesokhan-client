package ir.jaamebaade.jaamebaade_client.viewmodel

import ir.jaamebaade.jaamebaade_client.model.Comment
import ir.jaamebaade.jaamebaade_client.model.CommentPoemCategoriesPoet
import ir.jaamebaade.jaamebaade_client.model.VersePoemCategoriesPoet
import ir.jaamebaade.jaamebaade_client.repository.CommentRepository

import android.content.Context
import android.content.Intent
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ir.jaamebaade.jaamebaade_client.repository.CategoryRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class MyNoteViewModel @Inject constructor(
    private val noteRepository: CommentRepository,
    private val categoryRepository: CategoryRepository
) : ViewModel() {
    var notes by mutableStateOf<List<CommentPoemCategoriesPoet>>(emptyList())
        private set

    init {
        getAllNotes()
    }


    fun deleteComment(commentPoemCategoriesPoet: CommentPoemCategoriesPoet) {
        viewModelScope.launch {
            notes = notes.toMutableList().also {
                it.remove(commentPoemCategoriesPoet)
            }
            deleteCommentFromRepository(commentPoemCategoriesPoet.comment)
        }
    }

    fun share(note: CommentPoemCategoriesPoet, context: Context) {
        viewModelScope.launch {

            val textToCopy = note.comment.text
            val sendIntent: Intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, textToCopy)
                type = "text/plain"
            }

            val shareIntent = Intent.createChooser(sendIntent, null)
            context.startActivity(shareIntent)
        }
    }

    private suspend fun deleteCommentFromRepository(comment: Comment) {
        withContext(Dispatchers.IO) {
            noteRepository.deleteComment(comment)
        }
    }

    private fun getAllNotes() {
        viewModelScope.launch {
            notes = getAllCommentsWithVersePoemPoetFromRepository()
        }
    }


    private suspend fun getAllCommentsWithVersePoemPoetFromRepository(): List<CommentPoemCategoriesPoet>
    {
        val res = withContext(Dispatchers.IO) {
            noteRepository.getAllCommentsWithPoemPoet().map {
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