package com.example.vetlink.data.network

import com.example.vetlink.data.model.auth.LoginResponse
import com.example.vetlink.data.model.auth.LogoutResponse
import com.example.vetlink.data.model.auth.RegisterResponse
import com.example.vetlink.data.model.user.ProfileResponse
import com.example.vetlink.data.network.response.auth.CheckResponse
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.PartMap

interface AuthApi {

    @FormUrlEncoded
    @POST("check-email")
    suspend fun checkEmail(
       @Field("email") email: String
    ): CheckResponse

    @FormUrlEncoded
    @POST("check-username")
    suspend fun checkUsername(
        @Field("username") username: String
    ): CheckResponse

    @Multipart
    @POST("register")
    suspend fun register(
        @Part("name") name: RequestBody,
        @Part("username") username: RequestBody,
        @Part("email") email: RequestBody,
        @Part("password") password: RequestBody,
        @Part("phone") phone: RequestBody,
        @Part photo: MultipartBody.Part? = null,
        @Part("role") role: RequestBody = "customer".toRequestBody("text/plain".toMediaTypeOrNull())
    ): RegisterResponse

    @FormUrlEncoded
    @POST("login")
    suspend fun login(
        @Field("identifier") identifier: String,
        @Field("password") password: String
    ): LoginResponse

    @GET("profile")
    suspend fun getProfile(): ProfileResponse

    @Multipart
    @POST("profile/update")
    suspend fun editProfile(
        @PartMap params: Map<String, @JvmSuppressWildcards RequestBody>,
        @Part photo: MultipartBody.Part? = null,
    ): ProfileResponse

    @POST("logout")
    suspend fun logout(): Response<LogoutResponse>
}