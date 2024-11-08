package com.example.vetlink.data.model.pets

data class PetDetailsResponse(
    val  status: Int,
    val message: String,
    val data: PetDetails
)