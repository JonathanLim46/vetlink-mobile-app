package com.example.vetlink.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.vetlink.R

class PetsCategoryListAdapter(private val petsCategoryList: List<PetsCategoryList>): RecyclerView.Adapter<PetsCategoryListAdapter.PetsCategoryViewHolder>() {

    class PetsCategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var petsCategoryName : TextView = itemView.findViewById(R.id.tvPetCategory)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PetsCategoryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.each_item_pet_category, parent, false)
        return PetsCategoryViewHolder(view)
    }

    override fun getItemCount(): Int {
        return petsCategoryList.size
    }

    override fun onBindViewHolder(holder: PetsCategoryViewHolder, position: Int) {
        val petsCategoryList = petsCategoryList[position]
        holder.petsCategoryName.text = petsCategoryList.namePetsCategory
    }
}