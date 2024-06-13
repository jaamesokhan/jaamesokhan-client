package com.example.jaamebaade_client.api


import com.example.jaamebaade_client.model.Poet
import retrofit2.http.GET


interface PoetApiService {
    @GET("/api/poets") // TODO change dummy api
    suspend fun getPoets(): List<Poet>
}
