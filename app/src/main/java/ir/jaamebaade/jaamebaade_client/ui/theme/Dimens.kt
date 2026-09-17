package ir.jaamebaade.jaamebaade_client.ui.theme

import androidx.compose.ui.unit.dp

/**
 * Spacing scale from the Jaame Sokhan design system (design-system.json → tokens.json → spacing).
 * Not a 4/8 grid — 6, 10, 14 and 22 are as authored as 8, 16 and 24. Keep exact, do not round.
 */
object Dimens {
    val space2 = 2.dp
    val space4 = 4.dp
    val space6 = 6.dp
    val space8 = 8.dp
    val space10 = 10.dp
    val space12 = 12.dp
    val space14 = 14.dp
    val space16 = 16.dp
    val space20 = 20.dp
    val space22 = 22.dp
    val space24 = 24.dp
    val space28 = 28.dp
    val space34 = 34.dp

    /** Margin either side of the screen content. */
    val screenGutter = 24.dp

    /** Width of a floating card or the bottom-nav bar at the design's 428dp canvas. */
    val contentWidth = 396.dp

    /** Width of the centred hairline between list rows. */
    val ruleWidth = 334.dp
}
