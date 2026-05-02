package ir.jaamebaade.jaamebaade_client

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import ir.jaamebaade.jaamebaade_client.api.AccountApiClient
import ir.jaamebaade.jaamebaade_client.api.AccountApiService
import ir.jaamebaade.jaamebaade_client.api.JaameSokhanApiClient
import ir.jaamebaade.jaamebaade_client.api.JaameSokhanApiService
import ir.jaamebaade.jaamebaade_client.api.GanjoorApiClient
import ir.jaamebaade.jaamebaade_client.api.GanjoorAudioApiService
import ir.jaamebaade.jaamebaade_client.api.SyncAudioClient
import ir.jaamebaade.jaamebaade_client.database.AppDatabase
import ir.jaamebaade.jaamebaade_client.datamanager.PoetDataManager
import ir.jaamebaade.jaamebaade_client.repository.BookmarkRepository
import ir.jaamebaade.jaamebaade_client.repository.CategoryRepository
import ir.jaamebaade.jaamebaade_client.repository.CommentRepository
import ir.jaamebaade.jaamebaade_client.repository.FontRepository
import ir.jaamebaade.jaamebaade_client.repository.HighlightRepository
import ir.jaamebaade.jaamebaade_client.repository.HistoryRepository
import ir.jaamebaade.jaamebaade_client.repository.PoemRepository
import ir.jaamebaade.jaamebaade_client.repository.PoetRepository
import ir.jaamebaade.jaamebaade_client.repository.SearchHistoryRepository
import ir.jaamebaade.jaamebaade_client.repository.VerseRepository
import ir.jaamebaade.jaamebaade_client.utility.DownloadStatusManager
import ir.jaamebaade.jaamebaade_client.utility.SharedPrefManager
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    val okHttpClient = OkHttpClient.Builder()
        .connectTimeout(10, TimeUnit.SECONDS)
        .readTimeout(5, TimeUnit.SECONDS)
        .writeTimeout(5, TimeUnit.SECONDS)
        .build()

    @Provides
    @Singleton
    fun provideJaamesokhanRetrofit(@ApplicationContext context: Context): JaameSokhanApiService {
        return Retrofit.Builder()
            .baseUrl(context.getString(R.string.SERVER_BASE_URL))
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
            .create<JaameSokhanApiService>()
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
    fun provideJaameSokhanApiClient(
        apiService: JaameSokhanApiService,
        ganjoorApiClient: GanjoorApiClient
    ): JaameSokhanApiClient {
        return JaameSokhanApiClient(apiService, ganjoorApiClient)
    }

    @Provides
    @Singleton
    fun provideGanjoorAudioApiService(@ApplicationContext context: Context): GanjoorAudioApiService {
        return Retrofit.Builder()
            .baseUrl(context.getString(R.string.GANJOOR_BASE_URL))
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
            .create<GanjoorAudioApiService>()
    }

    @Provides
    @Singleton
    fun provideGanjoorApiClient(
        ganjoorApiService: GanjoorAudioApiService,
    ): GanjoorApiClient {
        return GanjoorApiClient(ganjoorApiService)
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
            ).addMigrations(AppDatabase.MIGRATION_7_8).build()
        }

        return instance
    }

    @Provides
    @Singleton
    fun provideAccountApiService(@ApplicationContext context: Context): AccountApiService {
        return Retrofit.Builder()
            .baseUrl(context.getString(R.string.SERVER_BASE_URL))
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
            .create<AccountApiService>()
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

    @Provides
    @Singleton
    fun provideSearchHistoryRepository(appDatabase: AppDatabase): SearchHistoryRepository {
        return SearchHistoryRepository(appDatabase)
    }
}
