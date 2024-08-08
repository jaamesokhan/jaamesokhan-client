package ir.jaamebaade.jaamebaade_client.api

import ir.jaamebaade.jaamebaade_client.api.request.WordRequest
import ir.jaamebaade.jaamebaade_client.api.response.DictionaryResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface DictionaryApiService {
    @POST("/api/v1/dictionary/meaning")
    suspend fun getMeaning(@Body request: WordRequest): Response<DictionaryResponse>

}