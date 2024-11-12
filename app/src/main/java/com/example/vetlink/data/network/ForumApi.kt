package com.example.vetlink.data.network

import com.example.vetlink.data.model.forums.ForumsResponse
import com.example.vetlink.data.network.response.queue.ResponseForums
import retrofit2.http.GET

interface ForumApi {
    @GET("customer/forums")
    suspend fun getForums(): ForumsResponse
}