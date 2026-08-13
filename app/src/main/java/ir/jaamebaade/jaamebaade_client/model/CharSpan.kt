package ir.jaamebaade.jaamebaade_client.model

/**
 * A verse-local character span. [end] is exclusive, matching [Highlight.startIndex]/[Highlight.endIndex]
 * and [androidx.compose.ui.text.AnnotatedString.Builder.addStyle] — NOT Kotlin's inclusive IntRange.
 */
data class CharSpan(val start: Int, val end: Int)
