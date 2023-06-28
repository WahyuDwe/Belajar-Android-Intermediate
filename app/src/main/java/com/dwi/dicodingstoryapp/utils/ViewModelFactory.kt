package com.dwi.dicodingstoryapp.utils

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dwi.dicodingstoryapp.di.Injection
import com.dwi.dicodingstoryapp.ui.detail.DetailViewModel
import com.dwi.dicodingstoryapp.ui.home.MainViewModel
import com.dwi.dicodingstoryapp.ui.login.LoginViewModel
import com.dwi.dicodingstoryapp.ui.maps.MapsViewModel
import com.dwi.dicodingstoryapp.ui.register.RegisterViewModel
import com.dwi.dicodingstoryapp.ui.stories.UploadStoriesViewModel

class ViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(Injection.provideRepository(context)) as T
        }
        else if (modelClass.isAssignableFrom(DetailViewModel::class.java)) {
            return DetailViewModel(Injection.provideRepository(context)) as T
        }
        else if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            return LoginViewModel(Injection.provideRepository(context)) as T
        }
        else if (modelClass.isAssignableFrom(RegisterViewModel::class.java)) {
            return RegisterViewModel(Injection.provideRepository(context)) as T
        }
        else if (modelClass.isAssignableFrom(UploadStoriesViewModel::class.java)) {
            return UploadStoriesViewModel(Injection.provideRepository(context)) as T
        }
        else if (modelClass.isAssignableFrom(MapsViewModel::class.java)) {
            return MapsViewModel(Injection.provideRepository(context)) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}