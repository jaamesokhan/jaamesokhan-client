package ir.jaamebaade.jaamebaade_client.view.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ir.jaamebaade.jaamebaade_client.R

@Composable
fun RandomPoemBox(
) {
    Box(
        modifier = Modifier.padding(vertical = 32.dp)
    ) {

        Row(
            modifier = Modifier
                .clip(RoundedCornerShape(25.dp))
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.tertiaryContainer,
                            MaterialTheme.colorScheme.tertiary,
                        )
                    )
                )
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            Image(
                painter = painterResource(id = R.drawable.random_poem),
                contentDescription = stringResource(R.string.RANDOM_POEM),
                modifier = Modifier.size(height = 228.dp, width = 110.dp)
            )
        }
    }
}