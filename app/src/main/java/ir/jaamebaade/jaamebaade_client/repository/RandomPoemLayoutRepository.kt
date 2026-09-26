package ir.jaamebaade.jaamebaade_client.repository

import ir.jaamebaade.jaamebaade_client.analytics.AnalyticsLogger
import ir.jaamebaade.jaamebaade_client.ui.theme.RandomPoemLayoutType
import ir.jaamebaade.jaamebaade_client.utility.SharedPrefManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RandomPoemLayoutRepository @Inject constructor(
    private val sharedPrefManager: SharedPrefManager,
    private val analytics: AnalyticsLogger,
) {
    private val _layout = MutableStateFlow(sharedPrefManager.getRandomPoemLayout())
    val layout: StateFlow<RandomPoemLayoutType> = _layout

    init {
        analytics.setUserProperty(AnalyticsLogger.UserProperties.RANDOM_POEM_LAYOUT, _layout.value.name)
    }

    fun setLayout(layout: RandomPoemLayoutType) {
        sharedPrefManager.setRandomPoemLayout(layout)
        _layout.value = layout
        analytics.logSettingChanged(AnalyticsLogger.UserProperties.RANDOM_POEM_LAYOUT, layout.name)
    }
}
