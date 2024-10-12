package com.example.vetlink.data.model.pet

data class PetResponse(
    val status: Int,
    val message: String,
    val data: List<Pet>
)