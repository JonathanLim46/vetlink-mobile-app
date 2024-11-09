package com.example.vetlink.data.model.pets

import okhttp3.MultipartBody
import okhttp3.RequestBody

data class PetUpdateRequest(
    val pet_name: RequestBody? = null,
    val photo: MultipartBody.Part? = null,
    val age: RequestBody? = null,
    val weight: RequestBody? = null,
    val gender: RequestBody? = null,
    val notes: RequestBody? = null,
    val type: RequestBody? = null,
    val breed: RequestBody? = null,
)
