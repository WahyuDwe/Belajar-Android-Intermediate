//package com.dwi.dicodingstoryapp.utils
//
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.ViewModelProvider
//import com.dwi.dicodingstoryapp.di.Injection
//import com.dwi.dicodingstoryapp.ui.home.MainViewModel
//
//@Suppress("UNCHECKED_CAST")
//class ViewModelFactory() : ViewModelProvider.Factory {
//    override fun <T : ViewModel> create(modelClass: Class<T>): T {
//        return when {
//            modelClass.isAssignableFrom(MainViewModel::class.java) -> {
//                MainViewModel(Injection.provideRepository()) as T
//            }
//
//            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
//        }
//    }
//}