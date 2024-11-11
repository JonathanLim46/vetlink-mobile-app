package com.example.vetlink.data.model.forums

data class ForumsResponse(
    val status: Int,
    val message: String,
    val data: List<Forum>
)
