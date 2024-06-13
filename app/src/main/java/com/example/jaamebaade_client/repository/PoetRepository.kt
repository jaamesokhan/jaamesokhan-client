package com.example.jaamebaade_client.repository


import com.example.jaamebaade_client.api.PoetApiService
import com.example.jaamebaade_client.api.RetrofitInstance
import com.example.jaamebaade_client.model.Poet

class PoetRepository {
    private val api: PoetApiService = RetrofitInstance.api

    suspend fun getPoets(): List<Poet> {
        val poets = api.getPoets()
        return poets.content
    }
    }