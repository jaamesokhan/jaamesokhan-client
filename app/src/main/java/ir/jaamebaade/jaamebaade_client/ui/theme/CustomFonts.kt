package ir.jaamebaade.jaamebaade_client.ui.theme

import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import ir.jaamebaade.jaamebaade_client.R

// TODO : all other font sizes except for Dana need to change
object CustomFonts {
    val Nastaliq = CustomFont(
        name = "Nastaliq",
        displayName = "نستعلیق",
        fontFamily = FontFamily(
            Font(R.font.irannastaliq)
        ),
        specs = CustomFontSpecs(
            label = CustomFontAttributes(
                small = CustomFontMeasures(
                    fontSize = 10.sp,
                    lineHeight = 12.sp,
                    letterSpacing = 0.5.sp,
                    fontWeight = FontWeight.Normal,
                ),
                medium = CustomFontMeasures(
                    fontSize = 12.sp,
                    lineHeight = 14.sp,
                    letterSpacing = 0.5.sp,
                    fontWeight = FontWeight.Normal,
                ),
                large = CustomFontMeasures(
                    fontSize = 14.sp,
                    lineHeight = 16.sp,
                    letterSpacing = 0.5.sp,
                    fontWeight = FontWeight.Normal,
                ),
            ),
            body = CustomFontAttributes(
                small = CustomFontMeasures(
                    fontSize = 10.sp,
                    lineHeight = 12.sp,
                    letterSpacing = 0.5.sp,
                    fontWeight = FontWeight.Normal,
                ),
                medium = CustomFontMeasures(
                    fontSize = 12.sp,
                    lineHeight = 14.sp,
                    letterSpacing = 0.5.sp,
                    fontWeight = FontWeight.Normal,
                ),
                large = CustomFontMeasures(
                    fontSize = 14.sp,
                    lineHeight = 16.sp,
                    letterSpacing = 0.5.sp,
                    fontWeight = FontWeight.Normal,
                ),
            ),
            title = CustomFontAttributes(
                small = CustomFontMeasures(
                    fontSize = 10.sp,
                    lineHeight = 12.sp,
                    letterSpacing = 0.5.sp,
                    fontWeight = FontWeight.Normal,
                ),
                medium = CustomFontMeasures(
                    fontSize = 12.sp,
                    lineHeight = 14.sp,
                    letterSpacing = 0.5.sp,
                    fontWeight = FontWeight.Normal,
                ),
                large = CustomFontMeasures(
                    fontSize = 14.sp,
                    lineHeight = 16.sp,
                    letterSpacing = 0.5.sp,
                    fontWeight = FontWeight.Normal,
                ),
            ),
            display = CustomFontAttributes(
                small = CustomFontMeasures(
                    fontSize = 10.sp,
                    lineHeight = 12.sp,
                    letterSpacing = 0.5.sp,
                    fontWeight = FontWeight.Normal,
                ),
                medium = CustomFontMeasures(
                    fontSize = 12.sp,
                    lineHeight = 14.sp,
                    letterSpacing = 0.5.sp,
                    fontWeight = FontWeight.Normal,
                ),
                large = CustomFontMeasures(
                    fontSize = 14.sp,
                    lineHeight = 16.sp,
                    letterSpacing = 0.5.sp,
                    fontWeight = FontWeight.Normal,
                ),
            ),
            headline = CustomFontAttributes(
                small = CustomFontMeasures(
                    fontSize = 10.sp,
                    lineHeight = 12.sp,
                    letterSpacing = 0.5.sp,
                    fontWeight = FontWeight.Normal,
                ),
                medium = CustomFontMeasures(
                    fontSize = 12.sp,
                    lineHeight = 14.sp,
                    letterSpacing = 0.5.sp,
                    fontWeight = FontWeight.Normal,
                ),
                large = CustomFontMeasures(
                    fontSize = 14.sp,
                    lineHeight = 16.sp,
                    letterSpacing = 0.5.sp,
                    fontWeight = FontWeight.Normal,
                ),
            )
        )
    )

