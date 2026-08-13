package ir.jaamebaade.jaamebaade_client.ui.theme

import androidx.compose.ui.graphics.Color

/**
 * Fixed, theme-independent highlighter colors — a highlight's color is persisted (as an ARGB
 * Int, see [ir.jaamebaade.jaamebaade_client.model.Highlight.color]) and must look the same
 * regardless of light/dark theme, unlike the rest of the app's Material palette.
 */
object HighlightColors {
    val Green = Color(0xFF8FBF6B)
    val Yellow = Color(0xFFE8D06A)
    val Blue = Color(0xFF7FA8D9)
    val Pink = Color(0xFFE38FA8)
    val Purple = Color(0xFFB08FD9)

    /** Must match [ir.jaamebaade.jaamebaade_client.model.Highlight]'s column default. */
    val Default = Green

    val palette = listOf(Green, Yellow, Blue, Pink, Purple)
}
