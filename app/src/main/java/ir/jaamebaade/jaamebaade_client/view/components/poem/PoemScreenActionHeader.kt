package ir.jaamebaade.jaamebaade_client.view.components.poem

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import ir.jaamebaade.jaamebaade_client.view.components.base.SquareToggleButton

data class ToggleButtonItem(
    val checkedIconId: Int? = null,
    val uncheckedIconId: Int? = null,
    val checkedImageVector: ImageVector? = null,
    val uncheckedImageVector: ImageVector? = null,
    val contentDescription: String? = null,
    val checked: Boolean,
    val onClick: (Boolean) -> Unit
)

@Composable
fun PoemScreenActionHeader(
    toggleButtonItems: List<ToggleButtonItem>
) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .width(190.dp)
                .padding(vertical = 16.dp, horizontal = 20.dp)
        ) {

            toggleButtonItems.forEach { item ->
                SquareToggleButton(
                    checkedIconId = item.checkedIconId,
                    uncheckedIconId = item.uncheckedIconId,
                    checkedImageVector = item.checkedImageVector,
                    uncheckedImageVector = item.uncheckedImageVector,
                    contentDescription = item.contentDescription,
                    checked = item.checked,
                    onClick = item.onClick
                )
            }
        }
    }
}