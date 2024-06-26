package com.example.jaamebaade_client.api

import android.util.Log
import com.example.jaamebaade_client.api.request.LoginRequest
import com.example.jaamebaade_client.api.request.RegisterRequest
import javax.inject.Inject

class AccountApiClient @Inject constructor(
    private val accountApiService: AccountApiService,
) {
    suspend fun register(username: String, password: String): Long? {
        try {
            val request = RegisterRequest(username = username, password = password)
            val res = accountApiService.register(request).body()
            return res?.result
        } catch (e: Exception) {
            Log.e("AccountApiClient", "register: ${e.message}")
            return null
        }
    }

    suspend fun login(username: String, password: String): String? {
        try {
            val request = LoginRequest(username = username, password = password)
            val res = accountApiService.login(request).body()
            return res?.result
        } catch (e: Exception) {
            Log.e("AccountApiClient", "login: ${e.message}")
            return null
        }
    }
}