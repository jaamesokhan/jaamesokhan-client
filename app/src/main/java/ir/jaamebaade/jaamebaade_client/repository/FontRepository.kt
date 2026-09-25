package ir.jaamebaade.jaamebaade_client.repository

import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.TextUnit
import ir.jaamebaade.jaamebaade_client.ui.theme.CustomFont
import ir.jaamebaade.jaamebaade_client.ui.theme.CustomFonts
import ir.jaamebaade.jaamebaade_client.ui.theme.PoemFontSize
import ir.jaamebaade.jaamebaade_client.utility.SharedPrefManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class FontRepository @Inject constructor(
    private val sharedPrefManager: SharedPrefManager,
) {

    private val _poemFontFamily = MutableStateFlow(CustomFonts.getDefaultFont())
    val poemFontFamily : StateFlow<CustomFont> get() = _poemFontFamily

    private val _poemFontSizePercent = MutableStateFlow(sharedPrefManager.getPoemFontSizePercent())
    val poemFontSizePercent: StateFlow<Int> get() = _poemFontSizePercent

    init {
        _poemFontFamily.value = sharedPrefManager.getPoemFont()
    }


    fun setPoemFontSizePercent(percent: Int) {
        val coerced = PoemFontSize.coerce(percent)
        _poemFontSizePercent.value = coerced
        sharedPrefManager.savePoemFontSizePercent(coerced)
    }

    fun getPoemFontSizeFromPercent(percent: Int, font: CustomFont = _poemFontFamily.value): TextUnit {
        return font.specs.body.medium.fontSize * (percent / 100f)
    }

    fun setPoemFontFamily(family: CustomFont) {
        _poemFontFamily.value = family
        sharedPrefManager.savePoemFont(family)
    }


    fun getPoemFontFamily(): FontFamily{
        return poemFontFamily.value.fontFamily
    }

    fun getPoemFontSize(): TextUnit {
        return getPoemFontSizeFromPercent(poemFontSizePercent.value)
    }
}
