package ir.jaamebaade.jaamebaade_client.view

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ir.jaamebaade.jaamebaade_client.R
import kotlinx.coroutines.delay
import ir.jaamebaade.jaamebaade_client.ui.theme.Dimens

// How long the splash screen waits before assuming the load is taking unusually long
// (e.g. opening a big local DB after many poets were downloaded) and surfacing a hint
// so the app doesn't look frozen.
private const val SLOW_LOAD_HINT_DELAY_MS = 3000L

@Composable
fun SplashScreen() {
    var showSlowLoadHint by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        delay(SLOW_LOAD_HINT_DELAY_MS)
        showSlowLoadHint = true
    }

    Surface(
        modifier = Modifier
            .fillMaxSize(),
        color = MaterialTheme.colorScheme.background,
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.name_logo),
                modifier = Modifier
                    .size(width = 280.dp, height = 60.dp)
                    .scale(scaleX = -1f, scaleY = 1f),
                contentDescription = "Logo",
                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary)
            )
            AnimatedVisibility(
                visible = showSlowLoadHint,
                enter = fadeIn(),
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = Dimens.space34)
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(28.dp),
                        color = MaterialTheme.colorScheme.primary,
                    )
                    Text(
                        text = stringResource(id = R.string.SLOW_LOAD_HINT),
                        modifier = Modifier.padding(top = Dimens.space12),
                        color = MaterialTheme.colorScheme.onBackground,
                    )
                }
            }
        }
    }
}
