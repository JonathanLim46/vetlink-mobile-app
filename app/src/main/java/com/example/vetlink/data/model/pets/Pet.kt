package com.example.vetlink.data.model.pets

data class Pet(
    val id: Int,
    val pet_name: String,
    val photo: String,
    val type: String,
    val breed: String,
    val gender: String,
    val age: Int,
    val weight: String,
    val notes: String,
    val id_user: Int,
    val created_at: String,
    val updated_at: String
)
