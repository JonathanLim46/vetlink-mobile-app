package com.example.vetlink.data.model.pets

data class PetsResponse(
    val status: Int,
    val message: String,
    val data: List<Pet>
)
