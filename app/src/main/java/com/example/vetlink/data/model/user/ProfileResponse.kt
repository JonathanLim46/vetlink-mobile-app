package com.example.vetlink.data.model.user

data class ProfileResponse(
    val status: Int,
    val message: String,
    val data: User
)
