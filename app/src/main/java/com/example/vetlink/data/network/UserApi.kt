package com.example.vetlink.data.network

import com.example.vetlink.data.model.user.ProfileResponse
import retrofit2.Call
import retrofit2.http.GET

interface UserApi {

    @GET("profile")
    fun getProfile(): Call<ProfileResponse>
}