package ir.jaamebaade.jaamebaade_client.utility

import android.content.Context
import android.content.SharedPreferences
import dagger.hilt.android.qualifiers.ApplicationContext
import ir.jaamebaade.jaamebaade_client.ui.theme.AppThemeType
import ir.jaamebaade.jaamebaade_client.ui.theme.CustomFont

class SharedPrefManager(
    @ApplicationContext private val context: Context
) {
    companion object {
        const val FONT_KEY = "Font"
        const val FONT_SIZE_INDEX_KEY = "FontSizeIndex"
        const val USERNAME_KEY = "Username"
        const val AUTH_TOKEN_KEY = "AuthToken"
        const val APP_THEME_TYPE_KEY = "AppThemeType"
        const val SHOW_APP_INTRO_POEM_KEY = "ShowAppIntroPoem"
        const val SHOW_APP_INTRO_MANI_KEY = "ShowAppIntroMain"
        const val SHOW_APP_INTRO_HIGHLIGHT_KEY = "ShowAppIntroHighlight"

        const val NOTIFICATION_PERMISSION_KEY = "NotificationPermission"
    }

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("JaameBaadePrefs", Context.MODE_PRIVATE)

    fun saveAuthCredentials(username: String?, token: String?) {
        val editor = sharedPreferences.edit()
        editor.putString(USERNAME_KEY, username)
        editor.putString(AUTH_TOKEN_KEY, token)
        editor.apply()
    }

    fun getUsername(): String? {
        return sharedPreferences.getString(USERNAME_KEY, null)
    }

    fun getAuthToken(): String? {
        return sharedPreferences.getString(AUTH_TOKEN_KEY, null)
    }

    fun saveFont(font: CustomFont) {
        val editor = sharedPreferences.edit()
        editor.putString(FONT_KEY, font.name)
        editor.apply()
    }

    fun saveFontSizeIndex(fontSizeIndex: Int) {
        val editor = sharedPreferences.edit()
        editor.putInt(FONT_SIZE_INDEX_KEY, fontSizeIndex)
        editor.apply()
    }

    fun getFont(): CustomFont {
        val savedFont = sharedPreferences.getString(FONT_KEY, CustomFont.VAZIRMATN.name)?.let {
            CustomFont.valueOf(it).name
        } ?: CustomFont.VAZIRMATN.name
        return CustomFont.valueOf(savedFont)

    }

    fun getFontSizeIndex(): Int {
        return sharedPreferences.getInt(FONT_SIZE_INDEX_KEY, 1)
    }

    fun setThemePreference(appThemeType: AppThemeType) {
        val editor = sharedPreferences.edit()
        editor.putString(APP_THEME_TYPE_KEY, appThemeType.name)
        editor.apply()
    }

    fun getThemePreference(): AppThemeType {
        val savedTheme =
            sharedPreferences.getString(APP_THEME_TYPE_KEY, AppThemeType.SYSTEM_AUTO.name)?.let {
                AppThemeType.valueOf(it)
            } ?: AppThemeType.SYSTEM_AUTO
        return savedTheme
    }

    fun setHighlightMergeToggleState(state: Boolean) {
        val editor = sharedPreferences.edit()
        editor.putBoolean("highlightMergeToggleState", state)
        editor.apply()
    }

    fun getHighlightMergeToggleState(): Boolean {
        return sharedPreferences.getBoolean("highlightMergeToggleState", false)
    }

    fun setShowAppIntroPoem(showIntro: Boolean) {
        sharedPreferences.edit().putBoolean(SHOW_APP_INTRO_POEM_KEY, showIntro).apply()
    }

    fun getShowAppIntroPoem(): Boolean {
        return sharedPreferences.getBoolean(SHOW_APP_INTRO_POEM_KEY, true)
    }

    fun setShowAppIntroHighlight(showIntro: Boolean) {
        sharedPreferences.edit().putBoolean(SHOW_APP_INTRO_HIGHLIGHT_KEY, showIntro).apply()
    }

    fun getShowAppIntroHighlight(): Boolean {
        return sharedPreferences.getBoolean(SHOW_APP_INTRO_HIGHLIGHT_KEY, true)

    }

    fun getShowAppIntroMain(): Boolean {
        return sharedPreferences.getBoolean(SHOW_APP_INTRO_MANI_KEY, true)
    }

    fun setShowAppIntroMain(value: Boolean) {
        sharedPreferences.edit().putBoolean(SHOW_APP_INTRO_MANI_KEY, value).apply()
    }

    fun setNotificationPermissionPreference(preference: Boolean) {
        sharedPreferences.edit().putBoolean(NOTIFICATION_PERMISSION_KEY, preference).apply()
    }

    fun getNotificationPermissionPreference(): Boolean {
        return sharedPreferences.getBoolean(NOTIFICATION_PERMISSION_KEY, true)
    }
}
