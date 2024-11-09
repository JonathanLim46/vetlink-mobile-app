package com.example.vetlink.repository

import com.example.vetlink.data.model.queue.QueuesResponse
import com.example.vetlink.data.network.QueueApi
import com.example.vetlink.data.network.RetrofitInstance
import com.example.vetlink.helper.SessionManager

class QueueRepository(val session: SessionManager) {
    private val queueApi = RetrofitInstance.getRetrofit(session).create(QueueApi::class.java)

    suspend fun getQueues(): Result<QueuesResponse> {
        return try {
            val response = queueApi.getCustomerQueue()
            Result.success(response)
        } catch (e: Exception){
            Result.failure(e)
        }
    }
}