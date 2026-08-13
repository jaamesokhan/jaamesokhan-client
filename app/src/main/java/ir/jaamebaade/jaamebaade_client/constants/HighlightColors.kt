package ir.jaamebaade.jaamebaade_client.constants

import androidx.compose.ui.graphics.Color

/**
 * Predefined highlight colors available to users
 */
object HighlightColors {
    val YELLOW = Color(0xFFFFEB3B)
    val GREEN = Color(0xFF8BC34A)
    val BLUE = Color(0xFF64B5F6)
    val PINK = Color(0xFFF48FB1)
    val ORANGE = Color(0xFFFFB74D)
    val PURPLE = Color(0xFFBA68C8)
    
    val DEFAULT = YELLOW
    
    val ALL_COLORS = listOf(
        YELLOW,
        GREEN,
        BLUE,
        PINK,
        ORANGE,
        PURPLE
    )
    
    /**
     * Convert Color to hex string for storage
     */
    fun colorToHex(color: Color): String {
        val argb = (color.value and 0xFFFFFFFFu).toLong()
        return String.format("#%08X", argb)
    }
    
    /**
     * Convert hex string to Color
     */
    fun hexToColor(hex: String): Color {
        return try {
            Color(android.graphics.Color.parseColor(hex))
        } catch (e: Exception) {
            DEFAULT
        }
    }
}
