package com.example.vetlink.data.network.response.auth

data class CheckResponse(
    val status: Int,
    val message: String,
    val isExists: Boolean
)
