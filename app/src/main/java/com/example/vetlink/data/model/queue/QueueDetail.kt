package com.example.vetlink.data.model.queue

import com.example.vetlink.data.model.pets.PetBasic
import com.example.vetlink.data.model.veteriner.VeterinerBasic

data class QueueDetail(
    val id: Int,
    val appointment_time: String,
    val status: String,
    val pet: PetBasic,
    val veteriner: VeterinerBasic,
)
