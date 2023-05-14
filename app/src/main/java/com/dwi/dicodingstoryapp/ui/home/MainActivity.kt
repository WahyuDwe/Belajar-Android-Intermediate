package com.dwi.dicodingstoryapp.ui.home

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.dwi.dicodingstoryapp.R
import com.dwi.dicodingstoryapp.data.source.remote.StatusResponse
import com.dwi.dicodingstoryapp.databinding.ActivityMainBinding
import com.dwi.dicodingstoryapp.ui.login.LoginActivity
import com.dwi.dicodingstoryapp.ui.maps.MapsActivity
import com.dwi.dicodingstoryapp.ui.stories.UploadStoriesActivity
import com.dwi.dicodingstoryapp.utils.Constanta.ACCESS_TOKEN
import com.dwi.dicodingstoryapp.utils.SharedPrefUtils

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val mainViewModel: MainViewModel by viewModels()
    private lateinit var mainAdapter: MainAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // change title action bar
        supportActionBar?.title = getString(R.string.dicoding_story)

        Log.d("MainActivity", "Access Token ${SharedPrefUtils.getString(ACCESS_TOKEN)}")

        mainAdapter = MainAdapter()
        with(binding.rvStories) {
            layoutManager = LinearLayoutManager(this@MainActivity)
            setHasFixedSize(false)
            this.adapter = mainAdapter
        }

        getStories()

        binding.fabAddStories.setOnClickListener {
            startActivity(Intent(this@MainActivity, UploadStoriesActivity::class.java))
        }
    }

    override fun onResume() {
        super.onResume()
        getStories()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.language -> {
                Intent(Settings.ACTION_LOCALE_SETTINGS).apply {
                    startActivity(this)
                }
            }

            R.id.logout -> {
                SharedPrefUtils.clear()
                startActivity(Intent(this@MainActivity, LoginActivity::class.java))
                finish()
            }

            R.id.maps -> {
                startActivity(Intent(this@MainActivity, MapsActivity::class.java))
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun getStories() {
        isLoading(true)
        mainViewModel.getStories().observe(this) {
            if (it != null) {
                when (it.status) {
                    StatusResponse.SUCCESS -> {
                        isLoading(false)
                        mainAdapter.setData(it.body?.story!!)
                        Log.d("MainActivity", "Success: ${it.body.story}")
                    }
                    StatusResponse.ERROR -> {
                        isLoading(false)
                        Log.d("MainActivity", "Error: ${it.message}")
                    }
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