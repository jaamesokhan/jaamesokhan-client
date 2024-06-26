package com.example.jaamebaade_client.utility

import android.content.Context
import android.content.SharedPreferences
import dagger.hilt.android.qualifiers.ApplicationContext

class SharedPrefManager(
    @ApplicationContext private val context: Context
) {
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("JaameBaadePrefs", Context.MODE_PRIVATE)

    fun saveAuthCredentials(username: String?, token: String?) {
        val editor = sharedPreferences.edit()
        editor.putString("Username", username)
        editor.putString("AuthToken", token)
        editor.apply()
    }

    fun getUsername(): String? {
        return sharedPreferences.getString("Username", null)
    }

    fun getAuthToken(): String? {
        return sharedPreferences.getString("AuthToken", null)
    }

}
