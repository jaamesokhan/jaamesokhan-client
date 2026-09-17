package ir.jaamebaade.jaamebaade_client.view.components.bookmarkcategory

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import ir.jaamebaade.jaamebaade_client.ui.theme.PillShape
import ir.jaamebaade.jaamebaade_client.ui.theme.Dimens

@Composable
fun CategoryToggleChip(
    name: String,
    color: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .height(40.dp)
            .clip(PillShape)
            .background(if (selected) MaterialTheme.colorScheme.tertiary else MaterialTheme.colorScheme.surface)
            .border(
                width = 1.dp,
                color = if (selected) MaterialTheme.colorScheme.tertiary else MaterialTheme.colorScheme.outline,
                shape = PillShape
            )
            .clickable(onClick = onClick)
            .padding(horizontal = Dimens.space16),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Text(
            text = name,
            style = MaterialTheme.typography.labelLarge,
            color = if (selected) MaterialTheme.colorScheme.onTertiary else MaterialTheme.colorScheme.onSurfaceVariant,
        )
        if (selected) {
            Icon(
                imageVector = Icons.Filled.Check,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onTertiary,
            )
        } else {
            ColorDot(color = color)
        }
    }
}
