package com.dwi.dicodingstoryapp.ui.detail

import androidx.lifecycle.ViewModel
import com.dwi.dicodingstoryapp.data.source.StoryDataRepository

class DetailViewModel: ViewModel() {
    private val mStoryDataRepository = StoryDataRepository()

    fun getDetailStories(id: String) = mStoryDataRepository.getDetailStories(id)
}