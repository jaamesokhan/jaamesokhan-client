package com.example.jaamebaade_client.repository


import com.example.jaamebaade_client.api.PoetApiService
import com.example.jaamebaade_client.api.RetrofitInstance

class PoetRepository {
    private val api: PoetApiService = RetrofitInstance.api

    suspend fun getPoets() = api.getPoets()
}