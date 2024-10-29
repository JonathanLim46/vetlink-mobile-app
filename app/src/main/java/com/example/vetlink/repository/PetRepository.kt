package com.example.vetlink.repository

import com.example.vetlink.data.model.pets.PetsResponse
import com.example.vetlink.data.network.PetApi
import com.example.vetlink.data.network.RetrofitInstance
import com.example.vetlink.helper.SessionManager

class PetRepository(val session: SessionManager) {
    private val petApi: PetApi = RetrofitInstance.getRetrofit(session).create(PetApi::class.java)

    suspend fun getPets(): Result<PetsResponse> {
        return try {
            val response = petApi.getPets()
            Result.success(response)
        }catch (e: Exception){
            Result.failure(e)
        }
    }

}