package com.dwi.dicodingstoryapp.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.dwi.dicodingstoryapp.data.source.StoryDataRepository
import com.dwi.dicodingstoryapp.data.source.remote.response.StoryResult

class MainViewModel: ViewModel() {
    private val mStoryDataRepository = StoryDataRepository()

    fun getStories(token: String): LiveData<PagingData<StoryResult>> = mStoryDataRepository.getStories(token).cachedIn(viewModelScope)
}