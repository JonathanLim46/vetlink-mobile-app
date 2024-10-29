package com.example.vetlink.data.model.queue

import com.google.gson.annotations.SerializedName
import java.util.Date

data class Queue(
    val id: Int,
    val appointment_time: Date,
    val status: String,
    val id_customer: Int,
    val id_veteriner: Int,
    val created_at: Date,
    val updated_at: Date
)
