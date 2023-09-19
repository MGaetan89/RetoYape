package com.fulbiopretell.retoyape.di

import com.fulbiopretell.retoyape.BuildConfig
import com.fulbiopretell.retoyape.source.ApiService
import com.fulbiopretell.retoyape.source.IRepositoryApp
import com.fulbiopretell.retoyape.source.RepositoryApp
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://80b206d4-45e3-4f37-a2e7-431d37ecdc10.mock.pstmn.io/")
            .baseUrl(BuildConfig.URL_SERVICE)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): ApiService {
        return retrofit.create(ApiService::class.java)
    }

    @Singleton
    @Provides
    fun provideRepositoryApp(api: ApiService): IRepositoryApp {
        return RepositoryApp(api)
    }
}