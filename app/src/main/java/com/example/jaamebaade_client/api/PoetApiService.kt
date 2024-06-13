package com.example.jaamebaade_client.api


import com.example.jaamebaade_client.model.Poet
import retrofit2.http.GET


interface PoetApiService {
    @GET("/api/v1/poet/")
    suspend fun getPoets(): PoetResponse
}

data class PoetResponse(val content: List<Poet>)
