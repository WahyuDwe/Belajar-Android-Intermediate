package com.dwi.dicodingstoryapp.di

import com.dwi.dicodingstoryapp.data.source.StoryDataRepository

object Injection {
    fun provideRepository(): StoryDataRepository {
        return StoryDataRepository()
    }
}