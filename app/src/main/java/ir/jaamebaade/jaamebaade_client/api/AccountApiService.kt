package ir.jaamebaade.jaamebaade_client.api

import ir.jaamebaade.jaamebaade_client.api.request.LoginRequest
import ir.jaamebaade.jaamebaade_client.api.request.RegisterRequest
import ir.jaamebaade.jaamebaade_client.api.response.LoginResponse
import ir.jaamebaade.jaamebaade_client.api.response.RegisterResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AccountApiService {
    @POST("/api/v1/auth/register")
    suspend fun register(@Body request: RegisterRequest): Response<RegisterResponse>

    @POST("/api/v1/auth/login")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>
}