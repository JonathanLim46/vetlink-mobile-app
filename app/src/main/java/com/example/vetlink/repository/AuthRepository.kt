package com.example.vetlink.repository

import android.se.omapi.Session
import com.example.vetlink.data.model.auth.LoginResponse
import com.example.vetlink.data.model.auth.LogoutResponse
import com.example.vetlink.data.model.auth.RegisterResponse
import com.example.vetlink.data.model.user.ProfileResponse
import com.example.vetlink.data.network.AuthApi
import com.example.vetlink.data.network.RetrofitInstance
import com.example.vetlink.helper.SessionManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response

class AuthRepository(val session: SessionManager) {

    private val authApi: AuthApi = RetrofitInstance.getRetrofit(session).create(AuthApi::class.java)

    suspend fun login(identifier: String, password: String): LoginResponse {
        return authApi.login(identifier, password)
    }

    suspend fun register(
        name: String,
        username: String,
        email: String,
        password: String,
        phoneNumber: String,
        photo: MultipartBody.Part? = null
    ): RegisterResponse {
        val nameRequestBody = RequestBody.create("text/plain".toMediaTypeOrNull(), name)
        val usernameRequestBody = RequestBody.create("text/plain".toMediaTypeOrNull(), username)
        val emailRequestBody = RequestBody.create("text/plain".toMediaTypeOrNull(), email)
        val passwordRequestBody = RequestBody.create("text/plain".toMediaTypeOrNull(), password)
        val phoneNumberRequestBody = RequestBody.create("text/plain".toMediaTypeOrNull(), phoneNumber)
        return authApi.register(nameRequestBody, usernameRequestBody, emailRequestBody, passwordRequestBody, phoneNumberRequestBody, photo)
    }

    suspend fun getProfile(): ProfileResponse {
        return authApi.getProfile()
    }

    suspend fun editProfile(
        params: MutableMap<String, RequestBody>,
        photo: MultipartBody.Part?
    ): Result<ProfileResponse>{
        return try {
            val response = authApi.updateProfile(params, photo)
            Result.success(response)
        } catch (e: Exception){
            Result.failure(e)
        }
    }

    suspend fun logout(): Boolean {
        return withContext(Dispatchers.IO){
            try{
                val logoutResponse = authApi.logout()
                if (logoutResponse.isSuccessful){
                    session.clearAuthToken()
                    true
                }else {
                    false
                }
            }catch(e: Exception){
                false
            }
        }
    }

}