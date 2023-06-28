package com.dwi.dicodingstoryapp.ui.maps

import androidx.lifecycle.ViewModel
import com.dwi.dicodingstoryapp.data.source.StoryDataRepository

class MapsViewModel(private val storyDataRepository: StoryDataRepository) : ViewModel() {

    fun getAllStoriesLocation() = storyDataRepository.getStoriesWithLocation()
}