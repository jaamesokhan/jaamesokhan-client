package ir.jaamebaade.jaamebaade_client.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ir.jaamebaade.jaamebaade_client.utility.SharedPrefManager
import ir.jaamebaade.jaamebaade_client.utility.SharedPrefManager.Companion.SHOW_APP_INTRO_NAVBAR_KEY
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NavbarViewModel @Inject constructor(
    private val sharedPreferencesManager: SharedPrefManager
) : ViewModel() {

    private val _showAppIntro = MutableStateFlow(true)
    val showAppIntro = _showAppIntro.asStateFlow()

    init {
        getShowAppIntroState()
    }

    fun setShowAppIntroState(showIntro: Boolean) {
        viewModelScope.launch {
            sharedPreferencesManager.setShowAppIntroMain(SHOW_APP_INTRO_NAVBAR_KEY, showIntro)
            _showAppIntro.value = showIntro
        }
    }

    private fun getShowAppIntroState() {
        viewModelScope.launch {
            _showAppIntro.value = sharedPreferencesManager.getShowAppIntro(SHOW_APP_INTRO_NAVBAR_KEY)
        }
    }
}