    val Vazirmatn = CustomFont(
        name = "Vazirmatn",
        displayName = "وزیر متن",
        fontFamily = FontFamily(
            Font(R.font.vazirmatn_regular),
            Font(R.font.vazirmatn_bold, weight = FontWeight.Companion.Bold),
            Font(R.font.vazirmatn_extra_bold, weight = FontWeight.Companion.ExtraBold),
            Font(R.font.vazirmatn_light, weight = FontWeight.Companion.Light),
            Font(R.font.vazirmatn_extra_light, weight = FontWeight.Companion.ExtraLight)
        ),
        specs = CustomFontSpecs(
            label = CustomFontAttributes(
                small = CustomFontMeasures(
                    fontSize = 10.sp,
                    lineHeight = 12.sp,
                    letterSpacing = 0.5.sp,
                    fontWeight = FontWeight.Normal,
                ),
                medium = CustomFontMeasures(
                    fontSize = 12.sp,
                    lineHeight = 14.sp,
                    letterSpacing = 0.5.sp,
                    fontWeight = FontWeight.Normal,
                ),
                large = CustomFontMeasures(
                    fontSize = 14.sp,
                    lineHeight = 16.sp,
                    letterSpacing = 0.5.sp,
                    fontWeight = FontWeight.Normal,
                ),
            ),
            body = CustomFontAttributes(
                small = CustomFontMeasures(
                    fontSize = 12.sp,
                    lineHeight = 14.sp,
                    letterSpacing = 0.25.sp,
                    fontWeight = FontWeight.Normal,
                ),
                medium = CustomFontMeasures(
                    fontSize = 14.sp,
                    lineHeight = 16.sp,
                    letterSpacing = 0.25.sp,
                    fontWeight = FontWeight.Normal,
                ),
                large = CustomFontMeasures(
                    fontSize = 16.sp,
                    lineHeight = 18.sp,
                    letterSpacing = 0.5.sp,
                    fontWeight = FontWeight.Normal,
                ),
            ),
            title = CustomFontAttributes(
                small = CustomFontMeasures(
                    fontSize = 14.sp,
                    lineHeight = 16.sp,
                    letterSpacing = 0.sp,
                    fontWeight = FontWeight.Normal,
                ),
                medium = CustomFontMeasures(
                    fontSize = 16.sp,
                    lineHeight = 18.sp,
                    letterSpacing = 0.sp,
                    fontWeight = FontWeight.Normal,
                ),
                large = CustomFontMeasures(
                    fontSize = 18.sp,
                    lineHeight = 20.sp,
                    letterSpacing = 0.sp,
                    fontWeight = FontWeight.Normal,
                ),
            ),
            display = CustomFontAttributes(
                small = CustomFontMeasures(
                    fontSize = 16.sp,
                    lineHeight = 18.sp,
                    letterSpacing = 0.sp,
                    fontWeight = FontWeight.Normal,
                ),
                medium = CustomFontMeasures(
                    fontSize = 18.sp,
                    lineHeight = 20.sp,
                    letterSpacing = 0.sp,
                    fontWeight = FontWeight.Normal,
                ),
                large = CustomFontMeasures(
                    fontSize = 20.sp,
                    lineHeight = 22.sp,
                    letterSpacing = 0.sp,
                    fontWeight = FontWeight.Normal,
                ),
            ),
            headline = CustomFontAttributes(
                small = CustomFontMeasures(
                    fontSize = 14.sp,
                    lineHeight = 16.sp,
                    letterSpacing = 0.sp,
                    fontWeight = FontWeight.Normal,
                ),
                medium = CustomFontMeasures(
                    fontSize = 16.sp,
                    lineHeight = 18.sp,
                    letterSpacing = 0.sp,
                    fontWeight = FontWeight.Normal,
                ),
                large = CustomFontMeasures(
                    fontSize = 18.sp,
                    lineHeight = 20.sp,
                    letterSpacing = 0.sp,
                    fontWeight = FontWeight.Normal,
                ),
            )
        )
    )

