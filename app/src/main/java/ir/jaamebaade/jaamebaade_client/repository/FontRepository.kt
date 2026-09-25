package ir.jaamebaade.jaamebaade_client.repository

import android.net.Uri
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.TextUnit
import ir.jaamebaade.jaamebaade_client.ui.theme.CustomFont
import ir.jaamebaade.jaamebaade_client.ui.theme.CustomFonts
import ir.jaamebaade.jaamebaade_client.ui.theme.PoemFontSize
import ir.jaamebaade.jaamebaade_client.utility.SharedPrefManager
import ir.jaamebaade.jaamebaade_client.utility.UserFontStorage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class FontRepository @Inject constructor(
    private val sharedPrefManager: SharedPrefManager,
    private val userFontStorage: UserFontStorage,
) {

    private val _userFonts = MutableStateFlow(
        userFontStorage.getFontFiles().map { CustomFonts.fromUserFile(it) }
    )
    val userFonts: StateFlow<List<CustomFont>> get() = _userFonts

    private val _poemFontFamily = MutableStateFlow(CustomFonts.getDefaultFont())
    val poemFontFamily : StateFlow<CustomFont> get() = _poemFontFamily

    private val _poemFontSize = MutableStateFlow(PoemFontSize.fromOrdinal(sharedPrefManager.getPoemFontSizeIndex()))
    val poemFontSize: StateFlow<PoemFontSize> get() = _poemFontSize

    init {
        _poemFontSize.value = PoemFontSize.fromOrdinal(sharedPrefManager.getPoemFontSizeIndex())
        val savedFontName = sharedPrefManager.getPoemFontName()
        _poemFontFamily.value = getAllFonts().find { it.name == savedFontName }
            ?: CustomFonts.getDefaultFont()
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

    fun getAllFonts(): List<CustomFont> = CustomFonts.getAllFonts() + _userFonts.value

    /** Copies the font at [uri] into app storage and selects it on success. */
    suspend fun importUserFont(uri: Uri): UserFontStorage.ImportResult {
        val result = withContext(Dispatchers.IO) { userFontStorage.importFont(uri) }
        if (result is UserFontStorage.ImportResult.Success) {
            val font = CustomFonts.fromUserFile(result.file)
            _userFonts.value = _userFonts.value + font
            setPoemFontFamily(font)
        }
        return result
    }

    fun deleteUserFont(font: CustomFont) {
        val fileName = font.fileName ?: return
        if (_poemFontFamily.value.name == font.name) {
            setPoemFontFamily(CustomFonts.getDefaultFont())
        }
        userFontStorage.deleteFont(fileName)
        _userFonts.value = _userFonts.value.filterNot { it.name == font.name }
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
