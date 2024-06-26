package com.example.jaamebaade_client

import android.content.Context
import androidx.room.Room
import com.example.jaamebaade_client.api.AccountApiClient
import com.example.jaamebaade_client.api.AccountApiService
import com.example.jaamebaade_client.api.DictionaryApiClient
import com.example.jaamebaade_client.api.DictionaryApiService
import com.example.jaamebaade_client.api.PoetApiClient
import com.example.jaamebaade_client.api.PoetApiService
import com.example.jaamebaade_client.database.AppDatabase
import com.example.jaamebaade_client.datamanager.PoetDataManager
import com.example.jaamebaade_client.repository.BookmarkRepository
import com.example.jaamebaade_client.repository.CategoryRepository
import com.example.jaamebaade_client.repository.HighlightRepository
import com.example.jaamebaade_client.repository.PoemRepository
import com.example.jaamebaade_client.repository.PoetRepository
import com.example.jaamebaade_client.repository.VerseRepository
import com.example.jaamebaade_client.utility.DownloadStatusManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Singleton
    @Provides
    fun provideApplicationContext(
        @ApplicationContext appContext: Context
    ): Context = appContext

    @Provides
    @Singleton
    fun provideApiService(@ApplicationContext context: Context): PoetApiService {
        return Retrofit.Builder()
            .baseUrl(context.getString(R.string.SERVER_BASE_URL))
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
    fun providePoetDataManager(
        downloadStatusManager: DownloadStatusManager,
    ): PoetDataManager {
        return PoetDataManager(downloadStatusManager)
    }

    @Provides
    @Singleton
    fun providePoetApiClient(
        apiService: PoetApiService,
    ): PoetApiClient {
        return PoetApiClient(apiService)
    }

    @Provides
    @Singleton
    fun provideDictionaryApiService(@ApplicationContext context: Context): DictionaryApiService {
        return Retrofit.Builder()
            .baseUrl(context.getString(R.string.SERVER_BASE_URL))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(DictionaryApiService::class.java)
    }

    @Provides
    @Singleton
    fun providesDictionaryApiClient(
        apiService: DictionaryApiService,
    ): DictionaryApiClient {
        return DictionaryApiClient(apiService)
    }

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        var instance: AppDatabase
        synchronized(AppDatabase::class) {
            instance = Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "app_database"
            ).build()
        }

        return instance
    }

    @Provides
    @Singleton
    fun provideAccountApiService(@ApplicationContext context: Context): AccountApiService {
        return Retrofit.Builder()
            .baseUrl(context.getString(R.string.SERVER_BASE_URL))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(AccountApiService::class.java)
    }

    @Provides
    @Singleton
    fun providesAccountApiClient(
        apiService: AccountApiService,
    ): AccountApiClient {
        return AccountApiClient(apiService)
    }

    @Provides
    @Singleton
    fun providePoetRepository(appDatabase: AppDatabase): PoetRepository {
        return PoetRepository(appDatabase)
    }

    @Provides
    @Singleton
    fun providePoemRepository(appDatabase: AppDatabase): PoemRepository {
        return PoemRepository(appDatabase)
    }

    @Provides
    @Singleton
    fun provideCategoryRepository(appDatabase: AppDatabase): CategoryRepository {
        return CategoryRepository(appDatabase)
    }

    @Provides
    @Singleton
    fun provideVerseRepository(appDatabase: AppDatabase): VerseRepository {
        return VerseRepository(appDatabase)
    }

    @Provides
    @Singleton
    fun provideHighlightRepository(appDatabase: AppDatabase): HighlightRepository {
        return HighlightRepository(appDatabase)
    }

    @Provides
    @Singleton
    fun provideBookmarkRepository(appDatabase: AppDatabase): BookmarkRepository {
        return BookmarkRepository(appDatabase)
    }
}
