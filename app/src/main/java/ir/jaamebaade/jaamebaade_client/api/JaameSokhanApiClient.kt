package ir.jaamebaade.jaamebaade_client.api

import android.util.Log
import ir.jaamebaade.jaamebaade_client.RecitationsApiService
import ir.jaamebaade.jaamebaade_client.api.request.WordRequest
import ir.jaamebaade.jaamebaade_client.api.response.AudioData
import ir.jaamebaade.jaamebaade_client.model.Poet
import okhttp3.ResponseBody
import retrofit2.Response
import javax.inject.Inject

class JaameSokhanApiClient @Inject constructor(
    private val jaameSokhanApiService: JaameSokhanApiService,
    @RecitationsApiService private val recitationsApiService: JaameSokhanApiService,
    private val ganjoorApiClient: GanjoorApiClient,
) {
    suspend fun getAllRecitations(
        poemId: Int,
        onSuccess: () -> Unit,
        onFailure: () -> Unit
    ): List<AudioData> {
        try {
            val response = recitationsApiService.getAllRecitations(poemId)
            if (response.code() == 200) {
                response.body()?.let {
                    onSuccess()
                    return it
                }
            }
            Log.e("AudioApiClient", "New API returned HTTP ${response.code()}, trying Ganjoor fallback")
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
