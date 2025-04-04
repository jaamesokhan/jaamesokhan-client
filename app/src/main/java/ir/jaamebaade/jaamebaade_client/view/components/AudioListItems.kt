package ir.jaamebaade.jaamebaade_client.view.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import ir.jaamebaade.jaamebaade_client.R
import ir.jaamebaade.jaamebaade_client.api.response.AudioData
import ir.jaamebaade.jaamebaade_client.model.Status
import ir.jaamebaade.jaamebaade_client.ui.theme.secondaryS30
import ir.jaamebaade.jaamebaade_client.viewmodel.AudioViewModel
import ir.jaamebaade.jaamebaade_client.viewmodel.PoemViewModel

@Composable
fun AudioListItems(
    viewModel: PoemViewModel,
    audioViewModel: AudioViewModel,
) {
    val audioDataList = viewModel.urls.collectAsState().value
    var fetchStatus by remember { mutableStateOf(Status.NOT_STARTED) }
    val mediaPlayer = audioViewModel.mediaPlayer
    val selectedAudio = audioViewModel.selectedAudioData
    val onClick: (AudioData) -> Unit =
        {
            mediaPlayer.reset()
            audioViewModel.setSelectedAudioDate(it)
            audioViewModel.changePlayStatus(Status.LOADING)
            viewModel.fetchAudioSyncInfo(it.syncXmlUrl, {
                mediaPlayer.setDataSource(it.url)
                mediaPlayer.prepareAsync()
            }, {
                // TODO show toast
            })
        }
    LaunchedEffect(Unit) {
        fetchStatus = Status.LOADING
        viewModel.fetchRecitationsForPoem(
            onSuccess = { fetchStatus = Status.SUCCESS },
            onFailure = { fetchStatus = Status.FAILED }
        )
    }
    if (audioDataList.isEmpty() && fetchStatus == Status.SUCCESS) {
        Row(
            modifier = Modifier
                .height(250.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
        ) {
            Icon(
                imageVector = Icons.Outlined.Info,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.outlineVariant,
                modifier = Modifier
                    .size(26.dp)
            )
            Text(
                text = stringResource(R.string.RECITATION_NOT_FOUND),
                color = MaterialTheme.colorScheme.outlineVariant,
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
            )
        }
    }
    when (fetchStatus) {
        Status.LOADING -> {
            Column(
                modifier = Modifier
                    .height(250.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                LoadingIndicator()
            }
        }

        Status.FAILED -> {
            // TODO : Dismiss and display toast
        }

        else -> {
            LazyColumn {
                items(items = audioDataList, key = { it.url }) { audioData ->
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.background,
                            contentColor = MaterialTheme.colorScheme.onBackground,

                            ),
                        modifier = Modifier.height(64.dp),
                        onClick = {
                            onClick(audioData)
                        },
                    )
                    {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxSize()
                        ) {
                            RadioButton(
                                modifier = Modifier.size(24.dp),
                                selected = selectedAudio == audioData,
                                onClick = { onClick(audioData) },
                                colors = RadioButtonDefaults.colors(
                                    selectedColor = MaterialTheme.colorScheme.secondaryS30,
                                    unselectedColor = MaterialTheme.colorScheme.secondaryS30,
                                )
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Box(modifier = Modifier.fillMaxSize()) {
                                Text(
                                    modifier = Modifier.align(Alignment.CenterStart),
                                    text = audioData.artist,
                                    style = MaterialTheme.typography.headlineLarge
                                )
                                if (audioDataList.last() != audioData) {
                                    HorizontalDivider(
                                        color = MaterialTheme.colorScheme.outline,
                                        modifier = Modifier.align(Alignment.BottomCenter)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
