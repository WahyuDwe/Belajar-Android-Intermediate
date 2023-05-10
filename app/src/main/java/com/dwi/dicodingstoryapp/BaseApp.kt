package com.dwi.dicodingstoryapp

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context

class BaseApp : Application() {
    override fun onCreate() {
        super.onCreate()
        context = applicationContext
    }

    companion object {
        @SuppressLint("StaticFieldLeak")
        var context: Context? = null

        @SuppressLint("StaticFieldLeak")
        private var INSTANCE: BaseApp? = null

        val instance: BaseApp
            get() {
                if (INSTANCE == null) {
                    synchronized(BaseApp::class.java) {
                        throw RuntimeException("Something wrong !!!")
                    }
                }
                return INSTANCE!!
            }
    }
}