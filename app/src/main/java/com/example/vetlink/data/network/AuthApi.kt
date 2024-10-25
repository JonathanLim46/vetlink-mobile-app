package com.example.vetlink.data.network

import com.example.vetlink.data.model.auth.LoginResponse
import com.example.vetlink.data.model.auth.LogoutResponse
import com.example.vetlink.data.model.user.ProfileResponse
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

interface AuthApi {

    @FormUrlEncoded
    @POST("login")
    suspend fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): LoginResponse

    @GET("profile")
    suspend fun getProfile(): ProfileResponse

    @POST("logout")
    suspend fun logout(): Response<LogoutResponse>
}