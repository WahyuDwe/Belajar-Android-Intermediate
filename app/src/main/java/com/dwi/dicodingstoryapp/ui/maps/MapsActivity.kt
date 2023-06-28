package com.dwi.dicodingstoryapp.ui.maps

import android.content.res.Resources
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.dwi.dicodingstoryapp.R
import com.dwi.dicodingstoryapp.data.source.remote.StatusResponse
import com.dwi.dicodingstoryapp.databinding.ActivityMapsBinding
import com.dwi.dicodingstoryapp.utils.ViewModelFactory
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.MarkerOptions

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private val viewModel: MapsViewModel by viewModels {
        ViewModelFactory(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        mMap.uiSettings.apply {
            isCompassEnabled = true
            isZoomControlsEnabled = true
        }

        getStoryLocation()
        customMapStyle()
    }

    private fun getStoryLocation() {
        viewModel.getAllStoriesLocation().observe(this@MapsActivity) { loc ->
            if (loc != null) {
                when (loc.status) {
                    StatusResponse.LOADING -> {
                        isLoading(true)
                    }
                    StatusResponse.SUCCESS -> {
                        isLoading(false)
                        val lStoriesMap = ArrayList<LatLng>()
                        val lStoriesMapName = ArrayList<String>()
                        for (i in loc.body?.story!!.indices) {
                            lStoriesMap.add(
                                LatLng(
                                    loc.body.story[i].lat!!,
                                    loc.body.story[i].lon!!
                                )
                            )
                            lStoriesMapName.add(loc.body.story[i].name!!)
                        }

                        for (i in lStoriesMap.indices) {
                            mMap.addMarker(
                                MarkerOptions()
                                    .position(lStoriesMap[i])
                                    .title(lStoriesMapName[i])
                            )
                            mMap.animateCamera(
                                CameraUpdateFactory.newLatLngZoom(
                                    lStoriesMap[i],
                                    17f
                                )
                            )
                        }
                    }

                    StatusResponse.ERROR -> {
                        isLoading(false)
                        Toast.makeText(
                            this@MapsActivity,
                            getString(R.string.gagal_memuat_data),
                            Toast.LENGTH_SHORT
                        ).show()
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

    private fun customMapStyle() {
        try {
            val success =
                mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.map_style))
            if (!success) {
                Log.e("MapsActivity", "Style parsing failed.")
            }
        } catch (exception: Resources.NotFoundException) {
            Log.e("MapsActivity", "Can't find style. Error: ", exception)
        }
    }
}