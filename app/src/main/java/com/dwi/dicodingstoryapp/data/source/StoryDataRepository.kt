package com.dwi.dicodingstoryapp.data.source


import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.dwi.dicodingstoryapp.data.source.remote.ApiResponse
import com.dwi.dicodingstoryapp.data.source.remote.response.*
import com.dwi.dicodingstoryapp.network.ApiConfig
import com.dwi.dicodingstoryapp.utils.Constanta.ACCESS_TOKEN
import com.dwi.dicodingstoryapp.utils.SharedPrefUtils
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class StoryDataRepository : StoryDataSource {
    override suspend fun authRequest(email: String, password: String): Response<LoginResponse> {
        return ApiConfig.getService().loginUser(email, password)
    }

    override suspend fun auth(
        email: String, password: String
    ): LiveData<ApiResponse<LoginResponse>> {
        val authResult: MutableLiveData<ApiResponse<LoginResponse>> = MutableLiveData()
        try {
            val response = authRequest(email, password)
            Log.d("LoginViewModel", "code: ${response.code()}")
            if (response.code() == 200) {
                SharedPrefUtils.saveString(
                    ACCESS_TOKEN, "Bearer " + response.body()?.loginResult?.token
                )
                authResult.value = ApiResponse.success(response.body()!!)
                Log.d("LoginViewModel", "auth: ${response.body()}")
            } else {
                authResult.value = ApiResponse.error(response.message())
                Log.d("LoginViewModel", "auth: ${response.message()}, code: ${response.code()}")
            }
        } catch (e: Exception) {
            authResult.value = ApiResponse.error(e.message.toString())
        }
        return authResult
    }

    override fun registerUser(
        name: String, email: String, password: String
    ): LiveData<ApiResponse<RegisterResponse>> {
        val registerResult: MutableLiveData<ApiResponse<RegisterResponse>> = MutableLiveData()

        ApiConfig.getService().registerUser(name, email, password)
            .enqueue(object : Callback<RegisterResponse> {
                override fun onResponse(
                    call: Call<RegisterResponse>,
                    response: Response<RegisterResponse>
                ) {
                    if (response.isSuccessful) {
                        registerResult.value = ApiResponse.success(response.body()!!)
                        Log.d(
                            "RegisterResponse",
                            "onResponse: ${response.body()}, msg: ${response.message()}, code: ${response.code()}, error ${response.body()?.error}"
                        )

                    } else {
                        registerResult.value = ApiResponse.error(response.message())
                        Log.d(
                            "RegisterResponse",
                            "onResponse: ${response.body()}, msg: ${response.message()}, code: ${response.code()}")
                    }
                }

                override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                    registerResult.value = ApiResponse.error(t.message!!)
                }

            })
        return registerResult
    }

    override fun getStories(): LiveData<ApiResponse<StoriesResponse>> {
        val storiesResult: MutableLiveData<ApiResponse<StoriesResponse>> = MutableLiveData()

        ApiConfig.getService().getStories(SharedPrefUtils.getString(ACCESS_TOKEN)!!)
            .enqueue(object : Callback<StoriesResponse> {
                override fun onResponse(
                    call: Call<StoriesResponse>,
                    response: Response<StoriesResponse>
                ) {
                    if (response.isSuccessful) {
                        storiesResult.value = ApiResponse.success(response.body()!!)
                        Log.d(
                            "StoriesResponse",
                            "onResponse: ${response.body()}, msg: ${response.message()}, code: ${response.code()}"
                        )
                    } else {
                        storiesResult.value = ApiResponse.error(response.message())
                        Log.d(
                            "StoriesResponse",
                            "msg: ${response.body()?.message}, code: ${response.code()}, error ${response.body()?.error}"
                        )
                    }
                }

                override fun onFailure(call: Call<StoriesResponse>, t: Throwable) {
                    storiesResult.value = ApiResponse.error(t.message!!)
                }

            })
        return storiesResult
    }

    override fun uploadStories(
        file: MultipartBody.Part,
        description: RequestBody
    ): LiveData<ApiResponse<UploadStoriesResponse>> {
        val uploadResult = MutableLiveData<ApiResponse<UploadStoriesResponse>>()

        ApiConfig.getService()
            .uploadStories(SharedPrefUtils.getString(ACCESS_TOKEN)!!, file, description)
            .enqueue(object : Callback<UploadStoriesResponse> {
                override fun onResponse(
                    call: Call<UploadStoriesResponse>,
                    response: Response<UploadStoriesResponse>
                ) {
                    if (response.isSuccessful) {
                        uploadResult.value = ApiResponse.success(response.body()!!)
                        Log.d(
                            "UploadStoriesResponse",
                            "onResponse: ${response.body()}, msg: ${response.message()}, code: ${response.code()}"
                        )
                    } else {
                        uploadResult.value = ApiResponse.error(response.message())
                        Log.d(
                            "UploadStoriesResponse",
                            "msg: ${response.body()?.message}, code: ${response.code()}, error ${response.body()?.error}"
                        )
                    }
                }

                override fun onFailure(call: Call<UploadStoriesResponse>, t: Throwable) {
                    uploadResult.value = ApiResponse.error(t.message!!)
                }

            })
        return uploadResult
    }

    override fun getDetailStories(id: String): LiveData<ApiResponse<DetailStoriesResponse>> {
        val detailResult = MutableLiveData<ApiResponse<DetailStoriesResponse>>()

        ApiConfig.getService().getDetailStories(
            SharedPrefUtils.getString(ACCESS_TOKEN)!!,
            id
        ).enqueue(object : Callback<DetailStoriesResponse> {
            override fun onResponse(
                call: Call<DetailStoriesResponse>,
                response: Response<DetailStoriesResponse>
            ) {
                if (response.isSuccessful) {
                    detailResult.value = ApiResponse.success(response.body()!!)
                    Log.d(
                        "DetailStoriesResponse",
                        "onResponse: ${response.body()}, msg: ${response.message()}, code: ${response.code()}"
                    )
                } else {
                    detailResult.value = ApiResponse.error(response.message())
                    Log.d(
                        "DetailStoriesResponse",
                        "msg: ${response.body()?.message}, code: ${response.code()}, error ${response.body()?.error}"
                    )
                }
            }

            override fun onFailure(call: Call<DetailStoriesResponse>, t: Throwable) {
                detailResult.value = ApiResponse.error(t.message!!)
            }

        })
        return detailResult
    }
}
