package ir.jaamebaade.jaamebaade_client.view.components

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import ir.jaamebaade.jaamebaade_client.model.Status
import ir.jaamebaade.jaamebaade_client.viewmodel.AppNavHostViewModel

@Composable
fun AudioButton(
    viewModel: AppNavHostViewModel,
    onClick: () -> Unit,
    tint: Color = MaterialTheme.colorScheme.onBackground,
    iconSize: Dp = 20.dp,
    modifier: Modifier = Modifier
) {
    val playStatus = viewModel.playStatus
    val context = LocalContext.current

    LaunchedEffect(playStatus) {
        if (playStatus == Status.FAILED) {
            Toast.makeText(context, "دریافت با خطا مواجه شد", Toast.LENGTH_SHORT).show()
        }
    }

    val clickableStatuses = setOf(
        Status.IN_PROGRESS,
        Status.STOPPED,
        Status.NOT_STARTED,
        Status.SUCCESS,
        Status.FINISHED
    )
    val clickableModifier = if (playStatus in clickableStatuses) {
        Modifier.clickable { onClick() }
    } else {
        Modifier
    }

    val content: @Composable () -> Unit = when (playStatus) {
        Status.IN_PROGRESS -> {
            {
                Icon(
                    imageVector = Icons.Default.Pause,
                    contentDescription = "pause",
                    modifier = Modifier.size(iconSize),
                    tint = tint
                )
            }
        }

        Status.STOPPED, Status.NOT_STARTED, Status.SUCCESS -> {
            {
                Icon(
                    imageVector = Icons.Default.PlayArrow,
                    contentDescription = "play",
                    modifier = Modifier.size(iconSize),
                    tint = tint
                )
            }
        }

        Status.LOADING -> {
            {
                CircularProgressIndicator(
                    modifier = Modifier.size(iconSize),
                    color = tint,
                    strokeWidth = 2.dp
                )
            }
        }

        Status.FAILED -> {
            {
                Icon(
                    imageVector = Icons.Outlined.Error,
                    contentDescription = "error",
                    modifier = Modifier.size(iconSize),
                    tint = tint
                )
            }
        }

        Status.FINISHED -> {
            {
                Icon(
                    imageVector = Icons.Default.PlayCircleOutline,
                    contentDescription = "play",
                    modifier = Modifier.size(iconSize),
                    tint = tint
                )
            }
        }
    }

    Box(
        modifier = modifier
            .padding(horizontal = 8.dp)
            .then(clickableModifier),
        contentAlignment = Alignment.Center
    ) {
        content()
    }
}
