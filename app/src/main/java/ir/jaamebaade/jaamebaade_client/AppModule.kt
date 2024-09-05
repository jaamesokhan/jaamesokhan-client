package ir.jaamebaade.jaamebaade_client

import android.content.Context
import androidx.room.Room
import ir.jaamebaade.jaamebaade_client.api.AccountApiClient
import ir.jaamebaade.jaamebaade_client.api.AccountApiService
import ir.jaamebaade.jaamebaade_client.api.AudioApiClient
import ir.jaamebaade.jaamebaade_client.api.AudioApiService
import ir.jaamebaade.jaamebaade_client.api.DictionaryApiClient
import ir.jaamebaade.jaamebaade_client.api.DictionaryApiService
import ir.jaamebaade.jaamebaade_client.api.PoetApiClient
import ir.jaamebaade.jaamebaade_client.api.PoetApiService
import ir.jaamebaade.jaamebaade_client.api.SyncAudioClient
import ir.jaamebaade.jaamebaade_client.database.AppDatabase
import ir.jaamebaade.jaamebaade_client.datamanager.PoetDataManager
import ir.jaamebaade.jaamebaade_client.repository.BookmarkRepository
import ir.jaamebaade.jaamebaade_client.repository.CategoryRepository
import ir.jaamebaade.jaamebaade_client.repository.CommentRepository
import ir.jaamebaade.jaamebaade_client.repository.FontRepository
import ir.jaamebaade.jaamebaade_client.repository.HighlightRepository
import ir.jaamebaade.jaamebaade_client.repository.PoemRepository
import ir.jaamebaade.jaamebaade_client.repository.PoetRepository
import ir.jaamebaade.jaamebaade_client.repository.VerseRepository
import ir.jaamebaade.jaamebaade_client.utility.DownloadStatusManager
import ir.jaamebaade.jaamebaade_client.utility.SharedPrefManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import ir.jaamebaade.jaamebaade_client.repository.HistoryRepository
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
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
    fun provideSharedPrefManager(@ApplicationContext context: Context): SharedPrefManager {
        return SharedPrefManager(context)
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
    fun provideAudioApiService(@ApplicationContext context: Context): AudioApiService {
        return Retrofit.Builder()
            .baseUrl(context.getString(R.string.GANJOOR_BASE_URL))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(AudioApiService::class.java)
    }

    @Provides
    @Singleton
    fun providesAudioApiClient(
        apiService: AudioApiService,
    ): AudioApiClient {
        return AudioApiClient(apiService)
    }

    @Provides
    @Singleton
    fun providesAudioSyncClient(): SyncAudioClient {
        return SyncAudioClient()
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

    @Provides
    @Singleton
    fun provideFontRepository(sharedPrefManager: SharedPrefManager): FontRepository {
        return FontRepository(sharedPrefManager)
    }

    @Provides
    @Singleton
    fun provideCommentRepository(appDatabase: AppDatabase): CommentRepository {
        return CommentRepository(appDatabase)
    }

    @Provides
    @Singleton
    fun provideHistoryRepository(appDatabase: AppDatabase): HistoryRepository {
        return HistoryRepository(appDatabase)
    }
}
