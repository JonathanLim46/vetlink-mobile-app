package com.example.vetlink.data.model.forums

import android.os.Parcelable
import com.example.vetlink.data.model.comment.Comment
import com.example.vetlink.data.model.user.UserBasic
import kotlinx.parcelize.Parcelize

@Parcelize
data class Forum(
    val id: Int,
    val title: String,
    val status: String,
    val last_seen: String,
    val characteristics: String,
    val description: String,
    val pet_image: String,
    val user: UserBasic,
) : Parcelable
