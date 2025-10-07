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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import ir.jaamebaade.jaamebaade_client.model.Status
import ir.jaamebaade.jaamebaade_client.viewmodel.AppNavHostViewModel

@Composable
fun AudioButton(
    viewModel: AppNavHostViewModel,
    onClick: () -> Unit,
    tint: Color = MaterialTheme.colorScheme.onBackground,
    iconSize: Dp = 24.dp,
    modifier: Modifier = Modifier
) {
    val playStatus = viewModel.playStatus

    val context = LocalContext.current
    when (playStatus) {
        Status.IN_PROGRESS -> {
            Box(
                modifier = modifier
                    .clickable {
                        onClick()
                    },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Default.Pause,
                    contentDescription = "pause",
                    modifier = Modifier.size(iconSize),
                    tint = tint
                )
            }
        }

        Status.STOPPED, Status.NOT_STARTED, Status.SUCCESS -> {
            Box(
                modifier = modifier
                    .clickable {
                        onClick()
                    },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Default.PlayArrow,
                    contentDescription = "play",
                    modifier = Modifier.size(iconSize),
                    tint = tint
                )
            }
        }

        Status.LOADING -> {
            Box(
                modifier = modifier,
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.size(iconSize),
                    color = tint,
                    strokeWidth = 2.dp
                )
            }
        }

        Status.FAILED -> {
            Toast.makeText(context, "دریافت با خطا مواجه شد", Toast.LENGTH_SHORT).show()
            Box(
                modifier = modifier,
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Outlined.Error,
                    contentDescription = "error",
                    modifier = Modifier.size(iconSize),
                    tint = tint
                )
            }
        }

        Status.FINISHED -> {
            Box(
                modifier = modifier
                    .clickable {
                        onClick()
                    },
                contentAlignment = Alignment.Center
                ) {
                Icon(
                    Icons.Default.PlayCircleOutline,
                    contentDescription = "play",
                    modifier = Modifier.size(iconSize),
                    tint = tint
                )
            }
        }
    }
}
