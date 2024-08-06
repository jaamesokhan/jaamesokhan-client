package com.example.jaamebaade_client.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.jaamebaade_client.utility.SharedPrefManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val sharedPrefManager: SharedPrefManager,
) : ViewModel() {
    private val _username = mutableStateOf<String?>(null)
    val username: State<String?> = _username

    init {
//        getUserNameFromSharedPref()
    }

    fun logout() {
        sharedPrefManager.saveAuthCredentials(null, null)
        _username.value = null
    }

    private fun getUserNameFromSharedPref() {
        _username.value = sharedPrefManager.getUsername()
    }

}