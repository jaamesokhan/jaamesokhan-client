package ir.jaamebaade.jaamebaade_client.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ir.jaamebaade.jaamebaade_client.model.Comment
import ir.jaamebaade.jaamebaade_client.repository.CommentRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@HiltViewModel(assistedFactory = CommentViewModel.CommentViewModelFactory::class)
class CommentViewModel @AssistedInject constructor(
    @Assisted("poemId") private val poemId: Int,
    private val commentRepository: CommentRepository,
) : ViewModel() {
    private val _comments = MutableStateFlow<List<Comment>>(emptyList())
    val comments: StateFlow<List<Comment>> = _comments

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
            _comments.value += comment
        }
    }

    fun deleteComment(comment: Comment) {
        viewModelScope.launch {
            deleteCommentFromRepository(comment)
            _comments.value -= comment
        }
    }

    private suspend fun addCommentToRepository(
        comment: Comment
    ): Comment {
        withContext(Dispatchers.IO) {
            commentRepository.insertComment(comment)
        }
        return comment
    }

    private suspend fun deleteCommentFromRepository(
        comment: Comment
    ): Comment {
        withContext(Dispatchers.IO) {
            commentRepository.deleteComment(comment)
        }
        return comment
    }

    private fun getCommentsForPoem() {
        viewModelScope.launch {
            _comments.value = getCommentsForPoemFromRepository()
        }
    }


    private suspend fun getCommentsForPoemFromRepository(): List<Comment> {
        val res = withContext(Dispatchers.IO) {
            commentRepository.getCommentsForPoem(poemId)
        }
        return res
    }
}
