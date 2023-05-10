package com.dwi.dicodingstoryapp.data.source.remote.response

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class DetailStoriesResponse(
	val error: Boolean? = null,
	val message: String? = null,
	val story: Story? = null
) : Parcelable

@Parcelize
data class Story(
	val photoUrl: String? = null,
	val createdAt: String? = null,
	val name: String? = null,
	val description: String? = null,
	val lon: Float? = null,
	val id: String? = null,
	val lat: Float? = null
) : Parcelable
