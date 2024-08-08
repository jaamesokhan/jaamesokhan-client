package ir.jaamebaade.jaamebaade_client.viewmodel

import AudioSyncResponse
import android.content.Context
import android.content.Intent
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ir.jaamebaade.jaamebaade_client.api.AudioApiClient
import ir.jaamebaade.jaamebaade_client.api.SyncAudioClient
import ir.jaamebaade.jaamebaade_client.api.response.AudioData
import ir.jaamebaade.jaamebaade_client.model.Highlight
import ir.jaamebaade.jaamebaade_client.model.Pair
import ir.jaamebaade.jaamebaade_client.model.Status
import ir.jaamebaade.jaamebaade_client.model.VerseWithHighlights
import ir.jaamebaade.jaamebaade_client.repository.BookmarkRepository
import ir.jaamebaade.jaamebaade_client.repository.HighlightRepository
import ir.jaamebaade.jaamebaade_client.repository.PoemRepository
import ir.jaamebaade.jaamebaade_client.repository.PoetRepository
import ir.jaamebaade.jaamebaade_client.repository.VerseRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@HiltViewModel(assistedFactory = VersesViewModel.VerseViewModelFactory::class)
class VersesViewModel @AssistedInject constructor(
    @Assisted("poemId") val poemId: Int,
    @Assisted("poetId") val poetId: Int,
    private val versesRepository: VerseRepository,
    private val highlightRepository: HighlightRepository,
    private val poetRepository: PoetRepository,
    private val poemRepository: PoemRepository,
    private val bookmarkRepository: BookmarkRepository,
    private val audioApiClient: AudioApiClient,
    private val syncAudioClient: SyncAudioClient,
) : ViewModel() {

    private val _verses = MutableStateFlow<List<VerseWithHighlights>>(emptyList())
    val verses: StateFlow<List<VerseWithHighlights>> = _verses

    private var _isBookmarked = MutableStateFlow(false)
    val isBookmarked: StateFlow<Boolean> = _isBookmarked

    private val _urls = MutableStateFlow<List<AudioData>>(emptyList())
    val urls: StateFlow<List<AudioData>> = _urls

    private val _audioSyncInfo = MutableStateFlow<AudioSyncResponse?>(null)
    val audioSyncInfo: StateFlow<AudioSyncResponse?> = _audioSyncInfo

    var syncInfoFetchStatus by mutableStateOf(Status.NOT_STARTED)
        private set

    fun share(verses: List<VerseWithHighlights>, context: Context) {
        val poemText = verses.joinToString("\n") { it.verse.text }
        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, poemText)
            type = "text/plain"
        }

        val shareIntent = Intent.createChooser(sendIntent, null)
        context.startActivity(shareIntent)
    }

    @AssistedFactory
    interface VerseViewModelFactory {
        fun create(
            @Assisted("poemId") poemId: Int,
            @Assisted("poetId") poetId: Int,
        ): VersesViewModel
    }


    init {
        fetchPoemVerses()
        fetchIsBookmarked()
    }

    fun fetchRecitationsForPoem(onSuccess: () -> Unit, onFailure: () -> Unit) {
        viewModelScope.launch {
            _urls.value = audioApiClient.getAllRecitations(
                poemId = poemId,
                onSuccess = onSuccess,
                onFailure = onFailure
            )
        }
    }

    private fun fetchIsBookmarked() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val isBookmarked = bookmarkRepository.isPoemBookmarked(poemId)
                _isBookmarked.value = isBookmarked
            }
        }
    }

    private fun fetchPoemVerses() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val verses = versesRepository.getPoemVersesWithHighlights(poemId)
                _verses.value = verses
            }
        }
    }

    fun highlight(verseId: Int, startIndex: Int, endIndex: Int) {
        viewModelScope.launch {
            val highlight = addHighlightToRepository(verseId, startIndex, endIndex)
            _verses.value = _verses.value.map {
                if (it.verse.id == verseId) {
                    it.copy(highlights = it.highlights + highlight)
                } else {
                    it
                }
            }
        }
    }

    private suspend fun addHighlightToRepository(
        verseId: Int,
        startIndex: Int,
        endIndex: Int
    ): Highlight {
        val highlight = Highlight(
            verseId = verseId,
            startIndex = startIndex,
            endIndex = endIndex
        )
        withContext(Dispatchers.IO) {
            highlightRepository.insertHighlight(highlight)
        }
        return highlight
    }

    suspend fun getPoetName(id: Int): String {
        val res = withContext(Dispatchers.IO) {
            poetRepository.getPoetById(id).name
        }
        return res
    }

    fun onBookmarkClicked() {
        viewModelScope.launch {
            if (_isBookmarked.value) {
                removeBookmark()
                _isBookmarked.value = false
            } else {
                addBookmark()
                _isBookmarked.value = true
            }
        }
    }

    private suspend fun removeBookmark() {
        withContext(Dispatchers.IO) {
            bookmarkRepository.removeBookmark(poemId)
        }
    }

    private suspend fun addBookmark() {
        withContext(Dispatchers.IO) {
            bookmarkRepository.insertBookmark(poemId)
        }
    }

    suspend fun getPoemTitle(id: Int): String {
        val res = withContext(Dispatchers.IO) {
            poemRepository.getPoemById(id).title
        }
        return res
    }

    suspend fun getCategoryIdByPoemId(poemId: Int): Int {
        return withContext(Dispatchers.IO) { poemRepository.getCategoryIdByPoemId(poemId) }
    }

    suspend fun getFirstAndLastWithCategoryId(categoryId: Int): Pair {
        return withContext(Dispatchers.IO) { poemRepository.getFirstAndLastWithCategoryId(categoryId) }
    }

    fun fetchAudioSyncInfo(syncXmlUrl: String?, onSuccess: () -> Unit, onFailure: () -> Unit) {
        if (syncXmlUrl == null) return
        viewModelScope.launch {
            syncInfoFetchStatus = Status.LOADING
            _audioSyncInfo.value = syncAudioClient.getAudioSyncInfo(
                syncXmlUrl,
                {
                    syncInfoFetchStatus = Status.SUCCESS
                    onSuccess()
                },
                {
                    syncInfoFetchStatus = Status.FAILED
                    onFailure()
                })
        }
    }


}