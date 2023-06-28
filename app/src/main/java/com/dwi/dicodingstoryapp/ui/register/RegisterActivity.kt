package com.dwi.dicodingstoryapp.ui.register

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.dwi.dicodingstoryapp.R
import com.dwi.dicodingstoryapp.data.source.remote.StatusResponse
import com.dwi.dicodingstoryapp.databinding.ActivityRegisterBinding
import com.dwi.dicodingstoryapp.ui.login.LoginActivity
import com.dwi.dicodingstoryapp.utils.ViewModelFactory

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private val viewModel: RegisterViewModel by viewModels {
        ViewModelFactory(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        /**
         * change title action bar
         * */
        supportActionBar?.title = getString(R.string.dicoding_story)

        binding.btnRegister.setOnClickListener {
            val email = binding.etEmail.text.toString()
            val password = binding.etPassword.text.toString()
            val name = binding.etName.text.toString()

            if (isFieldValid()) {
                viewModel.register(
                    name, email, password
                ).observe(this) {
                    if (it != null) {
                        when (it.status) {
                            StatusResponse.LOADING -> {
                                isLoading(true)
                            }
                            StatusResponse.SUCCESS -> {
                                isLoading(false)
                                Toast.makeText(
                                    this,
                                    getString(R.string.pendaftaran_berhasil),
                                    Toast.LENGTH_SHORT
                                ).show()
                                startActivity(
                                    Intent(
                                        this@RegisterActivity,
                                        LoginActivity::class.java
                                    )
                                )
                                finish()
                            }
                            StatusResponse.ERROR -> {
                                isLoading(false)
                                Toast.makeText(
                                    this,
                                    getString(R.string.pendaftaran_gagal),
                                    Toast.LENGTH_SHORT
                                ).show()
                                Log.d("RegisterActivity", "onCreate: ${it.message}")
                            }
                        }
                    }
                }

            }
        }

        binding.tvLogin.setOnClickListener {
            startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
            finish()
        }
    }

    private fun isFieldValid(): Boolean {
        return when {
            binding.etName.text?.isEmpty()!! -> {
                Toast.makeText(
                    this,
                    getString(R.string.nama_kosong),
                    Toast.LENGTH_SHORT
                ).show()
                false
            }
            binding.etEmail.text?.isEmpty()!! -> {
                Toast.makeText(
                    this,
                    getString(R.string.email_kosong),
                    Toast.LENGTH_SHORT
                ).show()
                false
            }
            binding.etPassword.text?.length!! < 8 -> {
                Toast.makeText(
                    this,
                    getString(R.string.password_error),
                    Toast.LENGTH_SHORT
                ).show()
                false
            }
            else -> {
                true
            }
        }

    }

    private fun isLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }
}