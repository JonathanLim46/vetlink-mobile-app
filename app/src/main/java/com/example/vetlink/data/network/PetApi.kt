package com.example.vetlink.data.network

import com.example.vetlink.data.model.pets.PetsResponse
import retrofit2.http.GET

interface PetApi {
    @GET("pets")
    suspend fun getPets(): PetsResponse
}