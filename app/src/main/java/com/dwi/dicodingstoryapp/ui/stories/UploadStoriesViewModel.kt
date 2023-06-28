package com.dwi.dicodingstoryapp.ui.stories

import androidx.lifecycle.ViewModel
import com.dwi.dicodingstoryapp.data.source.StoryDataRepository
import okhttp3.MultipartBody
import okhttp3.RequestBody

class UploadStoriesViewModel(private val storyDataRepository: StoryDataRepository) : ViewModel() {

    fun uploadStories(file: MultipartBody.Part, description: RequestBody) =
        storyDataRepository.uploadStories(file, description)
}