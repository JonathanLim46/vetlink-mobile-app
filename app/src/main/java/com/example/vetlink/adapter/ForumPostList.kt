package com.example.vetlink.adapter

import com.example.vetlink.data.model.comment.Comment

data class ForumPostList(
    val postId: Int? = null,
    val postImageProfile: String?,
    val postImagePets: String,
    val postUsername: String,
    val postStatus: String,
    val postHeader: String,
    val postDescription: String,
    val postLastSeen: String,
    val postCharacteristics: String,
    val postComments: List<Comment>
)
