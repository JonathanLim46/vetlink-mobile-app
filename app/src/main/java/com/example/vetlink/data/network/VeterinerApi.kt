package com.example.vetlink.data.network

import com.example.vetlink.data.model.veteriner.Veteriner
import com.example.vetlink.data.model.veteriner.VeterinersResponse
import retrofit2.http.GET

interface VeterinerApi {

    @GET("customer/veteriners")
    suspend fun getVeterinars(): VeterinersResponse

}