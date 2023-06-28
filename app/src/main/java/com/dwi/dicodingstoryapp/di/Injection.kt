package com.dwi.dicodingstoryapp.di

import android.content.Context
import com.dwi.dicodingstoryapp.data.source.StoryDataRepository
import com.dwi.dicodingstoryapp.network.ApiConfig

object Injection {
    fun provideRepository(context: Context): StoryDataRepository {
        val apiService = ApiConfig.getService()
        return StoryDataRepository(apiService)
    }
}