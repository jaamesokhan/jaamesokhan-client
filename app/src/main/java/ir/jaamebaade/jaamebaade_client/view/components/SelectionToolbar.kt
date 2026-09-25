package ir.jaamebaade.jaamebaade_client.view.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.MenuBook
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PlainTooltip
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TooltipBox
import androidx.compose.material3.TooltipDefaults
import androidx.compose.material3.rememberTooltipState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ir.jaamebaade.jaamebaade_client.R

/**
 * Compact floating action bar shown right after a text selection ends, in place of jumping
 * straight to a bottom sheet — Copy/Meaning act immediately; Meaning opens the bottom sheet
 * (word-meaning lookups need room for a potentially long dictionary entry, a tooltip doesn't).
 * Highlight commits immediately with the default color — the caller then swaps this bar for
 * [HighlightAdjustmentToolbar] to let the color/category/removal be refined afterward, rather
 * than gating the commit on a color choice up front.
 *
 * Icon-only, matching [ir.jaamebaade.jaamebaade_client.view.components.poem.PoemScreenActionHeader]'s
 * convention elsewhere on this screen — names surface on long-press via [SelectionToolbarItem]'s
 * tooltip rather than as permanent labels.
 */
@Composable
fun SelectionToolbar(
    onHighlight: () -> Unit,
    onCopy: () -> Unit,
    onMeaning: () -> Unit,
) {
    Surface(
        shape = RoundedCornerShape(20.dp),
        color = MaterialTheme.colorScheme.surfaceVariant,
        contentColor = MaterialTheme.colorScheme.onSurfaceVariant,
        shadowElevation = 8.dp,
        tonalElevation = 8.dp,
    ) {
        Row(
            modifier = Modifier.padding(vertical = 6.dp, horizontal = 4.dp),
        ) {
            SelectionToolbarItem(
                painter = painterResource(R.drawable.highlight),
                text = stringResource(R.string.HIGHLIGHT),
                onClick = onHighlight,
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun SelectionToolbarItem(
    text: String,
    onClick: () -> Unit,
    painter: Painter? = null,
    imageVector: ImageVector? = null,
    tint: Color = MaterialTheme.colorScheme.onSurfaceVariant,
) {
    TooltipBox(
        positionProvider = TooltipDefaults.rememberPlainTooltipPositionProvider(),
        tooltip = { PlainTooltip { Text(text) } },
        state = rememberTooltipState(),
    ) {
        Box(
            modifier = Modifier
                .clickable(onClick = onClick)
                .padding(horizontal = 14.dp, vertical = 10.dp),
            contentAlignment = Alignment.Center,
        ) {
            if (painter != null) {
                Icon(painter = painter, contentDescription = text, tint = tint, modifier = Modifier.size(22.dp))
            } else if (imageVector != null) {
                Icon(imageVector = imageVector, contentDescription = text, tint = tint, modifier = Modifier.size(22.dp))
            }
        }
    }
}

@Composable
internal fun SelectionToolbarDivider() {
    Box(
        modifier = Modifier
            .padding(vertical = 8.dp)
            .width(1.dp)
            .height(24.dp)
            .background(MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.3f))
    )
}
