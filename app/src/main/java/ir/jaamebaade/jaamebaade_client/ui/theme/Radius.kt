package ir.jaamebaade.jaamebaade_client.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.unit.dp

/**
 * Corner-radius scale from the Jaame Sokhan design system (design-system.json → tokens.json → radius).
 */
object Radius {
    val radius4 = 4.dp
    val radius6 = 6.dp
    val radius10 = 10.dp
    val radius12 = 12.dp
    val radius15 = 15.dp
    val radius17 = 17.dp
    val radius20 = 20.dp
    val radius30 = 30.dp

    /** All buttons and FABs. */
    val button = radius15

    /** Cards and elevated surfaces. */
    val card = radius20

    /** Bottom sheets. */
    val sheet = radius20

    /** Chips and filter pills. */
    val chip = radius17

    /** The device frame in the UI kit screens. */
    val screen = radius30
}

val ButtonShape = RoundedCornerShape(Radius.button)
val CardShape = RoundedCornerShape(Radius.card)
val ChipShape = RoundedCornerShape(Radius.chip)
val SheetTopShape = RoundedCornerShape(topStart = Radius.sheet, topEnd = Radius.sheet)

/** Fully-round pill where a chip collapses to a circle. */
val PillShape = RoundedCornerShape(percent = 50)
