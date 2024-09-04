package ir.jaamebaade.jaamebaade_client.view.components

import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedIconToggleButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector

@Composable
fun CustomToggleButton(
    icon: ImageVector,
    iconDescription: String? = null,
    toggleState: Boolean,
    onToggleChange: () -> Unit
) {
    OutlinedIconToggleButton(
        checked = toggleState,
        onCheckedChange = { onToggleChange() },
    ) {
        Icon(imageVector = icon, contentDescription = iconDescription)
    }
}