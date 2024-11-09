package com.example.vetlink.data.network

import com.example.vetlink.data.model.queue.QueuesResponse
import retrofit2.http.GET

interface QueueApi {
    @GET("customer/queues")
    suspend fun getCustomerQueue(): QueuesResponse
}