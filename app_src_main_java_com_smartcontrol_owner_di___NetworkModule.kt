package com.smartcontrol.owner.di

import com.smartcontrol.owner.data.api.ApiService
import com.smartcontrol.owner.data.api.RetrofitClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideApiService(retrofitClient: RetrofitClient): ApiService {
        return retrofitClient.apiService
    }
}