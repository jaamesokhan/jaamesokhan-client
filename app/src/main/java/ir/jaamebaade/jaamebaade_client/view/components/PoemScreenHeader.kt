package ir.jaamebaade.jaamebaade_client.view.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Comment
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.outlined.Cancel
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import ir.jaamebaade.jaamebaade_client.R
import ir.jaamebaade.jaamebaade_client.constants.AppRoutes
import ir.jaamebaade.jaamebaade_client.viewmodel.AudioViewModel
import ir.jaamebaade.jaamebaade_client.viewmodel.VersesViewModel

@Composable
fun PoemScreenHeader(
    navController: NavController,
    poetId: Int,
    poemId: Int,
    modifier: Modifier = Modifier,
    versesViewModel: VersesViewModel,
    showVerseNumbers: Boolean,
    selectMode: Boolean,
    audioViewModel: AudioViewModel,
    onToggleVerseNumbers: () -> Unit,
    onToggleSelectMode: () -> Unit
) {
    val isBookmarked by versesViewModel.isBookmarked.collectAsState()

    val context = LocalContext.current
    var expanded by remember { mutableStateOf(false) }
    Column {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp, horizontal = 6.dp)
        ) {
            AudioMenu(versesViewModel, audioViewModel = audioViewModel)
            IconButton(
                modifier = Modifier.size(24.dp),
                onClick = { navController.navigate("${AppRoutes.COMMENTS}/$poetId/$poemId") }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Outlined.Comment,
                    contentDescription = stringResource(R.string.COMMENT),

                    )
            }
            IconButton(
                modifier = Modifier.size(24.dp),
                onClick = { versesViewModel.onBookmarkClicked() }) {
                if (isBookmarked) {
                    Icon(
                        imageVector = Icons.Filled.Favorite,
                        contentDescription = stringResource(R.string.UN_BOOKMARK),
                        tint = Color.Red,
                    )
                } else {
                    Icon(
                        imageVector = Icons.Outlined.FavoriteBorder,
                        contentDescription = stringResource(R.string.BOOKMARK),
                    )
                }
            }

            IconButton(
                modifier = Modifier.size(24.dp),
                onClick = { versesViewModel.share(versesViewModel.verses.value, context) }) {
                Icon(
                    imageVector = Icons.Default.Share,
                    contentDescription = stringResource(R.string.SHARE),
                )
            }

            Row {
                AnimatedVisibility(
                    visible = selectMode,
                    enter = slideInHorizontally(),
                    exit = slideOutHorizontally(),
                ) {
                    TextButton(onClick = onToggleSelectMode, modifier = Modifier.size(24.dp)) {
                        Icon(
                            imageVector = Icons.Outlined.Cancel,
                            contentDescription = stringResource(R.string.CANCEL),
                            modifier = Modifier.size(24.dp)
                        )
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
                )
            }
        }
        HorizontalDivider()
    }
}