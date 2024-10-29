package com.example.vetlink.adapter

import android.view.View.OnClickListener

data class ClinicList(
    val clinicImage:Int,
    val clinicName: String,
    val clinicLocation:String,
    val clinicTimeOpen: String,
    val clickListener: OnClickListener
)
