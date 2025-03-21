package ir.jaamebaade.jaamebaade_client.ui.theme

import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit

class CustomFont(
    val name: String,
    val displayName: String,
    val fontFamily: FontFamily,
    val specs: CustomFontSpecs,
)

class CustomFontSpecs(
    val label: CustomFontAttributes,
    val body: CustomFontAttributes,
    val title: CustomFontAttributes,
    val display: CustomFontAttributes,
    val headline: CustomFontAttributes,
)

class CustomFontAttributes(
    val small: CustomFontMeasures,
    val medium: CustomFontMeasures,
    val large: CustomFontMeasures,
)

class CustomFontMeasures(
    val fontWeight: FontWeight,
    val fontSize: TextUnit,
    val lineHeight: TextUnit,
    val letterSpacing: TextUnit
)