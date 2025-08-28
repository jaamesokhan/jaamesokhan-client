package ir.jaamebaade.jaamebaade_client.view.components.poem

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CopyAll
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import ir.jaamebaade.jaamebaade_client.R
import ir.jaamebaade.jaamebaade_client.model.VerseWithHighlights
import ir.jaamebaade.jaamebaade_client.view.components.base.SquareButton

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
                modifier = modifier.align(Alignment.BottomEnd).padding(4.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                SquareButton(
                    icon = Icons.Outlined.Close,
                    size = 42,
                    iconSize = 20,
                    contentDescription = stringResource(R.string.CANCEL),
                    text = null,
                    tint = MaterialTheme.colorScheme.onPrimary,
                    backgroundColor = MaterialTheme.colorScheme.primary,
                    roundedCornerShapeSize = 10,
                ) {
                    onSelectModeOffToggled()
                }
                val enabled = selectedVerses.isNotEmpty()
                Spacer(modifier = Modifier.height(12.dp))
                val backgroundColor by animateColorAsState(
                    targetValue = if (enabled) {
                        MaterialTheme.colorScheme.primary
                    } else {
                        MaterialTheme.colorScheme.outline
                    },
                )

                SquareButton(
                    icon = Icons.Filled.CopyAll,
                    size = 64,
                    iconSize = 32,
                    contentDescription = stringResource(R.string.COPY_SELECTED_VERSES),
                    text = null,
                    tint = MaterialTheme.colorScheme.onPrimary,
                    backgroundColor = backgroundColor,
                    roundedCornerShapeSize = 10,
                    enabled = enabled,
                ) {
                    selectedVerses.sortBy { it.verse.verseOrder }
                    val textToCopy =
                        selectedVerses.joinToString(separator = "\n") { it.verse.text }
                    clipboardManager.setText(AnnotatedString(textToCopy))
                    selectedVerses.clear()
                    onSelectModeOffToggled()
                }
            }
        }
    }
}