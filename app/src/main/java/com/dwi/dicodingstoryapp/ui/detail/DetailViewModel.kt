package com.dwi.dicodingstoryapp.ui.detail

import androidx.lifecycle.ViewModel
import com.dwi.dicodingstoryapp.data.source.StoryDataRepository

class DetailViewModel(private val storyDataRepository: StoryDataRepository): ViewModel() {

    fun getDetailStories(id: String) = storyDataRepository.getDetailStories(id)
}