package com.example.jaamebaade_client.api

import android.util.Log
import com.example.jaamebaade_client.api.request.WordRequest
import javax.inject.Inject

class DictionaryApiClient @Inject constructor(
    private val dictionaryApiService: DictionaryApiService
) {
    suspend fun getMeaning(word: String): String? {
        try {
            val request = WordRequest(word = word.trim())
            val res = dictionaryApiService.getMeaning(request).body()
            return res?.result?.meaning
        } catch (e: Exception) {
            Log.e("DictionaryApiClient", "getMeaning: ${e.message}")
            return "خطای اتصال به سرور"
        }
    }
}