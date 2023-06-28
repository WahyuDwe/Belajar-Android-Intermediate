package com.dwi.dicodingstoryapp.ui.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.dwi.dicodingstoryapp.data.source.StoryDataRepository
import com.dwi.dicodingstoryapp.data.source.remote.ApiResponse
import com.dwi.dicodingstoryapp.data.source.remote.response.RegisterResponse

class RegisterViewModel(private val storyDataRepository: StoryDataRepository) : ViewModel() {

    fun register(
        name: String,
        email: String,
        password: String
    ): LiveData<ApiResponse<RegisterResponse>> = storyDataRepository.registerUser(
        name, email, password
    )
}