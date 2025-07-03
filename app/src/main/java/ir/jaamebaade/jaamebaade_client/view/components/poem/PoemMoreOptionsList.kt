package ir.jaamebaade.jaamebaade_client.view.components.poem

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
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
    Column(
        modifier = Modifier
            .padding(horizontal = 8.dp)
            .padding(bottom = 32.dp)
    ) {
        optionsList.forEach { item ->
            MenuRowItem(
                isActive = item.isActive,
                activatedIcon = item.activatedIcon,
                deactivatedIcon = item.deactivatedIcon,
                deactivatedText = item.deactivatedText,
                activatedText = item.activatedText,
                includeBottomLine = item != optionsList.last(),
            ) { }
        }
    }
}
