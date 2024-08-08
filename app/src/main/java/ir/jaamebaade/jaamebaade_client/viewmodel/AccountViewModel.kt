package ir.jaamebaade.jaamebaade_client.viewmodel

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ir.jaamebaade.jaamebaade_client.api.AccountApiClient
import ir.jaamebaade.jaamebaade_client.utility.SharedPrefManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AccountViewModel @Inject constructor(
    private val accountApiClient: AccountApiClient,
    private val sharedPrefManager: SharedPrefManager,
) : ViewModel() {
    private val _apiResult = mutableStateOf<Boolean?>(null)
    val apiResult: State<Boolean?> = _apiResult


    fun handleLogin(username: String, password: String) {
        viewModelScope.launch {
            _apiResult.value = login(username, password)
        }
    }

    fun handleSignup(username: String, password: String) {
        viewModelScope.launch {
            val id = accountApiClient.register(username, password)
            Log.i("AccountViewModel", "handleSignup: id = $id")
            if (id != null) {
                // login the user
                _apiResult.value = login(username, password)

            } else {
                Log.w("AccountViewModel", "handleSignup: Signup failed!")
            }
        }
    }

    private suspend fun login(username: String, password: String): Boolean {
        val token = accountApiClient.login(username, password)
        if (token != null) {
            sharedPrefManager.saveAuthCredentials(username, token)
            return true
        } else {
            Log.w("AccountViewModel", "login: Login failed!")
            return false
        }

    }
}