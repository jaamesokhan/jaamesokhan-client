package com.example.jaamebaade_client.viewmodel

import android.content.Context
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.jaamebaade_client.utility.SharedPrefManager
import dagger.hilt.android.internal.Contexts.getApplication
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val appContext: Context

) : ViewModel() {
    private val _username = mutableStateOf<String?>(null)
    val username: State<String?> = _username

    init {
        getUserNameFromSharedPref()
    }

    fun logout() {
        val sharedPrefManager = SharedPrefManager(getApplication(appContext))
        sharedPrefManager.saveAuthCredentials(null, null)
        _username.value = null
    }

    private fun getUserNameFromSharedPref() {
        val sharedPrefManager = SharedPrefManager(getApplication(appContext))
        _username.value = sharedPrefManager.getUsername()
    }

}