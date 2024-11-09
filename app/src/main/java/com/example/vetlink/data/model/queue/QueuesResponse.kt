package com.example.vetlink.data.model.queue

data class QueuesResponse(
    val status: Int,
    val message: String,
    val data: List<Queue>
)
