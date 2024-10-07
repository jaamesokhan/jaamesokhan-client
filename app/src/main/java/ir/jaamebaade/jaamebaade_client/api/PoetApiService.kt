package ir.jaamebaade.jaamebaade_client.api


import ir.jaamebaade.jaamebaade_client.api.response.PoetListResponse
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


interface PoetApiService {
    @GET("/api/v1/poet")
    suspend fun getPoets(
        @Query("page") page: Int,
        @Query("size") size: Int,
        @Query("name") name: String? = null
    ): Response<PoetListResponse>

    @GET("/api/v1/poet/download/{id}")
    fun downloadPoet(@Path("id") id: String): Call<ResponseBody>
}