    val Dana = CustomFont(
        name = "Dana",
        displayName = "دانا",
        fontFamily = FontFamily(
            Font(R.font.dana_regular),
            Font(R.font.dana_ultra_light, weight = FontWeight.ExtraLight),
            Font(R.font.dana_light, weight = FontWeight.Light),
            Font(R.font.dana_medium, weight = FontWeight.Medium),
            Font(R.font.dana_demi_bold, weight = FontWeight.SemiBold),
            Font(R.font.dana_bold, weight = FontWeight.Bold),
            Font(R.font.dana_extra_bold, weight = FontWeight.ExtraBold)
        ),
        specs = CustomFontSpecs(
            label = CustomFontAttributes(
                small = CustomFontMeasures(
                    fontSize = 14.sp,
                    lineHeight = 12.sp,
                    letterSpacing = 0.sp,
                    fontWeight = FontWeight.ExtraBold,
                ),
                medium = CustomFontMeasures(
                    fontSize = 16.sp,
                    lineHeight = 14.sp,
                    letterSpacing = 0.sp,
                    fontWeight = FontWeight.ExtraBold,
                ),
                large = CustomFontMeasures(
                    fontSize = 18.sp,
                    lineHeight = 16.sp,
                    letterSpacing = 0.sp,
                    fontWeight = FontWeight.ExtraBold,
                ),
            ),
            body = CustomFontAttributes(
                small = CustomFontMeasures(
                    fontSize = 12.sp,
                    lineHeight = 20.0.sp,
                    letterSpacing = 0.sp,
                    fontWeight = FontWeight.Medium,
                ),
                medium = CustomFontMeasures(
                    fontSize = 14.sp,
                    lineHeight = 20.0.sp,
                    letterSpacing = 0.sp,
                    fontWeight = FontWeight.Medium,
                ),
                large = CustomFontMeasures(
                    fontSize = 16.sp,
                    lineHeight = 20.0.sp,
                    letterSpacing = 0.sp,
                    fontWeight = FontWeight.Medium,
                ),
            ),
            title = CustomFontAttributes(
                small = CustomFontMeasures(
                    fontSize = 14.sp,
                    lineHeight = 16.sp,
                    letterSpacing = 0.sp,
                    fontWeight = FontWeight.Medium,
                ),
                medium = CustomFontMeasures(
                    fontSize = 16.sp,
                    lineHeight = 18.sp,
                    letterSpacing = 0.sp,
                    fontWeight = FontWeight.Medium,
                ),
                large = CustomFontMeasures(
                    fontSize = 18.sp,
                    lineHeight = 20.sp,
                    letterSpacing = 0.sp,
                    fontWeight = FontWeight.Medium,
                ),
            ),
            display = CustomFontAttributes(
                small = CustomFontMeasures(
                    fontSize = 18.sp,
                    lineHeight = 18.sp,
                    letterSpacing = 0.sp,
                    fontWeight = FontWeight.Medium,
                ),
                medium = CustomFontMeasures(
                    fontSize = 20.sp,
                    lineHeight = 20.sp,
                    letterSpacing = 0.sp,
                    fontWeight = FontWeight.Medium,
                ),
                large = CustomFontMeasures(
                    fontSize = 24.sp,
                    lineHeight = 22.sp,
                    letterSpacing = 0.sp,
                    fontWeight = FontWeight.Medium,
                ),
            ),
            headline = CustomFontAttributes(
                small = CustomFontMeasures(
                    fontSize = 16.sp,
                    lineHeight = 25.6.sp,
                    letterSpacing = 0.sp,
                    fontWeight = FontWeight.SemiBold,
                ),
                medium = CustomFontMeasures(
                    fontSize = 18.sp,
                    lineHeight = 28.8.sp,
                    letterSpacing = 0.sp,
                    fontWeight = FontWeight.SemiBold,
                ),
                large = CustomFontMeasures(
                    fontSize = 20.sp,
                    lineHeight = 32.sp,
                    letterSpacing = 0.sp,
                    fontWeight = FontWeight.SemiBold,
                ),
            )
        )
    )

