package ir.jaamebaade.jaamebaade_client.view.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
fun VerseScreenMoreOptionsMenu(
    expanded: Boolean,
    onToggleExpanded: () -> Unit,
    showVerseNumbers: Boolean,
    selectMode: Boolean,
    onToggleVerseNumbers: () -> Unit,
    onToggleSelectMode: () -> Unit,
) {
    Box {
        IconButton(onClick = onToggleExpanded, modifier = Modifier.size(28.dp)) {
            Icon(Icons.Default.MoreVert, contentDescription = "More Options")
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
                            modifier = Modifier.padding(end = 8.dp)
                        )
                        Text(
                            text = stringResource(R.string.VERSE_NUMBER),
                            style = MaterialTheme.typography.headlineSmall
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
                            modifier = Modifier.padding(end = 8.dp)
                        )
                        Text(
                            text = if (selectMode) "حالت عادی" else "حالت انتخاب مصرع",
                            style = MaterialTheme.typography.headlineSmall
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
