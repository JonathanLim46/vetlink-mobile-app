package com.example.vetlink.data.model.pets

data class PetType(
    val id: Int,
    val name: String,
    val breeds: List<PetBreed>
)
