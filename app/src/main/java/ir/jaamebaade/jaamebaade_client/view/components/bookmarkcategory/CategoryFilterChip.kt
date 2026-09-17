package ir.jaamebaade.jaamebaade_client.view.components.bookmarkcategory

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import ir.jaamebaade.jaamebaade_client.ui.theme.ChipShape

@Composable
fun CategoryFilterChip(
    name: String,
    count: String,
    color: String?,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val background = if (selected) MaterialTheme.colorScheme.tertiary else MaterialTheme.colorScheme.surface
    val contentColor = if (selected) MaterialTheme.colorScheme.onTertiary else MaterialTheme.colorScheme.onSurfaceVariant
    val borderColor = if (selected) MaterialTheme.colorScheme.tertiary else MaterialTheme.colorScheme.outline

    Row(
        modifier = modifier
            .height(34.dp)
            .clip(ChipShape)
            .background(background)
            .border(1.dp, borderColor, ChipShape)
            .clickable(onClick = onClick)
            .padding(horizontal = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp),
    ) {
        color?.let { ColorDot(color = it) }
        Text(text = name, style = MaterialTheme.typography.labelLarge, color = contentColor, maxLines = 1)
        Text(
            text = count,
            style = MaterialTheme.typography.labelMedium,
            color = contentColor.copy(alpha = 0.72f)
        )
    }
}

@Composable
fun AddCategoryChip(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .height(34.dp)
            .dashedBorder(MaterialTheme.colorScheme.outline, cornerRadius = 17.dp)
            .clickable(onClick = onClick)
            .padding(horizontal = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp),
    ) {
        Icon(
            imageVector = Icons.Filled.Add,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.tertiary,
        )
        Text(text = text, style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.tertiary)
    }
}
