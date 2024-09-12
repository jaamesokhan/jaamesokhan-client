package ir.jaamebaade.jaamebaade_client.view.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CopyAll
import androidx.compose.material.icons.outlined.Close
import androidx.compose.runtime.Composable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import ir.jaamebaade.jaamebaade_client.R
import ir.jaamebaade.jaamebaade_client.model.VerseWithHighlights

@Composable
fun PoemScreenBottomToolBar(
    selectMode: Boolean,
    modifier: Modifier,
    selectedVerses: SnapshotStateList<VerseWithHighlights>,
    onSelectModeOffToggled: () -> Unit,
) {

    val clipboardManager = LocalClipboardManager.current
    AnimatedVisibility(
        visible = selectMode,
        enter = slideInHorizontally(animationSpec = tween(durationMillis = 200)),
        exit = slideOutHorizontally(animationSpec = tween(durationMillis = 200)),
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = modifier.align(Alignment.BottomEnd),
            ) {
                AnimatedVisibility(
                    visible = selectedVerses.isNotEmpty(),
                    enter = slideInHorizontally(animationSpec = tween(durationMillis = 200)),
                    exit = slideOutHorizontally(animationSpec = tween(durationMillis = 200)),
                ) {
                    RoundButton(
                        icon = Icons.Filled.CopyAll,
                        contentDescription = stringResource(R.string.COPY_SELECTED_VERSES)
                    ) {
                        selectedVerses.sortBy { it.verse.verseOrder }
                        val textToCopy =
                            selectedVerses.joinToString(separator = "\n") { it.verse.text }
                        clipboardManager.setText(AnnotatedString(textToCopy))
                        selectedVerses.clear()
                        onSelectModeOffToggled()
                    }
                }
                RoundButton(
                    icon = Icons.Outlined.Close,
                    contentDescription = stringResource(R.string.CANCEL)
                ) {
                    onSelectModeOffToggled()
                }
            }
        }
    }
}