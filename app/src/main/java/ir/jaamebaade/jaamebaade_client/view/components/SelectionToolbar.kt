package ir.jaamebaade.jaamebaade_client.view.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.MenuBook
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ir.jaamebaade.jaamebaade_client.R
import ir.jaamebaade.jaamebaade_client.ui.theme.HighlightColors

/**
 * Compact floating action bar shown right after a text selection ends, in place of jumping
 * straight to a bottom sheet — Highlight/Copy act immediately; Meaning opens the bottom sheet
 * (word-meaning lookups need room for a potentially long dictionary entry, a tooltip doesn't).
 * Tapping Highlight swaps the bar's content for a color swatch row instead of committing right
 * away, so the color is a one-extra-tap choice rather than a separate flow.
 */
@Composable
fun SelectionToolbar(
    onHighlight: (color: Int) -> Unit,
    onCopy: () -> Unit,
    onMeaning: () -> Unit,
) {
    var showColorPicker by remember { mutableStateOf(false) }

    Surface(
        shape = RoundedCornerShape(20.dp),
        color = MaterialTheme.colorScheme.surfaceVariant,
        contentColor = MaterialTheme.colorScheme.onSurfaceVariant,
        shadowElevation = 8.dp,
        tonalElevation = 8.dp,
    ) {
        if (showColorPicker) {
            ColorSwatchRow(
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 10.dp),
                onColorSelected = { color -> onHighlight(color.toArgb()) },
            )
        } else {
            Row(
                modifier = Modifier.padding(vertical = 6.dp, horizontal = 4.dp),
            ) {
                SelectionToolbarItem(
                    painter = painterResource(R.drawable.highlight),
                    text = stringResource(R.string.HIGHLIGHT),
                    onClick = { showColorPicker = true },
                )
                SelectionToolbarDivider()
                SelectionToolbarItem(
                    imageVector = Icons.Filled.ContentCopy,
                    text = stringResource(R.string.COPY),
                    onClick = onCopy,
                )
                SelectionToolbarDivider()
                SelectionToolbarItem(
                    imageVector = Icons.AutoMirrored.Outlined.MenuBook,
                    text = stringResource(R.string.MEANING),
                    onClick = onMeaning,
                )
            }
        }
    }
}

@Composable
private fun ColorSwatchRow(
    modifier: Modifier = Modifier,
    onColorSelected: (Color) -> Unit,
) {
    Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically) {
        HighlightColors.palette.forEach { color ->
            Box(
                modifier = Modifier
                    .padding(horizontal = 5.dp)
                    .size(28.dp)
                    .clip(CircleShape)
                    .background(color)
                    .border(
                        width = if (color == HighlightColors.Default) 2.dp else 0.dp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        shape = CircleShape,
                    )
                    .clickable { onColorSelected(color) }
            )
        }
    }
}

@Composable
private fun SelectionToolbarItem(
    text: String,
    onClick: () -> Unit,
    painter: Painter? = null,
    imageVector: ImageVector? = null,
) {
    Column(
        modifier = Modifier
            .clickable(onClick = onClick)
            .padding(horizontal = 12.dp, vertical = 4.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        if (painter != null) {
            Icon(painter = painter, contentDescription = text, modifier = Modifier.size(20.dp))
        } else if (imageVector != null) {
            Icon(imageVector = imageVector, contentDescription = text, modifier = Modifier.size(20.dp))
        }
        Text(text = text, style = MaterialTheme.typography.labelSmall)
    }
}

@Composable
private fun SelectionToolbarDivider() {
    Box(
        modifier = Modifier
            .padding(vertical = 8.dp)
            .width(1.dp)
            .height(24.dp)
            .background(MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.3f))
    )
}
