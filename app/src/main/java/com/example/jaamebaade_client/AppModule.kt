package com.example.jaamebaade_client

import android.content.Context
import com.example.jaamebaade_client.api.PoetApiService
import com.example.jaamebaade_client.api.RetrofitInstance
import com.example.jaamebaade_client.repository.PoetRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton
import com.example.jaamebaade_client.utility.DownloadStatusManager

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideApiService(): PoetApiService {
        return Retrofit.Builder()
            .baseUrl("http://10.0.2.2:8085/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(PoetApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideDownloadStatusManager(@ApplicationContext context: Context): DownloadStatusManager {
        return DownloadStatusManager(context)
    }

    @Provides
    @Singleton
    fun providePoetRepository(
        apiService: PoetApiService,
        downloadStatusManager: DownloadStatusManager,
        @ApplicationContext context: Context
    ): PoetRepository {
        return PoetRepository(apiService, context, downloadStatusManager)
    }
}
