package com.example.vetlink.data.model.auth

import com.example.vetlink.data.model.user.User

data class Login(
    val token: String,
    val user: User
)
