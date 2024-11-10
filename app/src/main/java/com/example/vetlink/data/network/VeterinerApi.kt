package com.example.vetlink.data.network

import com.example.vetlink.data.model.veteriner.Veteriner
import com.example.vetlink.data.model.veteriner.VeterinerDetailResponse
import com.example.vetlink.data.model.veteriner.VeterinersResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface VeterinerApi {

    @GET("customer/veteriners")
    suspend fun getVeteriners(): VeterinersResponse

    @GET("customer/veteriner/{id}")
    suspend fun getVeteriner(@Path("id") id: Int): VeterinerDetailResponse

}