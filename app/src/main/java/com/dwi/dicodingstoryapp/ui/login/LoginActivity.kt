package com.dwi.dicodingstoryapp.ui.login

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.dwi.dicodingstoryapp.R
import com.dwi.dicodingstoryapp.data.source.remote.StatusResponse
import com.dwi.dicodingstoryapp.databinding.ActivityLoginBinding
import com.dwi.dicodingstoryapp.ui.home.MainActivity
import com.dwi.dicodingstoryapp.ui.register.RegisterActivity

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private val viewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // change title action bar
        supportActionBar?.title = getString(R.string.dicoding_story)

        binding.btnLogin.setOnClickListener {
            val email = binding.etEmail.text.toString()
            val password = binding.etPassword.text.toString()

            if (isFieldValid()) {
                isLoading(true)
                viewModel.auth(email, password).observe(this) {
                    if (it != null) {
                        when (it.status) {
                            StatusResponse.SUCCESS -> {
                                isLoading(false)
                                Toast.makeText(
                                    this,
                                    getString(R.string.login_berhasil),
                                    Toast.LENGTH_SHORT
                                ).show()
                                startActivity(Intent(this, MainActivity::class.java))
                                finish()
                            }
                            StatusResponse.ERROR -> {
                                isLoading(false)
                                Toast.makeText(
                                    this,
                                    getString(R.string.login_gagal),
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    }
                }
            }
        }

        binding.tvRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }

    private fun isFieldValid(): Boolean {
        var isValid = true
        when {
            binding.etEmail.text.isNullOrEmpty() -> {
                isValid = false
                Toast.makeText(this, "Email tidak boleh kosong", Toast.LENGTH_SHORT).show()
            }
            binding.etPassword.text.isNullOrEmpty() -> {
                isValid = false
                Toast.makeText(this, "Password tidak boleh kosong", Toast.LENGTH_SHORT).show()
            }
            binding.etPassword.text?.length!! < 8 -> {
                Toast.makeText(
                    this,
                    getString(R.string.password_error),
                    Toast.LENGTH_SHORT
                ).show()
                isValid = false
            }
        }
        return isValid
    }

    private fun isLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }
}