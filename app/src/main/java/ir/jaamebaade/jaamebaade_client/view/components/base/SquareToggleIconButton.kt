package ir.jaamebaade.jaamebaade_client.view.components.base

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp


@Composable
fun SquareToggleIconButton(
    checkedIconId: Int?,
    uncheckedIconId: Int?,
    checkedImageVector: ImageVector?,
    uncheckedImageVector: ImageVector?,
    contentDescription: String?,
    checked: Boolean,
    iconSize: Int = 24,
    onClick: (Boolean) -> Unit,
) {
    assert((uncheckedIconId != null).xor(uncheckedImageVector != null))
    assert((checkedIconId != null).xor(checkedImageVector != null))

    val shape = RoundedCornerShape(8.dp)
    val interactionSource = remember { MutableInteractionSource() }

    Surface(
        modifier = Modifier
            .size(36.dp)
            .clip(shape)
            .clickable(
                interactionSource = interactionSource,
                indication =
                    ripple(
                        bounded = false,
                        radius = 40.dp
                    ),
                onClick = { onClick(!checked) }
            ),
        shape = shape,
        color = if (checked) MaterialTheme.colorScheme.secondaryContainer else Color.Transparent
    ) {
        Box(
            contentAlignment = Alignment.Center
        ) {
            if (checked) {
                checkedIconId?.let {
                    Icon(
                        painter = painterResource(it),
                        contentDescription = contentDescription,
                        tint = MaterialTheme.colorScheme.onSecondaryContainer,
                        modifier = Modifier.size(iconSize.dp)
                    )
                }
                checkedImageVector?.let {
                    Icon(
                        imageVector = it,
                        contentDescription = contentDescription,
                        tint = MaterialTheme.colorScheme.onSecondaryContainer,
                        modifier = Modifier.size(iconSize.dp)
                    )
                }
            } else {
                uncheckedIconId?.let {
                    Icon(
                        painter = painterResource(it),
                        contentDescription = contentDescription,
                        modifier = Modifier.size(iconSize.dp)
                    )
                }
                uncheckedImageVector?.let {
                    Icon(
                        imageVector = it,
                        contentDescription = contentDescription,
                        modifier = Modifier.size(iconSize.dp)
                    )
                }
            }
        }
    }
}
