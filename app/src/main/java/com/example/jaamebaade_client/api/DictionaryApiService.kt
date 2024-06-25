package com.example.jaamebaade_client.api

import com.example.jaamebaade_client.api.request.WordRequest
import com.example.jaamebaade_client.api.response.DictionaryResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface DictionaryApiService {
    @POST("/api/v1/dictionary/meaning")
    suspend fun getMeaning(@Body request: WordRequest): Response<DictionaryResponse>

}