package com.example.jaamebaade_client.view.components

import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PauseCircleOutline
import androidx.compose.material.icons.filled.PlayCircleOutline
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.jaamebaade_client.api.response.AudioData
import com.example.jaamebaade_client.model.Status
import com.example.jaamebaade_client.viewmodel.VersesViewModel

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AudioMenu(viewModel: VersesViewModel) {
    val audioUrls = viewModel.urls.collectAsState()
    var expanded by remember { mutableStateOf(false) }
    var fetchStatus by remember { mutableStateOf(Status.NOT_STARTED) }
    var selectedAudioData by remember { mutableStateOf<AudioData?>(null) }
    val mediaPlayer = viewModel.mediaPlayer
    val playStatus = viewModel.playStatus
    val context = LocalContext.current

    LaunchedEffect(mediaPlayer) {
        mediaPlayer.setOnPreparedListener {
            viewModel.changePlayStatus(Status.IN_PROGRESS)
            it.start()
        }
        mediaPlayer.setOnCompletionListener {
            viewModel.changePlayStatus(Status.FINISHED)
        }
    }

    Box(
        modifier = Modifier
            .size(24.dp)
            .combinedClickable(
                onClick = {
                    if (selectedAudioData == null) {
                        expanded = true
                        fetchStatus = Status.LOADING
                        viewModel.fetchRecitationsForPoem(
                            { fetchStatus = Status.SUCCESS },
                            { fetchStatus = Status.FAILED })
                    } else {
                        if (playStatus == Status.IN_PROGRESS) {
                            mediaPlayer.pause()
                            viewModel.changePlayStatus(Status.STOPPED)

                        } else {
                            mediaPlayer.start()
                            viewModel.changePlayStatus(Status.IN_PROGRESS)
                        }
                    }
                },
                onLongClick = {
                    expanded = true
                },
                onLongClickLabel = "لیست خوانش‌ها"
            ),

        ) {
        when (playStatus) {
            Status.IN_PROGRESS -> {
                Icon(Icons.Default.PauseCircleOutline, contentDescription = "pause")
            }

            Status.STOPPED, Status.NOT_STARTED, Status.SUCCESS -> {
                Icon(Icons.Default.PlayCircleOutline, contentDescription = "play")
            }

            Status.LOADING -> {
                LoadingIndicator()
            }

            Status.FAILED -> {
                Toast.makeText(context, "دریافت با خطا مواجه شد", Toast.LENGTH_SHORT).show()
            }

            Status.FINISHED -> {
                mediaPlayer.reset()
            }
        }

        AudioListScreen(
            audioDataList = audioUrls.value,
            expanded = expanded,
            onDismiss = { expanded = false },
            fetchStatus = fetchStatus
        ) {
            mediaPlayer.reset()
            selectedAudioData = it
            expanded = false
            viewModel.changePlayStatus(Status.LOADING)
            viewModel.fetchAudioSyncInfo(it.syncXmlUrl)
            mediaPlayer.setDataSource(it.url)
            mediaPlayer.prepareAsync()
        }
    }
}