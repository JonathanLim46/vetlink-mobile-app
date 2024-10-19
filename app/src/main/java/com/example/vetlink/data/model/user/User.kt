package com.example.vetlink.data.model.user

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    val id: Int,
    val name: String,
    val username: String,
    val email: String,
    val role: String,
    val phone: String,
    val photo: String,
): Parcelable
