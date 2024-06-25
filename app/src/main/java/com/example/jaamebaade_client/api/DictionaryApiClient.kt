package com.example.jaamebaade_client.api

import android.util.Log
import com.example.jaamebaade_client.api.request.WordRequest
import javax.inject.Inject

class DictionaryApiClient @Inject constructor(
    private val dictionaryApiService: DictionaryApiService
) {
    suspend fun getMeaning(word: String): String? {
        val res = dictionaryApiService.getMeaning(WordRequest(word = word)).body()
        Log.e("fuck", "getMeaning: $res")
        return res?.result?.meaning
    }
}