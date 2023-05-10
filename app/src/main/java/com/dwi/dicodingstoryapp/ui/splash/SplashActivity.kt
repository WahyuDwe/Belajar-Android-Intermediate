package com.dwi.dicodingstoryapp.ui.splash

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.dwi.dicodingstoryapp.databinding.ActivitySplashBinding
import com.dwi.dicodingstoryapp.ui.home.MainActivity
import com.dwi.dicodingstoryapp.ui.login.LoginActivity
import com.dwi.dicodingstoryapp.utils.Constanta.ACCESS_TOKEN
import com.dwi.dicodingstoryapp.utils.SharedPrefUtils

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        Handler(mainLooper).postDelayed({
            if (SharedPrefUtils.getString(ACCESS_TOKEN).isNullOrEmpty()) {
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            } else {
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
        }, 3000)
    }
}