package ir.jaamebaade.jaamebaade_client.viewmodel

import android.media.MediaPlayer
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ir.jaamebaade.jaamebaade_client.api.response.AudioData
import ir.jaamebaade.jaamebaade_client.model.PoemWithPoet
import ir.jaamebaade.jaamebaade_client.model.Status
import ir.jaamebaade.jaamebaade_client.repository.PoemRepository
import ir.jaamebaade.jaamebaade_client.repository.PoetRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class AppNavHostViewModel @Inject constructor(
    private val poemRepository: PoemRepository,
    private val poetRepository: PoetRepository,
) : ViewModel() {


    var mediaPlayer by mutableStateOf(MediaPlayer())
        private set

    var playStatus by mutableStateOf(Status.NOT_STARTED)
        private set

    var selectedAudioData by mutableStateOf<AudioData?>(null)
        private set

    var poemWithPoet by mutableStateOf<PoemWithPoet?>(null)
        private set

    var hasDownloadedAnyPoets by mutableStateOf<Boolean?>(null)
        private set


    init {
        hasDownloadedAnyPoets()
    }

    fun changePlayStatus(status: Status) {
        playStatus = status
    }

    fun setSelectedAudioDate(audioData: AudioData?) {
        selectedAudioData = audioData
    }

    suspend fun getPoemWithPoet() {
        return withContext(Dispatchers.IO) {
            poemWithPoet = poemRepository.getPoemWithPoet(poemId = selectedAudioData?.poemId!!)
        }
    }

    private fun hasDownloadedAnyPoets() {
        viewModelScope.launch {
            hasDownloadedAnyPoets = withContext(Dispatchers.IO) {
                poetRepository.getAllPoetsCount() > 0
            }
        }
    }
}