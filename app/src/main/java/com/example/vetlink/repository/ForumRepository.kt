package com.example.vetlink.repository

import com.example.vetlink.data.model.forums.ForumAddResponse
import com.example.vetlink.data.model.forums.ForumsResponse
import com.example.vetlink.data.network.ForumApi
import com.example.vetlink.data.network.RetrofitInstance
import com.example.vetlink.helper.SessionManager
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.HttpException
import java.io.IOException

class ForumRepository(val session: SessionManager) {
    private val forumApi = RetrofitInstance.getRetrofit(session).create(ForumApi::class.java)

    suspend fun getForums(): Result<ForumsResponse> {
        return try {
            val response = forumApi.getForums()
            Result.success(response)
        } catch (e: Exception){
            Result.failure(e)
        }
    }

    suspend fun addForum(
        params: MutableMap<String, RequestBody>,
        photoPart: MultipartBody.Part?
    ): Result<ForumAddResponse> {
        return try {
            val response = forumApi.addForum(params, photoPart)
            if (response.status == 201) {
                Result.success(response)
            } else {
                Result.failure(Exception("Failed with status: ${response.status}"))
            }
        } catch (e: HttpException) {
            // Handle HTTP errors (e.g., 404, 500)
            Result.failure(Exception("HTTP error: ${e.code()} - ${e.message}"))
        } catch (e: IOException) {
            // Handle network-related errors (e.g., no internet)
            Result.failure(Exception("Network error: ${e.message}"))
        } catch (e: Exception) {
            // Handle any other exceptions
            Result.failure(e)
        }
    }
}