package com.example.vetlink.data.model.forums

import com.example.vetlink.data.model.user.UserBasic

data class Forum(
    val id: Int,
    val title: String,
    val last_seen: String,
    val characteristics: String,
    val description: String,
    val pet_image: String,
    val user: UserBasic
    // BELOM ADA KOMENTAR
)
