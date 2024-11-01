package com.example.vetlink.repository

import com.example.vetlink.data.model.veteriner.VeterinersResponse
import com.example.vetlink.data.network.RetrofitInstance
import com.example.vetlink.data.network.VeterinerApi
import com.example.vetlink.helper.SessionManager

class VeterinerRepository(val session: SessionManager) {
    private val veterinerApi: VeterinerApi = RetrofitInstance.getRetrofit(session).create(VeterinerApi::class.java)

    suspend fun getVeterinars(): Result<VeterinersResponse> {
        return try {
            val response = veterinerApi.getVeterinars()
            Result.success(response)
        }catch (e: Exception){
            Result.failure(e)
        }
    }
}