package com.dwi.dicodingstoryapp.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dwi.dicodingstoryapp.data.source.StoryDataRepository
import com.dwi.dicodingstoryapp.data.source.remote.ApiResponse
import com.dwi.dicodingstoryapp.data.source.remote.response.LoginResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginViewModel : ViewModel() {
    private val mStoryDataRepository = StoryDataRepository()

    fun auth(
        email: String,
        password: String
    ): LiveData<ApiResponse<LoginResponse>> {
        val authResult = MutableLiveData<ApiResponse<LoginResponse>>()
        viewModelScope.launch {
            withContext(Dispatchers.Main) {
                authResult.postValue(mStoryDataRepository.auth(email, password).value)
            }
        }
        return authResult
    }
}