package com.example.vetlink.data.model.auth

data class LoginResponse(
    val status: Int,
    val message: String,
    val data: Login
)
