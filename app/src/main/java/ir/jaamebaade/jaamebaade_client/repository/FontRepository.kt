package ir.jaamebaade.jaamebaade_client.repository

import ir.jaamebaade.jaamebaade_client.ui.theme.CustomFont
import ir.jaamebaade.jaamebaade_client.ui.theme.CustomFonts
import ir.jaamebaade.jaamebaade_client.utility.SharedPrefManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class FontRepository @Inject constructor(
    private val sharedPrefManager: SharedPrefManager,
) {

    private val _fontSizeIndex = MutableStateFlow(1)
    val fontSizeIndex: StateFlow<Int> get() = _fontSizeIndex

    private val _fontFamily = MutableStateFlow(CustomFonts.getDefaultFont())
    val fontFamily: StateFlow<CustomFont> get() = _fontFamily

    private val _poemFontFamily = MutableStateFlow(CustomFonts.getDefaultFont())
    val poemFontFamily : StateFlow<CustomFont> get() = _poemFontFamily

    private val _poemFontSizeIndex = MutableStateFlow(1)
    val poemFontSizeIndex: StateFlow<Int> get() = _poemFontSizeIndex

    init {
        _fontSizeIndex.value = sharedPrefManager.getFontSizeIndex()
        _fontFamily.value = sharedPrefManager.getFont()
        _poemFontSizeIndex.value = sharedPrefManager.getFontSizeIndex()
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
        _poemFontSizeIndex.value = size
        sharedPrefManager.saveFontSizeIndex(size)
    }

    fun setPoemFontFamily(family: CustomFont) {
        _poemFontFamily.value = family
        sharedPrefManager.saveFont(family)
    }
}
