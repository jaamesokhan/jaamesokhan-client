package ir.jaamebaade.jaamebaade_client.api

import ir.jaamebaade.jaamebaade_client.api.request.WordRequest
import ir.jaamebaade.jaamebaade_client.api.response.AudioData
import ir.jaamebaade.jaamebaade_client.api.response.DictionaryResponse
import ir.jaamebaade.jaamebaade_client.api.response.PoetListResponse
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface JaameSokhanApiService {
    @GET("/api/v1/poet")
    suspend fun getPoets(
        @Query("page") page: Int,
        @Query("size") size: Int,
        @Query("name") name: String? = null
    ): Response<PoetListResponse>

    @GET("/api/v1/poet/download/{id}")
    fun downloadPoet(@Path("id") id: String): Call<ResponseBody>

    @POST("/api/v1/dictionary/meaning")
    suspend fun getMeaning(@Body request: WordRequest): Response<DictionaryResponse>
    @GET("/api/v1/recitations/{poemId}")
    suspend fun getAllRecitations(
        @Path("poemId") poemId: Int,
    ): Response<List<AudioData>>
}