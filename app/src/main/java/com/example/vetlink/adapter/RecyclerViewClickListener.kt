package com.example.vetlink.adapter

import android.view.View

interface RecyclerViewClickListener<T> {

    fun onItemClicke(view: View, item: T)
}