package ir.jaamebaade.jaamebaade_client.view.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import ir.jaamebaade.jaamebaade_client.model.CharSpan
import ir.jaamebaade.jaamebaade_client.model.Highlight
import ir.jaamebaade.jaamebaade_client.model.Verse
import ir.jaamebaade.jaamebaade_client.utility.toPersianNumber


@Composable
fun VerseItem(
    modifier: Modifier = Modifier,
    verse: Verse,
    index: Int,
    showVerseNumber: Boolean,
    selectMode: Boolean,
    isSelected: Boolean,
    onClick: () -> Unit,
    highlights: List<Highlight>,
    verseStyle: SpanStyle,
    selectionController: VerseSelectionController,
    pendingSelectionSpan: CharSpan? = null,
    ) {
    val paddingFromStart = 14.dp
    val liveSelectionColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.35f)
    // While a drag is active, the controller tracks the live in-progress span. Once it ends,
    // that clears — but the toolbar/bottom sheet for the confirmed selection is still open, so
    // fall back to the resolved pending span (passed down by the caller) to keep it visible.
    val liveSelection = selectionController.selection[verse.id] ?: pendingSelectionSpan

    val annotatedString = buildAnnotatedString {
        withStyle(verseStyle) {
            append(verse.text)

            highlights.forEach {
                addStyle(
                    style = SpanStyle(
                        background = Color(it.color),
                        fontWeight = FontWeight.Bold
                    ),
                    start = it.startIndex,
                    end = it.endIndex,
                )
            }

            liveSelection?.let {
                addStyle(
                    style = SpanStyle(background = liveSelectionColor),
                    start = it.start,
                    end = it.end,
                )
            }
        }
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(0.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,


        ) {
        AnimatedVisibility(
            visible = selectMode,
        ) {
            Checkbox(checked = isSelected, onCheckedChange = { onClick() })
        }

        AnimatedVisibility(visible = showVerseNumber) {
            if (index % 2 == 0) {
                Text(
                    text = (index / 2 + 1).toPersianNumber(),
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(start = paddingFromStart),
                    color = MaterialTheme.colorScheme.onBackground
                )
            } else {
                Spacer(modifier = Modifier.width(paddingFromStart))
            }
        }
        VerseText(
            verseId = verse.id,
            index = index,
            annotatedString = annotatedString,
            style = MaterialTheme.typography.bodyMedium.copy(
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onBackground
            ),
            selectionController = selectionController,
        )
    }
    if (verse.position % 2 == 1)
        Spacer(modifier = Modifier.height(20.dp))

}

