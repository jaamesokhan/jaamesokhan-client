package ir.jaamebaade.jaamebaade_client.ui.theme

/**
 * Poem font size as a percentage of the selected font's default body size.
 */
object PoemFontSize {
    const val MIN_PERCENT = 80
    const val MAX_PERCENT = 160
    const val STEP_PERCENT = 10
    const val DEFAULT_PERCENT = 100

    /** Number of intermediate stops for a stepped slider. */
    val sliderSteps: Int = (MAX_PERCENT - MIN_PERCENT) / STEP_PERCENT - 1

    fun coerce(percent: Int): Int {
        val snapped = Math.round((percent - MIN_PERCENT).toFloat() / STEP_PERCENT) * STEP_PERCENT + MIN_PERCENT
        return snapped.coerceIn(MIN_PERCENT, MAX_PERCENT)
    }

    /** Maps the legacy small/medium/large index to a percentage. */
    fun fromLegacyIndex(index: Int): Int = when (index) {
        0 -> 90
        2 -> 120
        else -> DEFAULT_PERCENT
    }
}
