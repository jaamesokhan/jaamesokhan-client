package ir.jaamebaade.jaamebaade_client.utility

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import dagger.hilt.android.qualifiers.ApplicationContext
import ir.jaamebaade.jaamebaade_client.ui.theme.AppThemeType
import ir.jaamebaade.jaamebaade_client.ui.theme.CustomFont
import ir.jaamebaade.jaamebaade_client.ui.theme.CustomFonts
import java.time.LocalTime

class SharedPrefManager(
    @ApplicationContext private val context: Context
) {
    companion object {
        const val POEM_FONT_KEY = "PoemFont"
        const val POEM_FONT_SIZE_KEY = "PoemFontSizeIndex"
        const val USERNAME_KEY = "Username"
        const val AUTH_TOKEN_KEY = "AuthToken"
        const val APP_THEME_TYPE_KEY = "AppThemeType"
        const val SHOW_HINT_FOR_HIGHLIGHT_KEY = "ShowHintForHighlight"
        const val NOTIFICATION_PERMISSION_KEY = "NotificationPermission"
        const val IS_SCHEDULED_NOTIFICATIONS_ENABLED_KEY = "IsScheduledNotificationsEnabled"
        const val IS_SCHEDULED_NOTIFICATIONS_SETUP_KEY = "IsScheduledNotificationsSetUp"
        const val SCHEDULED_NOTIFICATION_TIME_KEY = "ScheduledNotificationTime"
    }

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("JaameBaadePrefs", Context.MODE_PRIVATE)

    fun saveAuthCredentials(username: String?, token: String?) {
        sharedPreferences.edit {
            putString(USERNAME_KEY, username)
            putString(AUTH_TOKEN_KEY, token)
        }
    }

    fun getUsername(): String? {
        return sharedPreferences.getString(USERNAME_KEY, null)
    }

    fun getAuthToken(): String? {
        return sharedPreferences.getString(AUTH_TOKEN_KEY, null)
    }

    fun setShowHintForHighlight(show: Boolean) {
        sharedPreferences.edit {
            putBoolean(SHOW_HINT_FOR_HIGHLIGHT_KEY, show)
        }
    }

    fun getShowHintForHighlight(): Boolean {
        return sharedPreferences.getBoolean(SHOW_HINT_FOR_HIGHLIGHT_KEY, true)
    }

    fun savePoemFont(font: CustomFont) {
        sharedPreferences.edit {
            putString(POEM_FONT_KEY, font.name)
        }
    }

    fun savePoemFontSizeIndex(fontSizeIndex: Int) {
        sharedPreferences.edit {
            putInt(POEM_FONT_SIZE_KEY, fontSizeIndex)
        }
    }

    fun getPoemFont(): CustomFont {
        val savedFontName = sharedPreferences.getString(POEM_FONT_KEY, CustomFonts.Dana.name)
        return CustomFonts.getAllFonts().find { it.name == savedFontName }
            ?: CustomFonts.getDefaultFont()

    }

    fun getPoemFontSizeIndex(): Int {
        return sharedPreferences.getInt(POEM_FONT_SIZE_KEY, 1)
    }

    fun setThemePreference(appThemeType: AppThemeType) {
        sharedPreferences.edit {
            putString(APP_THEME_TYPE_KEY, appThemeType.name)
        }
    }

    fun getThemePreference(): AppThemeType {
        val savedTheme =
            sharedPreferences.getString(APP_THEME_TYPE_KEY, AppThemeType.SYSTEM_AUTO.name)?.let {
                AppThemeType.valueOf(it)
            } ?: AppThemeType.SYSTEM_AUTO
        return savedTheme
    }

    fun setNotificationPermissionPreference(preference: Boolean) {
        sharedPreferences.edit { putBoolean(NOTIFICATION_PERMISSION_KEY, preference) }
    }

    fun getNotificationPermissionPreference(): Boolean {
        return sharedPreferences.getBoolean(NOTIFICATION_PERMISSION_KEY, true)
    }

    fun setIsScheduledNotificationsEnabled(value: Boolean) {
        sharedPreferences.edit { putBoolean(IS_SCHEDULED_NOTIFICATIONS_ENABLED_KEY, value) }
    }

    fun getIsScheduledNotificationsEnabled(): Boolean {
        return sharedPreferences.getBoolean(IS_SCHEDULED_NOTIFICATIONS_ENABLED_KEY, true)
    }

    fun getIsScheduledNotificationsSetUp(): Boolean {
        return sharedPreferences.getBoolean(IS_SCHEDULED_NOTIFICATIONS_SETUP_KEY, false)
    }

    fun setIsScheduledNotificationsSetUp(value: Boolean) {
        sharedPreferences.edit { putBoolean(IS_SCHEDULED_NOTIFICATIONS_SETUP_KEY, value) }
    }

    fun setScheduledNotificationTime(value: LocalTime) {
        val string = "${value.hour}:${value.minute}"
        sharedPreferences.edit { putString(SCHEDULED_NOTIFICATION_TIME_KEY, string) }
    }

    fun getScheduledNotificationTime(): LocalTime {
        val string = sharedPreferences.getString(SCHEDULED_NOTIFICATION_TIME_KEY, null)
        return if (string != null) {
            val split = string.split(":")
            LocalTime.of(split[0].toInt(), split[1].toInt())
        } else {
            LocalTime.of(12, 25)
        }
    }
}
