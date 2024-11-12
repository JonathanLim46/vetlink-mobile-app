package com.example.vetlink.data.model.user

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class UserBasic(
    val id: Int,
    val name: String,
    val username: String,
    val photo: String
) : Parcelable
