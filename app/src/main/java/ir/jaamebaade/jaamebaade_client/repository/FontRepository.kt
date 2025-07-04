package ir.jaamebaade.jaamebaade_client.repository

import androidx.compose.ui.unit.TextUnit
import ir.jaamebaade.jaamebaade_client.ui.theme.CustomFont
import ir.jaamebaade.jaamebaade_client.ui.theme.CustomFonts
import ir.jaamebaade.jaamebaade_client.utility.SharedPrefManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class FontRepository @Inject constructor(
    private val sharedPrefManager: SharedPrefManager,
) {
    // probably we won't need these anymore
    private val _fontSizeIndex = MutableStateFlow(1)
    val fontSizeIndex: StateFlow<Int> get() = _fontSizeIndex

    private val _fontFamily = MutableStateFlow(CustomFonts.getDefaultFont())
    val fontFamily: StateFlow<CustomFont> get() = _fontFamily

    private val _poemFontFamily = MutableStateFlow(CustomFonts.getDefaultFont())
    val poemFontFamily : StateFlow<CustomFont> get() = _poemFontFamily

    private val _poemFontSize = MutableStateFlow(1)
    val poemFontSize: StateFlow<Int> get() = _poemFontSize

    init {
        _fontSizeIndex.value = sharedPrefManager.getFontSizeIndex()
        _fontFamily.value = sharedPrefManager.getFont()
        _poemFontSize.value = sharedPrefManager.getFontSizeIndex()
        _poemFontFamily.value = sharedPrefManager.getFont()
    }

    fun setFontSize(size: Int) {
        _fontSizeIndex.value = size
        sharedPrefManager.saveFontSizeIndex(size)
    }

    fun setFontFamily(family: CustomFont) {
        _fontFamily.value = family
        sharedPrefManager.saveFont(family)
    }

    fun setPoemFontSize(size: Int) {
        _poemFontSize.value = size
        sharedPrefManager.savePoemFontSizeIndex(size)
    }

    fun setPoemFontFamily(family: CustomFont) {
        _poemFontFamily.value = family
        sharedPrefManager.saveFont(family)
    }



    fun getAvailableFontSizes(): List<Int> {
        return listOf(0,1,2)
    }
    fun getFontNameFromSize(size: Int): String {
        return when (size) {
            0 -> "ریز"
            1 -> "متوسط"
            2 -> "درشت"
            else -> "متوسط"
        }
    }

    fun getPoemFontSizeFromIndex(index: Int): TextUnit {
        return when (index) {
            0 -> _poemFontFamily.value.specs.body.small.fontSize
            1 -> _poemFontFamily.value.specs.body.medium.fontSize
            2 -> _poemFontFamily.value.specs.body.large.fontSize
            else -> _poemFontFamily.value.specs.body.medium.fontSize
        }
    }
}
