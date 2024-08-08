package ir.jaamebaade.jaamebaade_client.api

import ir.jaamebaade.jaamebaade_client.api.request.WordRequest
import javax.inject.Inject

class DictionaryApiClient @Inject constructor(
    private val dictionaryApiService: DictionaryApiService
) {
    suspend fun getMeaning(word: String, successCallBack: () -> Unit, failureCallBack: () -> Unit ): String? {
        try {
            val request = WordRequest(word = word.trim())
            val res = dictionaryApiService.getMeaning(request).body()
            successCallBack()
            return res?.result?.meaning!!
        } catch (e: Exception) {
            failureCallBack()
            return null
        }
    }
}