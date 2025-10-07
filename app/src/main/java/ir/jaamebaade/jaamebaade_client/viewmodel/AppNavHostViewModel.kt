package ir.jaamebaade.jaamebaade_client.viewmodel

import android.media.MediaPlayer
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ir.jaamebaade.jaamebaade_client.api.response.AudioData
import ir.jaamebaade.jaamebaade_client.audio.AudioSessionManager
import ir.jaamebaade.jaamebaade_client.model.PoemWithPoet
import ir.jaamebaade.jaamebaade_client.model.Status
import ir.jaamebaade.jaamebaade_client.repository.PoemRepository
import ir.jaamebaade.jaamebaade_client.repository.PoetRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class AppNavHostViewModel @Inject constructor(
    private val poemRepository: PoemRepository,
    private val poetRepository: PoetRepository,
    private val audioSessionManager: AudioSessionManager,
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

    var playbackPosition by mutableStateOf(0L)
        private set

    var playbackDuration by mutableStateOf(0L)
        private set

    private var progressJob: Job? = null


    init {
        hasDownloadedAnyPoets()
        audioSessionManager.setPlaybackController(object : AudioSessionManager.PlaybackController {
            override fun onPlay() {
                viewModelScope.launch {
                    resumePlayback()
                }
            }

            override fun onPause() {
                viewModelScope.launch {
                    pausePlayback()
                }
            }

            override fun onStop() {
                viewModelScope.launch {
                    stopPlayback(clearSelection = false)
                }
            }
        })
    }

    fun changePlayStatus(status: Status) {
        playStatus = status
    }

    fun setSelectedAudioDate(audioData: AudioData?) {
        selectedAudioData = audioData
        playbackPosition = 0L
        playbackDuration = 0L
        if (audioData == null) {
            audioSessionManager.updateMetadata(null, null)
        } else {
            audioSessionManager.updateMetadata(audioData.artist, poemWithPoet?.poem?.title)
        }
    }

    suspend fun getPoemWithPoet() {
        val result = withContext(Dispatchers.IO) {
            poemRepository.getPoemWithPoet(poemId = selectedAudioData?.poemId!!)
        }
        poemWithPoet = result
        audioSessionManager.updateMetadata(poemWithPoet?.poem?.title, selectedAudioData?.artist)
    }

    fun resumePlayback() {
        if (mediaPlayer.isPlaying.not() && playStatus != Status.NOT_STARTED) {
            mediaPlayer.start()
            changePlayStatus(Status.IN_PROGRESS)
            startProgressUpdates()
            audioSessionManager.onPlay(
                mediaPlayer.currentPosition.toLong(),
                mediaPlayer.duration.toLong()
            )
        }
    }

    fun pausePlayback() {
        if (mediaPlayer.isPlaying) {
            mediaPlayer.pause()
            changePlayStatus(Status.STOPPED)
            playbackPosition = mediaPlayer.currentPosition.toLong()
            cancelProgressUpdates()
            audioSessionManager.onPause(mediaPlayer.currentPosition.toLong())
        }
    }

    fun stopPlayback(clearSelection: Boolean = false) {
        if (playStatus == Status.NOT_STARTED) {
            return
        }
        mediaPlayer.reset()
        cancelProgressUpdates()
        playbackPosition = 0L
        playbackDuration = 0L
        if (clearSelection) {
            setSelectedAudioDate(null)
            poemWithPoet = null
        }
        changePlayStatus(Status.NOT_STARTED)
        audioSessionManager.onStop()
    }

    fun onPlaybackPrepared() {
        changePlayStatus(Status.IN_PROGRESS)
        playbackDuration = mediaPlayer.duration.toLong()
        startProgressUpdates()
        audioSessionManager.onPlay(
            mediaPlayer.currentPosition.toLong(),
            mediaPlayer.duration.toLong()
        )
    }

    fun onPlaybackCompleted() {
        changePlayStatus(Status.FINISHED)
        cancelProgressUpdates()
        playbackPosition = playbackDuration
        audioSessionManager.onStop()
    }

    fun onPlaybackError() {
        cancelProgressUpdates()
        playbackPosition = 0L
        playbackDuration = 0L
        audioSessionManager.onStop()
    }

    fun seekTo(positionMillis: Int) {
        if (playStatus == Status.NOT_STARTED) {
            return
        }
        val duration = mediaPlayer.duration
        if (duration <= 0) {
            return
        }
        val clampedPosition = positionMillis.coerceIn(0, duration)
        mediaPlayer.seekTo(clampedPosition)
        playbackPosition = clampedPosition.toLong()
        if (playStatus == Status.IN_PROGRESS) {
            audioSessionManager.onPlay(
                mediaPlayer.currentPosition.toLong(),
                mediaPlayer.duration.toLong()
            )
        } else if (playStatus == Status.STOPPED) {
            audioSessionManager.onPause(mediaPlayer.currentPosition.toLong())
        }
    }

    fun seekBy(offsetMillis: Int) {
        if (playStatus == Status.NOT_STARTED) {
            return
        }
        val current = mediaPlayer.currentPosition
        val duration = mediaPlayer.duration
        if (duration <= 0) {
            return
        }
        val target = (current + offsetMillis).coerceIn(0, duration)
        seekTo(target)
    }

    private fun startProgressUpdates() {
        playbackDuration = mediaPlayer.duration.toLong()
        progressJob?.cancel()
        progressJob = viewModelScope.launch {
            while (true) {
                playbackPosition = mediaPlayer.currentPosition.toLong()
                if (playStatus != Status.IN_PROGRESS) {
                    break
                }
                delay(500)
            }
            playbackPosition = mediaPlayer.currentPosition.toLong()
        }
    }

    private fun cancelProgressUpdates() {
        progressJob?.cancel()
        progressJob = null
    }

    override fun onCleared() {
        super.onCleared()
        audioSessionManager.release()
    }

    private fun hasDownloadedAnyPoets() {
        viewModelScope.launch {
            hasDownloadedAnyPoets = withContext(Dispatchers.IO) {
                poetRepository.getAllPoetsCount() > 0
            }
        }
    }
}
