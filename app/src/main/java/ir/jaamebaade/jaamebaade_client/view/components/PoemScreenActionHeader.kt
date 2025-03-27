package ir.jaamebaade.jaamebaade_client.view.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import ir.jaamebaade.jaamebaade_client.R
import ir.jaamebaade.jaamebaade_client.constants.AppRoutes
import ir.jaamebaade.jaamebaade_client.viewmodel.AudioViewModel
import ir.jaamebaade.jaamebaade_client.viewmodel.PoemViewModel

@Composable
fun PoemScreenActionHeader(
    navController: NavController,
    poetId: Int,
    poemId: Int,
    poetName: String?,
    modifier: Modifier = Modifier,
    poemViewModel: PoemViewModel,
    showVerseNumbers: Boolean,
    selectMode: Boolean,
    audioViewModel: AudioViewModel,
    onToggleVerseNumbers: () -> Unit,
    onToggleSelectMode: () -> Unit
) {
    val isBookmarked by poemViewModel.isBookmarked.collectAsState()

    val context = LocalContext.current
    var expanded by remember { mutableStateOf(false) }

    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = modifier
                .width(190.dp)
                .padding(vertical = 8.dp, horizontal = 20.dp)
        ) {
            AudioMenu(
                viewModel = poemViewModel,
                audioViewModel = audioViewModel,
                modifier = Modifier,
            )

            IconButton(
                modifier = Modifier
                    .size(36.dp),
                colors = IconButtonDefaults.iconButtonColors(
                    containerColor = if (isBookmarked) MaterialTheme.colorScheme.secondaryContainer else Color.Transparent
                ),
                onClick = { poemViewModel.onBookmarkClicked() }) {
                if (isBookmarked) {
                    Icon(
                        painter = painterResource(R.drawable.bookmark_selected),
                        contentDescription = stringResource(R.string.UN_BOOKMARK),
                        tint = MaterialTheme.colorScheme.onSecondaryContainer,
                        modifier = Modifier.size(24.dp)

                    )
                } else {
                    Icon(
                        painter = painterResource(R.drawable.bookmark),
                        contentDescription = stringResource(R.string.BOOKMARK),
                        modifier = Modifier.size(24.dp)
                    )
                }
            }

            IconButton(
                modifier = Modifier
                    .size(36.dp),
                onClick = { navController.navigate("${AppRoutes.COMMENTS}/$poetId/$poemId") }) {
                Icon(
                    // TODO : should implement logic for changing the tint of this icon
                    painter = painterResource(R.drawable.note),
                    contentDescription = stringResource(R.string.COMMENT),
                    modifier = Modifier.size(24.dp)
                )
            }


//            IconButton(
//                modifier = Modifier
//                    .size(24.dp),
//                onClick = {
//                    poemViewModel.share(
//                        poemViewModel.verses.value,
//                        poetName,
//                        context
//                    )
//                }) {
//                Icon(
//                    imageVector = Icons.Default.Share,
//                    contentDescription = stringResource(R.string.SHARE),
//                )
//            }


            PoemScreenMoreOptionsMenu(
                expanded = expanded,
                onToggleExpanded = { expanded = !expanded },
                showVerseNumbers = showVerseNumbers,
                selectMode = selectMode,
                onToggleVerseNumbers = onToggleVerseNumbers,
                onToggleSelectMode = onToggleSelectMode,
            )
        }
    }
}