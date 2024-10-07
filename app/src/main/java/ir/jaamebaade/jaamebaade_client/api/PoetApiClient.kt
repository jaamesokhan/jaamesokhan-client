package ir.jaamebaade.jaamebaade_client.api

import ir.jaamebaade.jaamebaade_client.model.Poet
import okhttp3.ResponseBody
import retrofit2.Response
import javax.inject.Inject

class PoetApiClient @Inject constructor(
    private val poetApiService: PoetApiService
) {
    suspend fun getPoets(page: Int, size: Int, name: String? = null): List<Poet>? {
        val res = poetApiService.getPoets(page, size, name).body()
        return res?.content
    }

    fun downloadPoet(id: String): Response<ResponseBody> {
        return poetApiService.downloadPoet(id).execute()
    }
}