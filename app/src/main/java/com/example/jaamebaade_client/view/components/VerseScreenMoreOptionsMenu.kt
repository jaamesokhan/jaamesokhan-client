package com.example.jaamebaade_client.view.components

import android.content.Context
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.SelectAll
import androidx.compose.material.icons.filled.Share
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.jaamebaade_client.viewmodel.VersesViewModel

@Composable
fun VerseScreenMoreOptionsMenu(
    expanded: Boolean,
    onToggleExpanded: () -> Unit,
    showVerseNumbers: Boolean,
    selectMode: Boolean,
    onToggleVerseNumbers: () -> Unit,
    onToggleSelectMode: () -> Unit,
    bookmarkIconColor: Color,
    versesViewModel: VersesViewModel,
    context: Context
) {
    Box {
        IconButton(onClick = onToggleExpanded) {
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
                            text = if (showVerseNumbers) "مخفی‌سازی شماره بیت" else "نمایش شماره بیت",
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
            HorizontalDivider()
            DropdownMenuItem(text = {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(
                        imageVector = Icons.Default.Favorite,
                        contentDescription = "Bookmark",
                        tint = bookmarkIconColor,
                        modifier = Modifier.padding(end = 8.dp)
                    )
                    Text(
                        text = "علاقه‌مندی",
                        style = MaterialTheme.typography.headlineSmall
                    )
                }
            }, onClick = {
                versesViewModel.onBookmarkClicked()
            })
            HorizontalDivider()
            DropdownMenuItem(text = {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(
                        imageVector = Icons.Default.Share,
                        contentDescription = "Share",
                        modifier = Modifier.padding(end = 8.dp)
                    )
                    Text(
                        text = "اشتراک‌گذاری",
                        style = MaterialTheme.typography.headlineSmall
                    )
                }
            }, onClick = {
                versesViewModel.share(versesViewModel.verses.value, context)
            })
        }
    }
}
