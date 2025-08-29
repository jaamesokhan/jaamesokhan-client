package ir.jaamebaade.jaamebaade_client.view.components

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.PlayCircleOutline
import androidx.compose.material.icons.outlined.Error
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import ir.jaamebaade.jaamebaade_client.model.Status
import ir.jaamebaade.jaamebaade_client.viewmodel.AppNavHostViewModel

@Composable
fun AudioButton(viewModel: AppNavHostViewModel, onClick: () -> Unit) {
    val playStatus = viewModel.playStatus
    val mediaPlayer = viewModel.mediaPlayer
    val iconSize = 24.dp

    val context = LocalContext.current
    when (playStatus) {
        Status.IN_PROGRESS -> {
            Box(
                modifier = Modifier.clickable {
                    onClick()
                },
            ) {
                Icon(
                    Icons.Default.Pause,
                    contentDescription = "pause",
                    modifier = Modifier.size(iconSize),
                )
            }
        }

        Status.STOPPED, Status.NOT_STARTED, Status.SUCCESS -> {
            Box(
                modifier = Modifier.clickable {
                    onClick()
                }
            ) {
                Icon(
                    Icons.Default.PlayArrow,
                    contentDescription = "play",
                    modifier = Modifier.size(iconSize),
                )
            }
        }

        Status.LOADING -> {
            Box(modifier = Modifier.size(iconSize)) {
                LoadingIndicator()
            }
        }

        Status.FAILED -> {
            Toast.makeText(context, "دریافت با خطا مواجه شد", Toast.LENGTH_SHORT).show()
            Icon(
                Icons.Outlined.Error,
                contentDescription = "error",
                modifier = Modifier.size(iconSize),
            )
        }

        Status.FINISHED -> {
            mediaPlayer.reset()
            viewModel.setSelectedAudioDate(null)
            Box(
                modifier = Modifier.clickable {
                    onClick()
                },

                ) {
                Icon(Icons.Default.PlayCircleOutline, contentDescription = "play")
            }
        }
    }
}