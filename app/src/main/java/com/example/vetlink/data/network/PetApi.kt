package com.example.vetlink.data.network

import com.example.vetlink.data.model.pets.PetAddResponse
import com.example.vetlink.data.model.pets.PetDeleteResponse
import com.example.vetlink.data.model.pets.PetDetailsResponse
import com.example.vetlink.data.model.pets.PetTypeBreedResponse
import com.example.vetlink.data.model.pets.PetsResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

interface PetApi {
    //add new pet
    @Multipart
    @POST("customer/pet")
    suspend fun addPet(
        @Part("pet_name") pet_name: RequestBody,
        @Part("type") type: RequestBody,
        @Part("file") file: MultipartBody.Part,
        @Part("breed") breed: RequestBody,
        @Part("age") age: RequestBody,
        @Part("weight") weight: RequestBody
    ): PetAddResponse

    //get all pet
    @GET("customer/pets")
    suspend fun getPets(): PetsResponse

    //get pet type
    @GET("customer/pet-types-with-breeds")
    suspend fun getPetTypes(): PetTypeBreedResponse

    //detail pet
    @GET("customer/pet/{petId}")
    suspend fun getPetDetails(@Path("petId") petId: Int): PetDetailsResponse

    //delete pet
    @DELETE("customer/pet/{petId}")
    suspend fun deletePet(@Path("petId") petId: Int): PetDeleteResponse
}