package ir.jaamebaade.jaamebaade_client.viewmodel

import android.content.Context
import android.content.Intent
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import ir.jaamebaade.jaamebaade_client.model.Comment
import ir.jaamebaade.jaamebaade_client.repository.CommentRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@HiltViewModel(assistedFactory = CommentViewModel.CommentViewModelFactory::class)
class CommentViewModel @AssistedInject constructor(
    @Assisted("poemId") private val poemId: Int,
    private val commentRepository: CommentRepository,
) : ViewModel() {
    var comments by mutableStateOf<List<Comment>>(emptyList())
        private set

    init {
        getCommentsForPoem()
    }

    @AssistedFactory
    interface CommentViewModelFactory {
        fun create(
            @Assisted("poemId") poetId: Int,
        ): CommentViewModel
    }

    fun addComment(poemId: Int, text: String) {
        viewModelScope.launch {
            val comment = Comment(poemId = poemId, text = text)
            addCommentToRepository(comment)
        }
    }

    fun shareComment(comment: Comment, context: Context) {
        viewModelScope.launch {
            // TODO add the poem path to this
            val textToShare = comment.text
            val sendIntent: Intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, textToShare)
                type = "text/plain"
            }

            val shareIntent = Intent.createChooser(sendIntent, null)
            context.startActivity(shareIntent)
        }
    }

    fun deleteComment(comment: Comment) {
        viewModelScope.launch {
            deleteCommentFromRepository(comment)
        }
    }

    private suspend fun addCommentToRepository(
        comment: Comment
    ): Comment {
        withContext(Dispatchers.IO) {
            val commentId = commentRepository.insertComment(comment)
            comment.also {
                it.id = commentId.toInt()
            }
            comments = comments.toMutableList().also {
                it.add(comment)
            }
        }
        return comment
    }

    private suspend fun deleteCommentFromRepository(
        comment: Comment
    ): Comment {
        withContext(Dispatchers.IO) {
            commentRepository.deleteComment(comment)
            comments = comments.toMutableList().also {
                it.remove(comment)
            }
        }
        return comment
    }

    private fun getCommentsForPoem() {
        viewModelScope.launch {
            comments = getCommentsForPoemFromRepository()
        }
    }


    private suspend fun getCommentsForPoemFromRepository(): List<Comment> {
        val res = withContext(Dispatchers.IO) {
            commentRepository.getCommentsForPoem(poemId)
        }
        return res
    }
}
