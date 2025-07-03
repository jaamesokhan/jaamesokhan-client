package ir.jaamebaade.jaamebaade_client.view.components.poem

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun MenuRowItem(isActive: Boolean = true,
                activatedIcon: @Composable () -> Unit,
                deactivatedIcon: @Composable () -> Unit = activatedIcon,
                deactivatedText: String,
                activatedText: String = deactivatedText,
                includeBottomLine: Boolean = true,
                textColor: Color? = null,
                onClick: () -> Unit) {
    Card(
        onClick = onClick,
        modifier = Modifier
            .height(64.dp)
            .padding(horizontal = 26.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(horizontal = 8.dp)
        ) {
            if (isActive) {
                activatedIcon()
            } else {
                deactivatedIcon()
            }
            Spacer(modifier = Modifier.width(10.dp))
            Box(modifier = Modifier.fillMaxSize()) {
                Text(
                    modifier = Modifier.align(Alignment.CenterStart),
                    text = if (isActive) {
                        activatedText
                    } else {
                        deactivatedText
                    },
                    color = textColor ?: MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.headlineLarge
                )
                if (includeBottomLine) {
                    HorizontalDivider(
                        color = MaterialTheme.colorScheme.outline,
                        modifier = Modifier.align(Alignment.BottomCenter)
                    )
                }
            }
        }
    }
}