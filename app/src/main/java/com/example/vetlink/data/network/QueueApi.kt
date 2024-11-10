package com.example.vetlink.data.network

import com.example.vetlink.data.model.queue.AddQueueResponse
import com.example.vetlink.data.model.queue.QueuesResponse
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

interface QueueApi {
    @GET("customer/queues")
    suspend fun getCustomerQueue(): QueuesResponse

    @FormUrlEncoded
    @POST("customer/queue")
    suspend fun addCustomerQueue(
        @Field("id_pet") petId: Int,
        @Field("id_veteriner") veterinerId: Int,
        @Field("appointment_time") date: String
    ): AddQueueResponse
}