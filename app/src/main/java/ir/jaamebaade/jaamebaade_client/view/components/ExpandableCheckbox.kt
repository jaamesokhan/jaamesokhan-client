package ir.jaamebaade.jaamebaade_client.view.components

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TriStateCheckbox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.state.ToggleableState
import androidx.compose.ui.unit.dp

@Composable
fun ExpandableCheckbox(
    state: ToggleableState,
    onCheckedChange: () -> Unit,
    canExpand: Boolean = false,
    text: String,
    content: @Composable () -> Unit = {}
) {
    var isExpanded by remember { mutableStateOf(false) }
    val rotation by animateFloatAsState(if (isExpanded) 180f else 0f, label = "rotation")

    Column {
        Row(verticalAlignment = Alignment.CenterVertically) {
            TriStateCheckbox(
                state = state,
                onClick = onCheckedChange,
                modifier = Modifier.padding(end = 8.dp)
            )
            Text(text = text)
            if (canExpand) {
                IconButton(onClick = { isExpanded = !isExpanded }) {
                    Icon(
                        imageVector = Icons.Filled.ArrowDropDown,
                        contentDescription = null,
                        modifier = Modifier.graphicsLayer(rotationZ = rotation)
                    )
                }
            }
        }
        Column(
            modifier = Modifier
                .padding(start = 20.dp)
                .animateContentSize()
        ) {
            if (canExpand && isExpanded) {
                content()
            }
        }
    }
}