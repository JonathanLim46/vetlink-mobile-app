package com.example.vetlink.repository

import com.example.vetlink.data.model.pets.PetAddResponse
import com.example.vetlink.data.model.pets.PetDeleteResponse
import com.example.vetlink.data.model.pets.PetDetailsResponse
import com.example.vetlink.data.model.pets.PetTypeBreedResponse
import com.example.vetlink.data.model.pets.PetsResponse
import com.example.vetlink.data.network.PetApi
import com.example.vetlink.data.network.RetrofitInstance
import com.example.vetlink.helper.SessionManager
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.HttpException
import java.io.IOException

class PetRepository(val session: SessionManager) {
    private val petApi: PetApi = RetrofitInstance.getRetrofit(session).create(PetApi::class.java)

    suspend fun addPet(
        pet_name: String,
        type: String,
        file: MultipartBody.Part,
        breed: String,
        age: String,
        weight: String
    ): Result<PetAddResponse> {
        val petNameRequestBody = RequestBody.create("text/plain".toMediaTypeOrNull(), pet_name)
        val typeRequestBody = RequestBody.create("text/plain".toMediaTypeOrNull(), type)
        val breedRequestBody = RequestBody.create("text/plain".toMediaTypeOrNull(), breed)
        val ageRequestBody = RequestBody.create("text/plain".toMediaTypeOrNull(), age)
        val weightRequestBody = RequestBody.create("text/plain".toMediaTypeOrNull(), weight)
        return try {
            val response = petApi.addPet(petNameRequestBody, typeRequestBody, file, breedRequestBody, ageRequestBody, weightRequestBody)
            Result.success(response)
        }catch (e: Exception){
            Result.failure(e)
        }
    }

    suspend fun getPets(): Result<PetsResponse> {
        return try {
            val response = petApi.getPets()
            Result.success(response)
        }catch (e: Exception){
            Result.failure(e)
        }
    }

    suspend fun getPetTypes(): Result<PetTypeBreedResponse>{
        return try {
            val response = petApi.getPetTypes()
            Result.success(response)
        }catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getPetDetails(petId: Int): Result<PetDetailsResponse> {
        return try {
            val response = petApi.getPetDetails(petId)
            Result.success(response)
        }catch (e: Exception){
            Result.failure(e)
        }
    }

    suspend fun deletePet(petId: Int): Result<PetDeleteResponse> {
        return try {
            val response = petApi.deletePet(petId)
            // Optionally check if the response status is successful
            if (response.status == 200) { // Adjust according to your API's success status code
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