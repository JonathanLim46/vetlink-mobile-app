package com.example.vetlink.repository

import android.se.omapi.Session
import com.example.vetlink.data.model.auth.LoginResponse
import com.example.vetlink.data.network.AuthApi
import com.example.vetlink.data.network.RetrofitInstance
import com.example.vetlink.helper.SessionManager

class AuthRepository(val session: SessionManager) {

    private val authApi: AuthApi = RetrofitInstance.getRetrofit(session).create(AuthApi::class.java)

    suspend fun login(email: String, password: String): LoginResponse {
        return authApi.login(email, password)
    }

    suspend fun logout(): Void {
        return authApi.logout()
    }

}