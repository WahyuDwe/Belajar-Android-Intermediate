package com.dwi.dicodingstoryapp.ui.maps

import androidx.lifecycle.ViewModel
import com.dwi.dicodingstoryapp.data.source.StoryDataRepository

class MapsViewModel : ViewModel() {
    private val mStoryDataRepository = StoryDataRepository()

    fun getAllStoriesLocation() = mStoryDataRepository.getStoriesWithLocation()
}