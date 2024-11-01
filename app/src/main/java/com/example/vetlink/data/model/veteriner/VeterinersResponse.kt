package com.example.vetlink.data.model.veteriner

data class VeterinersResponse(
    val status: Int,
    val message: String,
    val data: List<Veteriner>
)
