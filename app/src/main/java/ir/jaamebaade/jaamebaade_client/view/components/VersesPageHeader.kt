package ir.jaamebaade.jaamebaade_client.view.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.automirrored.outlined.Comment
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import ir.jaamebaade.jaamebaade_client.constants.AppRoutes
import ir.jaamebaade.jaamebaade_client.viewmodel.AudioViewModel
import ir.jaamebaade.jaamebaade_client.viewmodel.VersesViewModel

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
    selectMode: Boolean,
    audioViewModel: AudioViewModel,
    onToggleVerseNumbers: () -> Unit,
    onToggleSelectMode: () -> Unit
) {
    val isBookmarked by versesViewModel.isBookmarked.collectAsState()

    val bookmarkIconColor = if (isBookmarked) Color.Red else Color.Gray
    val context = LocalContext.current
    var expanded by remember { mutableStateOf(false) }
    Column {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp, horizontal = 4.dp)
        ) {
            AudioMenu(versesViewModel, audioViewModel = audioViewModel)
            Spacer(modifier = Modifier.width(8.dp))
            IconButton(onClick = { navController.navigate("${AppRoutes.COMMENTS}/$poetId/$poemId") }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Outlined.Comment,
                    contentDescription = "یادداشت",
                    modifier = Modifier.size(28.dp)
                )
            }
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(4.dp)
                    .weight(0.6f),
            ) {

                IconButton(
                    modifier = Modifier.weight(0.1f),
                    onClick = {
                        navController.navigate(
                            "${AppRoutes.POEM}/${poetId}/${poemId - 1}/-1",
                            navOptions {
                                popUpTo("${AppRoutes.POEM}/${poetId}/${poemId}/-1") {
                                    inclusive = true
                                }
                            }
                        )
                    },
                    enabled = poemId - 1 >= minId
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                        contentDescription = "Previous"
                    )
                }

                IconButton(
                    modifier = Modifier.weight(0.1f),
                    onClick = {
                        navController.navigate(
                            "${AppRoutes.POEM}/${poetId}/${poemId + 1}/-1",
                            navOptions {
                                popUpTo("${AppRoutes.POEM}/${poetId}/${poemId}/-1") {
                                    inclusive = true
                                }
                            }
                        )
                    },
                    enabled = poemId + 1 <= maxId
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                        contentDescription = "Next"
                    )
                }

            }
            Spacer(modifier = Modifier.weight(0.1f))


            AnimatedVisibility(
                visible = selectMode,
                enter = fadeIn(),
                exit = fadeOut(),
            ) {
                TextButton(onClick = onToggleSelectMode) {
                    Text(text = "لغو")
                }
                Spacer(modifier = Modifier.weight(0.1f))
            }

            VerseScreenMoreOptionsMenu(
                expanded = expanded,
                onToggleExpanded = { expanded = !expanded },
                showVerseNumbers = showVerseNumbers,
                selectMode = selectMode,
                onToggleVerseNumbers = onToggleVerseNumbers,
                onToggleSelectMode = onToggleSelectMode,
                bookmarkIconColor = bookmarkIconColor,
                versesViewModel = versesViewModel,
                context = context
            )
        }
        HorizontalDivider()
    }
}