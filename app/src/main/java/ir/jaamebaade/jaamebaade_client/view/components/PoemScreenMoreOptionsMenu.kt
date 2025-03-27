package ir.jaamebaade.jaamebaade_client.view.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.SelectAll
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.outlined.SelectAll
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ir.jaamebaade.jaamebaade_client.R

@Composable
fun PoemScreenMoreOptionsMenu(
    expanded: Boolean,
    onToggleExpanded: () -> Unit,
    showVerseNumbers: Boolean,
    selectMode: Boolean,
    onToggleVerseNumbers: () -> Unit,
    onToggleSelectMode: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box {
        IconButton(onClick = onToggleExpanded, modifier = modifier.size(36.dp)) {
            Icon(
                Icons.Default.MoreVert, contentDescription = "More Options",
                modifier = Modifier.size(24.dp)
            )
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = onToggleExpanded,
        ) {
            DropdownMenuItem(
                text = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Icon(
                            imageVector = if (showVerseNumbers) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                            contentDescription = if (showVerseNumbers) "Hide Verse Numbers" else "Show Verse Numbers",
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = stringResource(R.string.VERSE_NUMBER),
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                },
                onClick = {
                    onToggleVerseNumbers()
                },
            )
            HorizontalDivider()
            DropdownMenuItem(
                text = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Icon(
                            imageVector = if (selectMode) Icons.Outlined.SelectAll else Icons.Filled.SelectAll,
                            contentDescription = if (selectMode) "Disable Select Mode" else "Enable Select Mode",
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = if (selectMode) stringResource(R.string.NORMAL_MODE) else stringResource(
                                R.string.SELECT_VERSES
                            ),
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                },
                onClick = {
                    onToggleSelectMode()
                    onToggleExpanded()
                },
            )
        }
    }
}
