package com.example.android.storedirectory.data

import androidx.room.Room
import com.example.android.storedirectory.StoreDirectoryApplication
import com.example.android.storedirectory.repository.Repository
import com.example.android.storedirectory.util.ApiCallAdapterFactory
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
class DataModule {

    @Provides
    @Singleton
    fun provideHttpClient(): OkHttpClient {
        return OkHttpClient().newBuilder().build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(ApiCallAdapterFactory())
            .baseUrl("http://sandbox.bottlerocketapps.com")
            .client(okHttpClient)
            .build()
    }

    @Provides
    fun provideApiService(retrofit: Retrofit): ApiService {
        return retrofit.create(ApiService::class.java)
    }

    @Provides
    fun provideRepository(
        apiService: ApiService,
        storeDao: StoreDao
    ): Repository {
        return Repository(
            apiService,
            storeDao
        )
    }

    @Provides
    @Singleton
    fun provideDatabase(application: StoreDirectoryApplication): AppDatabase {
        return Room.databaseBuilder(
            application,
            AppDatabase::class.java,
            "Stores.db"
        ).build()
    }

    @Provides
    @Singleton
    fun provideDao(appDatabase: AppDatabase): StoreDao {
        return appDatabase.storeDao()
    }
}