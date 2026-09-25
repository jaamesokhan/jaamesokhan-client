package ir.jaamebaade.jaamebaade_client.view.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Label
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ir.jaamebaade.jaamebaade_client.R
import ir.jaamebaade.jaamebaade_client.ui.theme.HighlightColors

/**
 * Replaces [SelectionToolbar] once a highlight has been committed (with the default color):
 * a color swatch row to recolor it in place, a category shortcut, and removal — all live
 * updates on the same highlight, staying open across multiple actions until dismissed (tapping
 * outside, same as [SelectionToolbar]'s own Popup dismissal).
 */
@Composable
fun HighlightAdjustmentToolbar(
    currentColor: Int,
    onColorSelected: (Int) -> Unit,
    onCategoryClick: () -> Unit,
    onRemove: () -> Unit,
) {
    Surface(
        shape = RoundedCornerShape(20.dp),
        color = MaterialTheme.colorScheme.surfaceVariant,
        contentColor = MaterialTheme.colorScheme.onSurfaceVariant,
        shadowElevation = 8.dp,
        tonalElevation = 8.dp,
    ) {
        Row(
            modifier = Modifier.padding(vertical = 6.dp, horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            HighlightColors.palette.forEach { color ->
                Box(
                    modifier = Modifier
                        .padding(horizontal = 4.dp)
                        .size(24.dp)
                        .clip(CircleShape)
                        .background(color)
                        .border(
                            width = if (color.toArgb() == currentColor) 2.dp else 0.dp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            shape = CircleShape,
                        )
                        .clickable { onColorSelected(color.toArgb()) }
                )
            }
            SelectionToolbarDivider()
            SelectionToolbarItem(
                imageVector = Icons.AutoMirrored.Outlined.Label,
                text = stringResource(R.string.CATEGORY),
                onClick = onCategoryClick,
            )
            SelectionToolbarDivider()
            SelectionToolbarItem(
                painter = painterResource(R.drawable.delete),
                text = stringResource(R.string.DELETE),
                tint = MaterialTheme.colorScheme.error,
                onClick = onRemove,
            )
        }
    }
}
