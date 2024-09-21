package ir.jaamebaade.jaamebaade_client.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ir.jaamebaade.jaamebaade_client.utility.SharedPrefManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainIntroViewModel @Inject constructor(
    private val sharedPrefManager: SharedPrefManager
) : ViewModel() {


    private val _showAppIntro = MutableStateFlow(sharedPrefManager.getShowAppIntroMain())
    val showAppIntro: StateFlow<Boolean> = _showAppIntro

    fun setIntroShown() {
        viewModelScope.launch {
            sharedPrefManager.setShowAppIntroMain(false)
            _showAppIntro.value = false
        }
    }
}