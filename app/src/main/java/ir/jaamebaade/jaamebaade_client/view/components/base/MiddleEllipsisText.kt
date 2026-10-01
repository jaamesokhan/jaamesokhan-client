package ir.jaamebaade.jaamebaade_client.view.components.base

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Constraints

private const val ELLIPSIS = "…"

/**
 * Like [Text], but when [text] would overflow [maxLines] it collapses the middle of the string
 * into an ellipsis instead of dropping the end — useful for paths such as
 * "poet > category > poem title" where the end is usually the part worth keeping visible.
 */
@Composable
fun MiddleEllipsisText(
    text: String,
    modifier: Modifier = Modifier,
    style: TextStyle = LocalTextStyle.current,
    color: Color = Color.Unspecified,
    maxLines: Int = 2,
) {
    val textMeasurer = rememberTextMeasurer()
    val density = LocalDensity.current
    BoxWithConstraints(modifier = modifier) {
        val maxWidthPx = with(density) { maxWidth.roundToPx() }
        val displayText = remember(text, maxWidthPx, style, maxLines, textMeasurer) {
            middleTruncateToFit(text, textMeasurer, style, maxWidthPx, maxLines)
        }
        Text(
            text = displayText,
            style = style,
            color = color,
            maxLines = maxLines,
            overflow = TextOverflow.Clip,
        )
    }
}

/**
 * Binary-searches the longest "prefix…suffix" of [text] that fits within [maxWidthPx] over
 * [maxLines] lines, keeping a larger share of the budget for the suffix since it's usually the
 * more useful part of the string.
 */
private fun middleTruncateToFit(
    text: String,
    textMeasurer: TextMeasurer,
    style: TextStyle,
    maxWidthPx: Int,
    maxLines: Int,
): String {
    if (maxWidthPx <= 0) return text

    fun fits(candidate: String): Boolean {
        val result = textMeasurer.measure(
            text = candidate,
            style = style,
            maxLines = maxLines,
            overflow = TextOverflow.Clip,
            constraints = Constraints(maxWidth = maxWidthPx),
        )
        return !result.hasVisualOverflow
    }

    if (fits(text)) return text

    var lo = 0
    var hi = text.length
    var best = ELLIPSIS
    while (lo <= hi) {
        val kept = (lo + hi) / 2
        val suffixLen = (kept * 0.65f).toInt()
        val prefixLen = kept - suffixLen
        val candidate = text.take(prefixLen) + ELLIPSIS + text.takeLast(suffixLen)
        if (fits(candidate)) {
            best = candidate
            lo = kept + 1
        } else {
            hi = kept - 1
        }
    }
    return best
}
