package com.example.vetlink.data.network

import com.example.vetlink.data.model.forums.CommentAddResponse
import com.example.vetlink.data.model.forums.CommentsResponse
import com.example.vetlink.data.model.forums.ForumAddResponse
import com.example.vetlink.data.model.forums.ForumDeleteResponse
import com.example.vetlink.data.model.forums.ForumUpdateResponse
import com.example.vetlink.data.model.forums.ForumsResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.DELETE
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.PartMap
import retrofit2.http.Path

interface ForumApi {
    @GET("customer/forums")
    suspend fun getForums(): ForumsResponse

    @Multipart
    @POST("customer/forum")
    suspend fun addForum(
        @PartMap params: Map<String, @JvmSuppressWildcards RequestBody>,
        @Part photo: MultipartBody.Part? = null,
    ): ForumAddResponse

    @GET("customer/forum/{id}")
    suspend fun getForum(@Path("id") id: Int): ForumAddResponse

    @DELETE("customer/forum/{id}")
    suspend fun deleteForum(@Path("id") id: Int): ForumDeleteResponse

    @Multipart
    @POST("customer/forum/{id}")
    suspend fun updateForum(
        @Path("id") id: Int,
        @PartMap params: Map<String, @JvmSuppressWildcards RequestBody>,
        @Part photo: MultipartBody.Part? = null,
    ): ForumUpdateResponse

    @POST("customer/forum/status/{id}")
    suspend fun updateStatus(@Path("id") id: Int): ForumUpdateResponse

    @GET("customer/comments/{id}")
    suspend fun getComments(@Path("id") id: Int): CommentsResponse

    @FormUrlEncoded
    @POST("customer/comment/{id}")
    suspend fun addComment(@Path("id") id: Int, @Field("content") comment: String): CommentAddResponse

}