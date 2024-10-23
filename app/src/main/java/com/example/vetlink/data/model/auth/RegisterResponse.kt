package com.example.vetlink.data.model.auth

import com.example.vetlink.data.model.user.User

data class RegisterResponse(
    val status: Int,
    val message: String,
    val data: RegisterData
)
