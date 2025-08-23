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

    private val _poemFontSize = MutableStateFlow(PoemFontSize.fromOrdinal(sharedPrefManager.getPoemFontSizeIndex()))
    val poemFontSize: StateFlow<PoemFontSize> get() = _poemFontSize

    init {
        _poemFontSize.value = PoemFontSize.fromOrdinal(sharedPrefManager.getPoemFontSizeIndex())
        _poemFontFamily.value = sharedPrefManager.getPoemFont()
    }


    fun setPoemFontSize(size: PoemFontSize) {
        _poemFontSize.value = size
        sharedPrefManager.savePoemFontSizeIndex(size.ordinal)
    }

    fun getAvailableFontSizes(): List<PoemFontSize> = PoemFontSize.entries

    fun getFontNameFromSize(size: PoemFontSize): String = size.displayName

    fun getPoemFontNumberFromSize(size: PoemFontSize): TextUnit {
        return when (size) {
            PoemFontSize.SMALL -> _poemFontFamily.value.specs.body.small.fontSize
            PoemFontSize.MEDIUM -> _poemFontFamily.value.specs.body.medium.fontSize
            PoemFontSize.LARGE -> _poemFontFamily.value.specs.body.large.fontSize
        }
    }

    fun setPoemFontFamily(family: CustomFont) {
        _poemFontFamily.value = family
        sharedPrefManager.savePoemFont(family)
    }


    fun getPoemFontFamily(): FontFamily{
        return poemFontFamily.value.fontFamily
    }

    fun getPoemFontSize(): TextUnit {
        return getPoemFontNumberFromSize(poemFontSize.value)
    }
}
