package ir.jaamebaade.jaamebaade_client.view.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun CardFooter(modifier: Modifier = Modifier, text: String) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(color = MaterialTheme.colorScheme.background.copy(alpha=0.4f))
    ) {
        Text(
            modifier = Modifier
                .padding(horizontal = 8.dp, vertical = 2.dp)
                .align(Alignment.CenterEnd),
            text = text,
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.labelSmall,
        )
    }
}