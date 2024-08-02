package com.example.jaamebaade_client.view.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Audiotrack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.jaamebaade_client.model.Status
import com.example.jaamebaade_client.viewmodel.AudioViewModel
import com.example.jaamebaade_client.viewmodel.VersesViewModel

@Composable
fun AudioMenu(viewModel: VersesViewModel, audioViewModel: AudioViewModel) {
    val audioUrls = viewModel.urls.collectAsState()
    var expanded by remember { mutableStateOf(false) }
    var fetchStatus by remember { mutableStateOf(Status.NOT_STARTED) }

    val mediaPlayer = audioViewModel.mediaPlayer

    LaunchedEffect(mediaPlayer) {
        mediaPlayer.setOnPreparedListener {
            audioViewModel.changePlayStatus(Status.IN_PROGRESS)
            it.start()
        }
        mediaPlayer.setOnCompletionListener {
            audioViewModel.changePlayStatus(Status.FINISHED)
        }
    }

    Box(
        modifier = Modifier
            .size(28.dp)
    ) {
        IconButton(onClick = {
            expanded = true
            fetchStatus = Status.LOADING
            viewModel.fetchRecitationsForPoem(
                { fetchStatus = Status.SUCCESS },
                { fetchStatus = Status.FAILED })
        }) {
            Icon(
                imageVector = Icons.Default.Audiotrack, contentDescription = "Audio",
            )
        }

        AudioListScreen(
            audioDataList = audioUrls.value,
            expanded = expanded,
            onDismiss = { expanded = false },
            fetchStatus = fetchStatus
        ) {
            mediaPlayer.reset()
            audioViewModel.setSelectedAudioDate(it)
            expanded = false
            audioViewModel.changePlayStatus(Status.LOADING)
            viewModel.fetchAudioSyncInfo(it.syncXmlUrl)
            mediaPlayer.setDataSource(it.url)
            mediaPlayer.prepareAsync()
        }
    }
}