package com.example.vetlink.data.network.response.queue

import com.example.vetlink.data.model.forums.Forum

data class ResponseForums(
    val status: Int,
    val message: String,
    val data: List<Forum>
)
