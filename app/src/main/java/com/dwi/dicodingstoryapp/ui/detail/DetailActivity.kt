package com.dwi.dicodingstoryapp.ui.detail

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import coil.load
import com.dwi.dicodingstoryapp.R
import com.dwi.dicodingstoryapp.data.source.remote.StatusResponse
import com.dwi.dicodingstoryapp.databinding.ActivityDetailBinding
import com.dwi.dicodingstoryapp.utils.Constanta.STORIES_ID

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    private val viewModel: DetailViewModel by viewModels()
    private lateinit var id: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbarDetailStories.setNavigationOnClickListener {
            onBackPressed()
        }

        id = intent.getStringExtra(STORIES_ID) as String
        Log.d("DetailActivity", "item id is : $id")

        getDetailStories()
    }

    private fun getDetailStories() {
        viewModel.getDetailStories(id).observe(this) { detail ->
            when (detail.status) {
                StatusResponse.LOADING -> {
                    isLoading(true)
                }
                StatusResponse.SUCCESS -> {
                    isLoading(false)
                    binding.apply {
                        detail.body?.story
                        ivDetailStoriesImage.load(detail.body?.story?.photoUrl)
                        tvDetailName.text = detail.body?.story?.name
                        tvDetailDescription.text = detail.body?.story?.description
                    }
                }

                StatusResponse.ERROR -> {
                    isLoading(false)
                    binding.apply {
                        ivDetailStoriesImage.load(R.drawable.ic_image)
                        tvDetailName.text = getString(R.string.gagal_memuat_data)
                        tvDetailDescription.text = getString(R.string.gagal_memuat_data)
                    }
                    Toast.makeText(
                        this@DetailActivity,
                        getString(R.string.gagal_silahkan_coba_lagi),
                        Toast.LENGTH_SHORT
                    ).show()
                }

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