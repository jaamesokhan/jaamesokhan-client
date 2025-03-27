package ir.jaamebaade.jaamebaade_client.view.components

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.VolumeUp
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ir.jaamebaade.jaamebaade_client.R
import ir.jaamebaade.jaamebaade_client.model.Status
import ir.jaamebaade.jaamebaade_client.viewmodel.AudioViewModel
import ir.jaamebaade.jaamebaade_client.viewmodel.PoemViewModel

@Composable
fun AudioMenu(
    viewModel: PoemViewModel,
    audioViewModel: AudioViewModel,
    modifier: Modifier = Modifier
) {
    // TODO it is not good to pass view models to inner components
    val audioUrls = viewModel.urls.collectAsState()
    var expanded by remember { mutableStateOf(false) }
    var fetchStatus by remember { mutableStateOf(Status.NOT_STARTED) }

    val mediaPlayer = audioViewModel.mediaPlayer
    val context = LocalContext.current

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
        modifier = modifier
            .size(24.dp)
    ) {
        IconButton(onClick = {
            expanded = true
            fetchStatus = Status.LOADING
            viewModel.fetchRecitationsForPoem(
                { fetchStatus = Status.SUCCESS },
                { fetchStatus = Status.FAILED })
        }) {
            Icon(
                // TODO : Auto-Mirroring is no good
                imageVector = Icons.Filled.VolumeUp,
                contentDescription = stringResource(R.string.RECITE),
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
            viewModel.fetchAudioSyncInfo(it.syncXmlUrl, {
                mediaPlayer.setDataSource(it.url)
                mediaPlayer.prepareAsync()
            }, {
                Toast.makeText(
                    context,
                    "دریافت اطلاعات همگام‌سازی صدا با خطا مواجه شد",
                    Toast.LENGTH_LONG
                ).show()
            })
        }
    }
}