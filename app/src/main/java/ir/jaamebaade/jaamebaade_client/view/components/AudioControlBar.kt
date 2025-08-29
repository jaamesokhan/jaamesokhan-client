package ir.jaamebaade.jaamebaade_client.view.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import ir.jaamebaade.jaamebaade_client.constants.AppRoutes
import ir.jaamebaade.jaamebaade_client.model.Status
import ir.jaamebaade.jaamebaade_client.viewmodel.AppNavHostViewModel

@Composable
fun AudioControlBar(navController: NavController, viewModel: AppNavHostViewModel) {
    var showControlBar by remember { mutableStateOf(false) }
    val playStatus = viewModel.playStatus
    val poemWithPoet = viewModel.poemWithPoet

    fun onClose() {
        viewModel.mediaPlayer.reset()
        viewModel.changePlayStatus(Status.NOT_STARTED)
        showControlBar = false
    }
    LaunchedEffect(playStatus) {
        if (playStatus == Status.IN_PROGRESS || playStatus == Status.STOPPED
            || playStatus == Status.LOADING
        ) {
            showControlBar = true
            viewModel.getPoemWithPoet()
        } else if (playStatus == Status.FINISHED) {
            onClose()
        }
    }
    AnimatedVisibility(
        visible = showControlBar,
        enter = expandHorizontally(
            expandFrom = Alignment.CenterHorizontally,
            animationSpec = tween(durationMillis = 200)
        ),
        exit = shrinkHorizontally(
            shrinkTowards = Alignment.CenterHorizontally,
            animationSpec = tween(durationMillis = 200)
        ),
    ) {
        val artistName = viewModel.selectedAudioData?.artist
        Row(
            modifier = Modifier
                .background(color = MaterialTheme.colorScheme.primaryContainer)
                .padding(vertical = 1.dp)
                .clickable { navController.navigate("${AppRoutes.POEM}/${poemWithPoet?.poet?.id}/${poemWithPoet?.poem?.id}/-1") }
                .fillMaxWidth(),

            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Row(
                modifier = Modifier.padding(start = 4.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                AudioButton(viewModel = viewModel, onClick = {
                    if (viewModel.playStatus == Status.IN_PROGRESS) {
                        viewModel.changePlayStatus(Status.STOPPED)
                        viewModel.mediaPlayer.pause()
                    } else if (viewModel.playStatus == Status.STOPPED) {
                        viewModel.changePlayStatus(Status.IN_PROGRESS)
                        viewModel.mediaPlayer.start()
                    }
                })
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "$artistName - ${poemWithPoet?.poem?.title} - ${poemWithPoet?.poet?.name}",
                    style = MaterialTheme.typography.bodySmall,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.sizeIn(
                        maxWidth = 270.dp
                    ),
                    maxLines = 1
                )
            }
            IconButton(onClick = {
                onClose()
            }) {
                Icon(
                    imageVector = Icons.Default.Close, contentDescription = "close",
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}