package com.dwi.dicodingstoryapp.ui.home

import androidx.lifecycle.ViewModel
import com.dwi.dicodingstoryapp.data.source.StoryDataRepository

class MainViewModel: ViewModel() {
    private val mStoryDataRepository = StoryDataRepository()

    fun getStories() = mStoryDataRepository.getStories()
}