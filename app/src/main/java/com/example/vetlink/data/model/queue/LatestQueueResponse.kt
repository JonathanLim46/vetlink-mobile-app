package com.example.vetlink.data.model.queue

data class LatestQueueResponse(
    val status: Int,
    val message: String,
    val data: LatestQueue
)
