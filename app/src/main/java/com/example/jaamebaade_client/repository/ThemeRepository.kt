package com.example.jaamebaade_client.repository

import com.example.jaamebaade_client.ui.theme.AppThemeType
import com.example.jaamebaade_client.utility.SharedPrefManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class ThemeRepository @Inject constructor(
    private val sharedPrefManager: SharedPrefManager,
) {
    private val _appTheme = MutableStateFlow(AppThemeType.SYSTEM_AUTO)
    val appTheme: StateFlow<AppThemeType> = _appTheme

    init {
        getAppThemePreference()
    }

    private fun getAppThemePreference() {
        _appTheme.value = sharedPrefManager.getThemePreference()
    }

    fun setAppThemePreference(appThemeType: AppThemeType) {
        sharedPrefManager.setThemePreference(appThemeType)
        _appTheme.value = appThemeType
    }
}