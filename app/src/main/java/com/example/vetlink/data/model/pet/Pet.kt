package com.example.vetlink.data.model.pet

data class Pet(
    val id: Int,
    val petName: String,
    val type: String,
    val breed: String,
    val age: Int,
    val weight: Float,
    val image: String,
)