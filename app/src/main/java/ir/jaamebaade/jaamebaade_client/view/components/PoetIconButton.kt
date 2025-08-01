package ir.jaamebaade.jaamebaade_client.view.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ir.jaamebaade.jaamebaade_client.R
import ir.jaamebaade.jaamebaade_client.model.Poet
import ir.jaamebaade.jaamebaade_client.view.components.base.SquareButton

@Composable
fun PoetIconButton(poet: Poet, onLongClick: () -> Unit, onClick: () -> Unit) {
    SquareButton(
        modifier = Modifier.padding(bottom = 16.dp),
        imageUrl = poet.imageUrl ?: stringResource(R.string.FALLBACK_IMAGE_URL),
        tint = Color.White,
        contentDescription = poet.name,
        textStyle = MaterialTheme.typography.headlineMedium,
        backgroundColor = Color.Transparent,
        onClick = onClick,
        onLongClick = onLongClick
    )
}