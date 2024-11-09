package com.example.vetlink.data.model.pets

data class PetDetails(
    val id: Int,
    val pet_name: String,
    val photo: String,
    val type: String,
    val breed: String,
    val gender: String,
    val age: Int,
    val weight: String,
    val notes: String
)
