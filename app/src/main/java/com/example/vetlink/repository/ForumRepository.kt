package com.example.vetlink.repository

import com.example.vetlink.data.model.forums.ForumsResponse
import com.example.vetlink.data.network.ForumApi
import com.example.vetlink.data.network.RetrofitInstance
import com.example.vetlink.helper.SessionManager

class ForumRepository(val session: SessionManager) {
    private val forumApi = RetrofitInstance.getRetrofit(session).create(ForumApi::class.java)

    suspend fun getForums(): Result<ForumsResponse> {
        return try {
            val response = forumApi.getCustomerForums()
            Result.success(response)
        } catch (e: Exception){
            Result.failure(e)
        }
    }
}