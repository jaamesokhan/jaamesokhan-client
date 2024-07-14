package com.example.jaamebaade_client.view.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.navOptions
import com.example.jaamebaade_client.viewmodel.VersesViewModel

@Composable
fun VersePageHeader(
    navController: NavController,
    poetId: Int,
    poemId: Int,
    minId: Int,
    maxId: Int,
    modifier: Modifier = Modifier,
    versesViewModel: VersesViewModel,
    showVerseNumbers: Boolean,
    onToggleVerseNumbers: () -> Unit
) {
    val isBookmarked by versesViewModel.isBookmarked.collectAsState()
    val urls by versesViewModel.urls.collectAsState()

    val bookmarkIconColor = if (isBookmarked) Color.Red else Color.Gray
    val context = LocalContext.current
    var expanded by remember { mutableStateOf(false) }
    Column {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = modifier
                .fillMaxWidth()
                .padding(4.dp, 2.dp)
        ) {
            AudioMenu(urls)
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(4.dp)
                    .weight(0.5f),
            ) {

                IconButton(
                    modifier = Modifier.weight(0.1f),
                    onClick = {
                        navController.navigate(
                            "poem/${poetId}/${poemId - 1}",
                            navOptions {
                                popUpTo("poem/{poetId}/{poemId}") {
                                    inclusive = true
                                }
                            }
                        )
                    },
                    enabled = poemId - 1 >= minId
                ) {
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowRight,
                        contentDescription = "Previous"
                    )
                }

                IconButton(
                    modifier = Modifier.weight(0.1f),
                    onClick = {
                        navController.navigate(
                            "poem/${poetId}/${poemId + 1}",
                            navOptions {
                                popUpTo("poem/{poetId}/{poemId}") {
                                    inclusive = true
                                }
                            }
                        )
                    },
                    enabled = poemId + 1 <= maxId
                ) {
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowLeft,
                        contentDescription = "Next"
                    )
                }

            }
            Spacer(modifier = Modifier.weight(0.1f))

            Box {
                IconButton(onClick = { expanded = true }) {
                    Icon(Icons.Default.MoreVert, contentDescription = "More Options")
                }
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
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
                    Divider()
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
                    Divider()
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
        Divider()
    }

}
