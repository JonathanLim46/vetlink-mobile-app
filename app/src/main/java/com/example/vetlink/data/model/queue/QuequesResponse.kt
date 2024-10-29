package com.example.vetlink.data.model.queue

data class QuequesResponse(
    val status: Int,
    val message: String,
    val data: List<Queue>
)
