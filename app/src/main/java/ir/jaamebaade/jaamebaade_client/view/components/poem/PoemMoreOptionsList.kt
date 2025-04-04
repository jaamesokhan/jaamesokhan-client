package ir.jaamebaade.jaamebaade_client.view.components.poem

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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

data class PoemOptionItem(
    val deactivatedIcon: @Composable () -> Unit,
    val activatedIcon: @Composable () -> Unit = deactivatedIcon,
    val deactivatedText: String,
    val activatedText: String = deactivatedText,
    var isActive: Boolean = true,
    val onClick: () -> Unit
)

@Composable
fun PoemMoreOptionsList(optionsList: List<PoemOptionItem>) {
    Column(modifier = Modifier
        .padding(horizontal = 8.dp)
        .padding(bottom = 32.dp)) {
        optionsList.forEach { item ->
            Card(
                onClick = item.onClick,
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
                    if (item.isActive) {
                        item.activatedIcon()
                    } else {
                        item.deactivatedIcon()
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Box(modifier = Modifier.fillMaxSize()) {
                        Text(
                            modifier = Modifier.align(Alignment.CenterStart),
                            text = if (item.isActive) {
                                item.activatedText
                            } else {
                                item.deactivatedText
                            },
                            style = MaterialTheme.typography.headlineLarge
                        )
                        if (item != optionsList.last()) {
                            HorizontalDivider(modifier = Modifier.align(Alignment.BottomCenter))
                        }
                    }
                }
            }
        }
    }
}
