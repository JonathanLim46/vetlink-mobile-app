package com.example.vetlink.data.model.pets

data class PetTypeBreedResponse(
    val status: Int,
    val message: String,
    val data: List<PetType>
)
