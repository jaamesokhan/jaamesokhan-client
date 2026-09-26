package ir.jaamebaade.jaamebaade_client.analytics

import android.content.Context
import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AnalyticsLogger @Inject constructor(
    @param:ApplicationContext context: Context,
) {
    private val firebaseAnalytics = FirebaseAnalytics.getInstance(context)

    fun logScreenView(screenName: String) {
        log(FirebaseAnalytics.Event.SCREEN_VIEW) {
            putText(FirebaseAnalytics.Param.SCREEN_NAME, screenName)
            putText(FirebaseAnalytics.Param.SCREEN_CLASS, screenName)
        }
    }

    fun logNotificationOpened(destination: String) {
        log(Events.NOTIFICATION_OPEN) { putText(Params.DESTINATION, destination) }
    }

    fun logPoemView(poetId: Int, poetName: String?, poemId: Int, poemTitle: String?) {
        log(Events.POEM_VIEW) {
            putNumber(Params.POET_ID, poetId)
            putText(Params.POET_NAME, poetName)
            putNumber(Params.POEM_ID, poemId)
            putText(Params.POEM_TITLE, poemTitle)
        }
    }

    fun logRandomPoemRefresh() = log(Events.RANDOM_POEM_REFRESH)

    fun logVersesCopied(poemId: Int, verseCount: Int, source: String) {
        log(Events.VERSES_COPY) {
            putNumber(Params.POEM_ID, poemId)
            putNumber(Params.COUNT, verseCount)
            putText(Params.SOURCE, source)
        }
    }

    fun logShare(contentType: String, itemId: Int?) {
        log(FirebaseAnalytics.Event.SHARE) {
            putText(FirebaseAnalytics.Param.CONTENT_TYPE, contentType)
            itemId?.let { putText(FirebaseAnalytics.Param.ITEM_ID, it.toString()) }
        }
    }

    fun logDictionaryLookup(poemId: Int) {
        log(Events.DICTIONARY_LOOKUP) { putNumber(Params.POEM_ID, poemId) }
    }

    fun logSearch(query: String, resultCount: Int, poetFilterCount: Int) {
        log(FirebaseAnalytics.Event.SEARCH) {
            putText(FirebaseAnalytics.Param.SEARCH_TERM, query)
            putNumber(Params.RESULT_COUNT, resultCount)
            putNumber(Params.POET_FILTER_COUNT, poetFilterCount)
        }
    }

    fun logBookmark(poemId: Int, added: Boolean) {
        log(if (added) Events.BOOKMARK_ADD else Events.BOOKMARK_REMOVE) {
            putNumber(Params.POEM_ID, poemId)
        }
    }

    fun logHighlightAdd(poemId: Int, verseCount: Int) {
        log(Events.HIGHLIGHT_ADD) {
            putNumber(Params.POEM_ID, poemId)
            putNumber(Params.COUNT, verseCount)
        }
    }

    fun logHighlightColorChange(poemId: Int) {
        log(Events.HIGHLIGHT_COLOR_CHANGE) { putNumber(Params.POEM_ID, poemId) }
    }

    fun logHighlightRemove(poemId: Int) {
        log(Events.HIGHLIGHT_REMOVE) { putNumber(Params.POEM_ID, poemId) }
    }

    fun logNoteAdd(poemId: Int, length: Int) {
        log(Events.NOTE_ADD) {
            putNumber(Params.POEM_ID, poemId)
            putNumber(Params.LENGTH, length)
        }
    }

    fun logNoteDelete(source: String) {
        log(Events.NOTE_DELETE) { putText(Params.SOURCE, source) }
    }

    fun logSavedItemRemove(type: String) {
        log(Events.SAVED_ITEM_REMOVE) { putText(Params.TYPE, type) }
    }

    fun logCategoryCreate(type: String) {
        log(Events.CATEGORY_CREATE) { putText(Params.TYPE, type) }
    }

    fun logCategoryDelete() = log(Events.CATEGORY_DELETE)

    fun logCategoryAssign(type: String, categoryCount: Int) {
        log(Events.CATEGORY_ASSIGN) {
            putText(Params.TYPE, type)
            putNumber(Params.COUNT, categoryCount)
        }
    }

    fun logHistoryClear() = log(Events.HISTORY_CLEAR)

    fun logHistoryItemDelete() = log(Events.HISTORY_ITEM_DELETE)

    fun logPoetDownloadStart(poetId: String, poetName: String?) {
        log(Events.POET_DOWNLOAD_START) {
            poetId.toIntOrNull()?.let { putNumber(Params.POET_ID, it) }
            putText(Params.POET_NAME, poetName)
        }
    }

    fun logPoetDownloadResult(poetId: String, poetName: String?, success: Boolean, durationMs: Long) {
        log(if (success) Events.POET_DOWNLOAD_SUCCESS else Events.POET_DOWNLOAD_FAILED) {
            poetId.toIntOrNull()?.let { putNumber(Params.POET_ID, it) }
            putText(Params.POET_NAME, poetName)
            putLong(Params.DURATION_MS, durationMs)
        }
    }

    fun logPoetDelete(poetId: Int, poetName: String) {
        log(Events.POET_DELETE) {
            putNumber(Params.POET_ID, poetId)
            putText(Params.POET_NAME, poetName)
        }
    }

    fun setDownloadedPoetsCount(count: Int) =
        setUserProperty(UserProperties.DOWNLOADED_POETS_COUNT, count.toString())

    fun logRecitationsLoaded(poemId: Int, count: Int, success: Boolean) {
        log(Events.RECITATIONS_LOAD) {
            putNumber(Params.POEM_ID, poemId)
            putNumber(Params.COUNT, count)
            putText(Params.RESULT, if (success) "success" else "failed")
        }
    }

    fun logRecitationPlay(poemId: Int, artistName: String) {
        log(Events.RECITATION_PLAY) {
            putNumber(Params.POEM_ID, poemId)
            putText(Params.ARTIST_NAME, artistName)
        }
    }

    fun logRecitationComplete(poemId: Int?, artistName: String?) {
        log(Events.RECITATION_COMPLETE) {
            poemId?.let { putNumber(Params.POEM_ID, it) }
            putText(Params.ARTIST_NAME, artistName)
        }
    }

    fun logRecitationError(poemId: Int?) {
        log(Events.RECITATION_ERROR) { poemId?.let { putNumber(Params.POEM_ID, it) } }
    }

    fun logPlaybackSpeedChange(speed: Float) {
        log(Events.PLAYBACK_SPEED_CHANGE) { putDouble(Params.VALUE, speed.toDouble()) }
    }

    fun logPlaybackRepeatToggle(enabled: Boolean) {
        log(Events.PLAYBACK_REPEAT_TOGGLE) { putText(Params.VALUE, enabled.toString()) }
    }

    fun logSettingChanged(setting: String, value: String) {
        log(Events.SETTING_CHANGE) {
            putText(Params.SETTING, setting)
            putText(Params.VALUE, value)
        }
        setUserProperty(setting, value)
    }

    fun setUserProperty(name: String, value: String?) {
        firebaseAnalytics.setUserProperty(name.take(MAX_USER_PROPERTY_NAME), value?.take(MAX_USER_PROPERTY_VALUE))
    }

    private fun log(event: String, params: Bundle.() -> Unit = {}) {
        firebaseAnalytics.logEvent(event, Bundle().apply(params))
    }

    private fun Bundle.putText(key: String, value: String?) {
        if (value != null) putString(key, value.take(MAX_PARAM_VALUE))
    }

    private fun Bundle.putNumber(key: String, value: Int) = putLong(key, value.toLong())

    object Events {
        const val NOTIFICATION_OPEN = "notification_open"
        const val POEM_VIEW = "poem_view"
        const val RANDOM_POEM_REFRESH = "random_poem_refresh"
        const val VERSES_COPY = "verses_copy"
        const val DICTIONARY_LOOKUP = "dictionary_lookup"
        const val BOOKMARK_ADD = "bookmark_add"
        const val BOOKMARK_REMOVE = "bookmark_remove"
        const val HIGHLIGHT_ADD = "highlight_add"
        const val HIGHLIGHT_COLOR_CHANGE = "highlight_color_change"
        const val HIGHLIGHT_REMOVE = "highlight_remove"
        const val NOTE_ADD = "note_add"
        const val NOTE_DELETE = "note_delete"
        const val SAVED_ITEM_REMOVE = "saved_item_remove"
        const val CATEGORY_CREATE = "category_create"
        const val CATEGORY_DELETE = "category_delete"
        const val CATEGORY_ASSIGN = "category_assign"
        const val HISTORY_CLEAR = "history_clear"
        const val HISTORY_ITEM_DELETE = "history_item_delete"
        const val POET_DOWNLOAD_START = "poet_download_start"
        const val POET_DOWNLOAD_SUCCESS = "poet_download_success"
        const val POET_DOWNLOAD_FAILED = "poet_download_failed"
        const val POET_DELETE = "poet_delete"
        const val RECITATIONS_LOAD = "recitations_load"
        const val RECITATION_PLAY = "recitation_play"
        const val RECITATION_COMPLETE = "recitation_complete"
        const val RECITATION_ERROR = "recitation_error"
        const val PLAYBACK_SPEED_CHANGE = "playback_speed_change"
        const val PLAYBACK_REPEAT_TOGGLE = "playback_repeat_toggle"
        const val SETTING_CHANGE = "setting_change"
    }

    object Params {
        const val POET_ID = "poet_id"
        const val POET_NAME = "poet_name"
        const val POEM_ID = "poem_id"
        const val POEM_TITLE = "poem_title"
        const val ARTIST_NAME = "artist_name"
        const val COUNT = "count"
        const val LENGTH = "length"
        const val RESULT = "result"
        const val RESULT_COUNT = "result_count"
        const val POET_FILTER_COUNT = "poet_filter_count"
        const val SOURCE = "source"
        const val TYPE = "type"
        const val DESTINATION = "destination"
        const val DURATION_MS = "duration_ms"
        const val SETTING = "setting"
        const val VALUE = "value"
    }

    object UserProperties {
        const val DOWNLOADED_POETS_COUNT = "downloaded_poets_count"
        const val APP_THEME = "app_theme"
        const val POEM_FONT = "poem_font"
        const val POEM_FONT_SIZE = "poem_font_size"
        const val RANDOM_POEM_LAYOUT = "random_poem_layout"
        const val DAILY_POEM_NOTIFICATION = "daily_poem_notification"
    }

    private companion object {
        const val MAX_PARAM_VALUE = 100
        const val MAX_USER_PROPERTY_NAME = 24
        const val MAX_USER_PROPERTY_VALUE = 36
    }
}
