package com.example.jaamebaade_client.ui.theme

import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import com.example.jaamebaade_client.R

enum class CustomFont(val displayName: String) {
    NASTALIQ("نستعلیق"),
    VAZIRMATN("وزیر متن"),
    Serif("سریف"),
    SansSerif("سنس سریف"),
    ;

    fun getFontFamily(): FontFamily {
        return when (this) {
            NASTALIQ -> FontFamily(
                Font(R.font.irannastaliq),
            )

            VAZIRMATN -> FontFamily(
                Font(R.font.vazirmatnregular),
                Font(R.font.vazirmatnbold, weight = FontWeight.Bold),
                Font(R.font.vazirmatnextrabold, weight = FontWeight.ExtraBold),
                Font(R.font.vazirmatnlight, weight = FontWeight.Light),
                Font(R.font.vazirmatnextralight, weight = FontWeight.ExtraLight),
            )

            Serif -> FontFamily.Serif
            SansSerif -> FontFamily.SansSerif
        }
    }

    fun getFontSizes(): List<Int> {
        return when (this) {
            NASTALIQ -> listOf(20, 30, 40)
            VAZIRMATN -> listOf(12, 16, 20)
            Serif -> listOf(16, 20, 24)
            SansSerif -> listOf(16, 20, 24)
        }
    }
}