package com.example.jaamebaade_client.view.components

import android.media.MediaPlayer
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PauseCircleOutline
import androidx.compose.material.icons.filled.PlayCircleOutline
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.jaamebaade_client.api.response.AudioData

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AudioMenu(audioDataList: List<AudioData>) {
    var expanded by remember { mutableStateOf(false) }
    var isPlaying by remember { mutableStateOf(false) }
    var selectedAudioData by remember { mutableStateOf<AudioData?>(null) }
    val mediaPlayer = remember {
        MediaPlayer()
    }

    LaunchedEffect(mediaPlayer) {
        mediaPlayer.setOnPreparedListener {
            isPlaying = true
            it.start()
        }
        mediaPlayer.setOnCompletionListener {
            isPlaying = false
        }
    }

    Box(
        modifier = Modifier
            .combinedClickable(
                onClick = {
                    if (selectedAudioData == null) {
                        expanded = true
                    } else {
                        if (isPlaying) {
                            mediaPlayer.pause()
                        } else {
                            mediaPlayer.start()
                        }
                        isPlaying = !isPlaying
                    }
                },
                onLongClick = {
                    expanded = true
                },
                onLongClickLabel = "لیست خوانش‌ها"
            ),

        ) {
        if (isPlaying) {
            Icon(Icons.Default.PauseCircleOutline, contentDescription = "All audios")
        } else {
            Icon(Icons.Default.PlayCircleOutline, contentDescription = "All audios")
        }

        AudioListScreen(
            expanded = expanded,
            onDismiss = { expanded = false },
            audioDataList = audioDataList
        ) {
            mediaPlayer.reset()
            selectedAudioData = it
            expanded = false
            mediaPlayer.setDataSource(it.url)
            mediaPlayer.prepareAsync()
        }
    }
}