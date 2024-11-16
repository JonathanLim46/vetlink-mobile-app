package com.example.vetlink.data.model.forums

import com.example.vetlink.data.model.comment.Comment

data class CommentsResponse(
    val status: Int,
    val message: String,
    val data: List<Comment>
)
