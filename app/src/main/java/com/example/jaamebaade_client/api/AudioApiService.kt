package com.example.jaamebaade_client.api

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface AudioApiService {
    @GET("/api/ganjoor/poem/{id}?catInfo=false&catPoems=false&rhymes=false&recitations=true&images=false&songs=false&comments=false&verseDetails=false&navigation=false&relatedpoems=false")
    suspend fun getAllRecitations(
        @Path("id") id: Int,
    ): Response<Map<String, Any>>
}