    val Serif = CustomFont(
        name = "Serif",
        displayName = "سریف",
        fontFamily = FontFamily.Companion.Serif,
        specs = CustomFontSpecs(
            label = CustomFontAttributes(
                small = CustomFontMeasures(
                    fontSize = 10.sp,
                    lineHeight = 12.sp,
                    letterSpacing = 0.5.sp,
                    fontWeight = FontWeight.Normal,
                ),
                medium = CustomFontMeasures(
                    fontSize = 12.sp,
                    lineHeight = 14.sp,
                    letterSpacing = 0.5.sp,
                    fontWeight = FontWeight.Normal,
                ),
                large = CustomFontMeasures(
                    fontSize = 14.sp,
                    lineHeight = 16.sp,
                    letterSpacing = 0.5.sp,
                    fontWeight = FontWeight.Normal,
                ),
            ),
            body = CustomFontAttributes(
                small = CustomFontMeasures(
                    fontSize = 12.sp,
                    lineHeight = 14.sp,
                    letterSpacing = 0.25.sp,
                    fontWeight = FontWeight.Normal,
                ),
                medium = CustomFontMeasures(
                    fontSize = 14.sp,
                    lineHeight = 16.sp,
                    letterSpacing = 0.25.sp,
                    fontWeight = FontWeight.Normal,
                ),
                large = CustomFontMeasures(
                    fontSize = 16.sp,
                    lineHeight = 18.sp,
                    letterSpacing = 0.5.sp,
                    fontWeight = FontWeight.Normal,
                ),
            ),
            title = CustomFontAttributes(
                small = CustomFontMeasures(
                    fontSize = 14.sp,
                    lineHeight = 16.sp,
                    letterSpacing = 0.sp,
                    fontWeight = FontWeight.Normal,
                ),
                medium = CustomFontMeasures(
                    fontSize = 16.sp,
                    lineHeight = 18.sp,
                    letterSpacing = 0.sp,
                    fontWeight = FontWeight.Normal,
                ),
                large = CustomFontMeasures(
                    fontSize = 18.sp,
                    lineHeight = 20.sp,
                    letterSpacing = 0.sp,
                    fontWeight = FontWeight.Normal,
                ),
            ),
            display = CustomFontAttributes(
                small = CustomFontMeasures(
                    fontSize = 16.sp,
                    lineHeight = 18.sp,
                    letterSpacing = 0.sp,
                    fontWeight = FontWeight.Normal,
                ),
                medium = CustomFontMeasures(
                    fontSize = 18.sp,
                    lineHeight = 20.sp,
                    letterSpacing = 0.sp,
                    fontWeight = FontWeight.Normal,
                ),
                large = CustomFontMeasures(
                    fontSize = 20.sp,
                    lineHeight = 22.sp,
                    letterSpacing = 0.sp,
                    fontWeight = FontWeight.Normal,
                ),
            ),
            headline = CustomFontAttributes(
                small = CustomFontMeasures(
                    fontSize = 14.sp,
                    lineHeight = 16.sp,
                    letterSpacing = 0.sp,
                    fontWeight = FontWeight.Normal,
                ),
                medium = CustomFontMeasures(
                    fontSize = 16.sp,
                    lineHeight = 18.sp,
                    letterSpacing = 0.sp,
                    fontWeight = FontWeight.Normal,
                ),
                large = CustomFontMeasures(
                    fontSize = 18.sp,
                    lineHeight = 20.sp,
                    letterSpacing = 0.sp,
                    fontWeight = FontWeight.Normal,
                ),
            )
        )
    )



    fun getDefaultFont(): CustomFont {
        return Dana
    }

    fun getAllFonts(): List<CustomFont> {
        return listOf(Nastaliq, Vazirmatn, Dana, Serif)
    }
}