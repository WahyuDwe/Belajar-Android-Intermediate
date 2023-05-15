package com.dwi.dicodingstoryapp.data.source

import androidx.lifecycle.LiveData
import androidx.paging.PagingData
import com.dwi.dicodingstoryapp.data.source.remote.ApiResponse
import com.dwi.dicodingstoryapp.data.source.remote.response.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response

interface StoryDataSource {
    suspend fun authRequest(email: String, password: String): Response<LoginResponse>

    suspend fun auth(email: String, password: String): LiveData<ApiResponse<LoginResponse>>

    fun registerUser(
        name: String,
        email: String,
        password: String
    ): LiveData<ApiResponse<RegisterResponse>>

    fun getStories(): LiveData<PagingData<StoryResult>>

    fun uploadStories(
        file: MultipartBody.Part,
        description: RequestBody
    ): LiveData<ApiResponse<UploadStoriesResponse>>

    fun getDetailStories(id: String): LiveData<ApiResponse<DetailStoriesResponse>>

    fun getStoriesWithLocation(): LiveData<ApiResponse<StoriesResponse>>
}