package com.example.jaamebaade_client.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.jaamebaade_client.R

val Nastaliq = FontFamily(
    Font(R.font.irannastaliq),
)
val VazirMatn = FontFamily(
    Font(R.font.vazirmatnregular),
    Font(R.font.vazirmatnbold, weight = FontWeight.Bold),
    Font(R.font.vazirmatnextrabold, weight = FontWeight.ExtraBold),
    Font(R.font.vazirmatnlight, weight = FontWeight.Light),
    Font(R.font.vazirmatnextralight, weight = FontWeight.ExtraLight),
)

// Set of Material typography styles to start with
val Typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    )
    /* Other default text styles to override
    titleLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    ),
    labelSmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    )
    */
)
var FONTS = listOf("Serif", "SansSerif", "IranNastaliq", "VazirMatn")

const val FONT_SCALE = 11
val FONT_SIZE_LIST = mutableMapOf((1 to 14), (2 to 21), (3 to 28))

fun getFontByFontFamilyName(fontFamily: String): FontFamily {
    val ff = when (fontFamily) {
        "Serif" -> FontFamily.Serif
        "SansSerif" -> FontFamily.SansSerif
        "IranNastaliq" -> Nastaliq
        "VazirMatn" -> VazirMatn
        else -> FontFamily.Default
    }
    return ff
}