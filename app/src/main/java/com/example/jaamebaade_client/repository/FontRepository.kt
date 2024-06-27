package com.example.jaamebaade_client.repository

import com.example.jaamebaade_client.ui.theme.FONTS
import com.example.jaamebaade_client.utility.SharedPrefManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject
import javax.inject.Singleton

class FontRepository @Inject constructor(
    private val sharedPrefManager: SharedPrefManager,
) {

    public var fonts = FONTS
    private val _fontSize = MutableStateFlow(1) // Default font size
    val fontSize: StateFlow<Int> get() = _fontSize

    private val _fontFamily = MutableStateFlow("DefaultFontFamily")
    val fontFamily: StateFlow<String> get() = _fontFamily

    init {
        _fontSize.value = sharedPrefManager.getFontSize()
        _fontFamily.value = sharedPrefManager.getFont()
    }
    fun setFontSize(size: Int) {
        _fontSize.value = size
        sharedPrefManager.saveFontSize(size)
    }

    fun setFontFamily(family: String) {
        _fontFamily.value = family
        sharedPrefManager.saveFont(family)
    }
}
