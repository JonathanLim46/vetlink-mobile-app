package com.example.vetlink.repository

import com.example.vetlink.data.model.veteriner.VeterinerDetailResponse
import com.example.vetlink.data.model.veteriner.VeterinersResponse
import com.example.vetlink.data.network.RetrofitInstance
import com.example.vetlink.data.network.VeterinerApi
import com.example.vetlink.helper.SessionManager

class VeterinerRepository(val session: SessionManager) {
    private val veterinerApi: VeterinerApi = RetrofitInstance.getRetrofit(session).create(VeterinerApi::class.java)

    suspend fun getVeteriners(): Result<VeterinersResponse> {
        return try {
            val response = veterinerApi.getVeteriners()
            Result.success(response)
        }catch (e: Exception){
            Result.failure(e)
        }
    }

    suspend fun getVeteriner(id: Int): Result<VeterinerDetailResponse>{
        return try {
            val response = veterinerApi.getVeteriner(id)
            Result.success(response)
        }catch (e: Exception){
            Result.failure(e)
        }
    }
}