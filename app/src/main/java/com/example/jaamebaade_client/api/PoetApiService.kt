package com.example.jaamebaade_client.api


import com.example.jaamebaade_client.api.response.PoetListResponse
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path


interface PoetApiService {
    @GET("/api/v1/poet/")
    suspend fun getPoets(): Response<PoetListResponse>

    @GET("/api/v1/poet/download/{id}")
    fun downloadPoet(@Path("id") id: String): Call<ResponseBody>
}
