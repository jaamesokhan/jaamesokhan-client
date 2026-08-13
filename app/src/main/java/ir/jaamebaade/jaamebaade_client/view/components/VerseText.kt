package ir.jaamebaade.jaamebaade_client.view.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp

/**
 * Read-only verse rendering, replacing the old per-verse BasicTextField. Compose's own text
 * selection can't span multiple TextField/Text composables, so instead of relying on it, this
 * just reports its layout to [selectionController], which hit-tests drag positions across all
 * currently-visible verses to build a selection that can cross verse (and couplet) boundaries.
 */
@Composable
fun VerseText(
    verseId: Long,
    index: Int,
    annotatedString: AnnotatedString,
    style: TextStyle,
    selectionController: VerseSelectionController,
) {
    var layoutResult by remember { mutableStateOf<TextLayoutResult?>(null) }
    var coordinates by remember { mutableStateOf<LayoutCoordinates?>(null) }

    DisposableEffect(verseId) {
        onDispose { selectionController.unregisterLayout(verseId) }
    }

    LaunchedEffect(layoutResult, coordinates, annotatedString.text, index) {
        val currentLayoutResult = layoutResult
        val currentCoordinates = coordinates
        if (currentLayoutResult != null && currentCoordinates != null && currentCoordinates.isAttached) {
            selectionController.registerLayout(
                verseId = verseId,
                index = index,
                text = annotatedString.text,
                layoutResult = currentLayoutResult,
                coordinates = currentCoordinates,
            )
        }
    }

    Text(
        text = annotatedString,
        style = style,
        modifier = Modifier
            .fillMaxWidth()
            .onGloballyPositioned { coordinates = it }
            .padding(4.dp),
        onTextLayout = { layoutResult = it },
    )
}
