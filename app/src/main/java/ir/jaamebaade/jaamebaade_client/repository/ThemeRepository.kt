package ir.jaamebaade.jaamebaade_client.repository

import android.os.Build
import ir.jaamebaade.jaamebaade_client.analytics.AnalyticsLogger
import ir.jaamebaade.jaamebaade_client.ui.theme.AppThemeType
import ir.jaamebaade.jaamebaade_client.utility.SharedPrefManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class ThemeRepository @Inject constructor(
    private val sharedPrefManager: SharedPrefManager,
    private val analytics: AnalyticsLogger,
) {
    private val _appTheme = MutableStateFlow(AppThemeType.SYSTEM_AUTO)
    val appTheme: StateFlow<AppThemeType> = _appTheme

    init {
        getAppThemePreference()
    }

    private fun getAppThemePreference() {
        val currentApiVersion = Build.VERSION.SDK_INT
        val currentAppTheme = sharedPrefManager.getThemePreference()
        if (currentApiVersion < 11 && currentAppTheme == AppThemeType.SYSTEM_AUTO) {
            setAppThemePreference(AppThemeType.LIGHT)
        } else {
            _appTheme.value = currentAppTheme
        }
        analytics.setUserProperty(AnalyticsLogger.UserProperties.APP_THEME, _appTheme.value.name)
    }

    fun setAppThemePreference(appThemeType: AppThemeType) {
        sharedPrefManager.setThemePreference(appThemeType)
        _appTheme.value = appThemeType
        analytics.logSettingChanged(AnalyticsLogger.UserProperties.APP_THEME, appThemeType.name)
    }
}