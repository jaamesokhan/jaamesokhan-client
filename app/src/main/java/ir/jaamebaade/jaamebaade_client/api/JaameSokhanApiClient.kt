package ir.jaamebaade.jaamebaade_client.api

import android.util.Log
import ir.jaamebaade.jaamebaade_client.api.request.WordRequest
import ir.jaamebaade.jaamebaade_client.api.response.AudioData
import ir.jaamebaade.jaamebaade_client.model.Poet
import okhttp3.ResponseBody
import retrofit2.Response
import javax.inject.Inject

class JaameSokhanApiClient @Inject constructor(
    private val jaameSokhanApiService: JaameSokhanApiService,
    private val ganjoorApiClient: GanjoorApiClient,
) {
    suspend fun getAllRecitations(
        poemId: Int,
        onSuccess: () -> Unit,
        onFailure: () -> Unit
    ): List<AudioData> {
        try {
            val res = jaameSokhanApiService.getAllRecitations(poemId).body() ?: return emptyList()
            onSuccess()
            return res
        } catch (e: Exception) {
            Log.e("AudioApiClient", "New API failed: ${e.message}, trying Ganjoor fallback")
        }

        return ganjoorApiClient.getAllRecitations(poemId, onSuccess, onFailure)
    }
    suspend fun getPoets(page: Int, size: Int, name: String? = null): List<Poet>? {
        val res = jaameSokhanApiService.getPoets(page, size, name).body()
        return res?.content
    }

    fun downloadPoet(id: String): Response<ResponseBody> {
        return jaameSokhanApiService.downloadPoet(id).execute()
    }

    suspend fun getMeaning(word: String, successCallBack: () -> Unit, failureCallBack: () -> Unit ): String? {
        try {
            val request = WordRequest(word = word.trim())
            val res = jaameSokhanApiService.getMeaning(request).body()
            successCallBack()
            return res?.result?.meaning!!
        } catch (e: Exception) {
            failureCallBack()
            return null
        }
    }
}
