package com.example.vetlink.data.model.veteriner

data class Veteriner(
    val id: Int,
    val clinic_name: String,
    val clinic_image: String,
    val phone_number: String,
    val email: String,
    val latitude: Float,
    val longitude: Float,
    val address: String,
    val city: String,
    val open_time: String,
    val close_time: String
)
