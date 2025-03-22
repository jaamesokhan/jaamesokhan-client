package ir.jaamebaade.jaamebaade_client.view.components.toast

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.unit.dp
import ir.jaamebaade.jaamebaade_client.viewmodel.ToastManager
import kotlinx.coroutines.delay


@Composable
fun ToastMessage(
    modifier: Modifier = Modifier,
) {
    val toastMessageId by ToastManager.toastMessageId.collectAsState()
    val showMessage by ToastManager.showMessage.collectAsState()
    val type by ToastManager.type.collectAsState()

    // Use a separate state to control visibility for animation
    var visible by remember { mutableStateOf(false) }

    // Update visible state when showMessage changes
    LaunchedEffect(showMessage) {
        if (showMessage) {
            visible = true
            delay(2000)
            // First change visibility to trigger animation
            visible = false
            // Then after animation completes, update the actual manager state
            delay(300) // animation duration
            ToastManager.hideToast()
        }
    }

    val backgroundColor = when (type) {
        ToastType.SUCCESS -> MaterialTheme.colorScheme.secondary
        ToastType.ERROR -> MaterialTheme.colorScheme.error
        ToastType.WARNING -> MaterialTheme.colorScheme.errorContainer
    }

    val color = when (type) {
        ToastType.SUCCESS -> MaterialTheme.colorScheme.onSecondary
        ToastType.ERROR -> MaterialTheme.colorScheme.onError
        ToastType.WARNING -> MaterialTheme.colorScheme.onErrorContainer
    }

    val icon = when (type) {
        ToastType.SUCCESS -> Icons.Default.Check
        ToastType.ERROR -> Icons.Outlined.Info
        ToastType.WARNING -> Icons.Outlined.Info
    }

    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(animationSpec = tween(300)) +
                slideInHorizontally(animationSpec = tween(300)) { it },
        exit = fadeOut(animationSpec = tween(300)) +
                slideOutHorizontally(animationSpec = tween(300)) { it }
    ) {
        Row(
            modifier = Modifier.padding(16.dp)
        ) {

            Row(
                modifier = modifier
                    .background(color = backgroundColor, shape = RoundedCornerShape(15.dp))
                    .padding(horizontal = 12.dp, vertical = 8.dp)
                    .fillMaxWidth()
                    .height(80.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = type.name,
                    tint = color,
                    modifier = Modifier.padding(end = 8.dp)
                )
                Text(
                    text = stringResource(toastMessageId),
                    style = MaterialTheme.typography.headlineLarge,
                    color = color
                )
            }
        }
    }
}
