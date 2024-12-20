package com.example.vetlink.data.model.queue

import com.example.vetlink.data.model.pets.PetBasic
import com.example.vetlink.data.model.user.UserBasic
import com.example.vetlink.data.model.veteriner.VeterinerBasic
import com.google.gson.annotations.SerializedName
import java.util.Date

data class Queue(
    val id: Int,
    val appointment_time: Date,
    val status: String,
    val pet: PetBasic,
    val veteriner: VeterinerBasic,
    val customer: UserBasic,
)
