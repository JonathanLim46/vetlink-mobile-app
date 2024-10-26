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
import retrofit2.Response

class AuthRepository(val session: SessionManager) {

    private val authApi: AuthApi = RetrofitInstance.getRetrofit(session).create(AuthApi::class.java)

    suspend fun login(email: String, password: String): LoginResponse {
        return authApi.login(email, password)
    }

    suspend fun register(name: String, username: String, email: String, password: String, phoneNumber: String): RegisterResponse {
        return authApi.register(name, username, email, password, phoneNumber)
    }

    suspend fun getProfile(): ProfileResponse {
        return authApi.getProfile()
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