package com.example.vetlink.data.model.pets

data class PetAddResponse(
    val status: Int,
    val message: String,
    val data: Pet
)
