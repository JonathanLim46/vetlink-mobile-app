package com.example.vetlink.repository

import com.example.vetlink.data.model.auth.LoginResponse
import com.example.vetlink.data.model.user.User
import com.example.vetlink.data.network.AuthApi

class UserRepository(private val authApi: AuthApi) {

    private val users = mutableListOf<User>()

    fun registerUser(user: User): Boolean {
        if (users.any { it.email == user.email }) return false
        users.add(user)
        return true
    }

    suspend fun logoutUser(): Void{
        return authApi.logout()
    }

    suspend fun loginUser(email: String, password: String): LoginResponse {
        return authApi.login(email, password)
    }

}