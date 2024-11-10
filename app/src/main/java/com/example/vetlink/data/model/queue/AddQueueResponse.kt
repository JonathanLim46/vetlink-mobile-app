package com.example.vetlink.data.model.queue

data class AddQueueResponse(
    val status: Int,
    val message: String,
    val data: Queue
)
