package com.example.vetlink.data.model.comment

import android.os.Parcelable
import com.example.vetlink.data.model.user.UserBasic
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Comment(
    val id: Int,
    val user: UserBasic,
    val content: String,
) : Parcelable
