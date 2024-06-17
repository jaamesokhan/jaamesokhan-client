package com.example.jaamebaade_client.api

import com.example.jaamebaade_client.model.Poet
import okhttp3.ResponseBody
import retrofit2.Response
import javax.inject.Inject

class PoetApiClient @Inject constructor(
    private val poetApiService: PoetApiService
) {
    suspend fun getPoets(): List<Poet> {
        val poets = poetApiService.getPoets()
        return poets.content
    }

    fun downloadPoet(id: String): Response<ResponseBody> {
        return poetApiService.downloadPoet(id).execute() // .awaitResponse() for coroutines
    }

}