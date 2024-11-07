package com.example.vetlink.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.vetlink.R

class PetsSelectListAdapter(private val petsSelectList: List<PetsSelectList>): RecyclerView.Adapter<PetsSelectListAdapter.PetsSelectListViewHolder>() {

    class PetsSelectListViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val petName: TextView = itemView.findViewById(R.id.tvPetName)
        val petCategory: TextView = itemView.findViewById(R.id.tvPetCategory)
        val petImage: ImageView = itemView.findViewById(R.id.ivPet)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PetsSelectListViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.each_item_pet_select, parent, false)
        return PetsSelectListViewHolder(view)
    }

    override fun getItemCount(): Int {
        return petsSelectList.size
    }

    override fun onBindViewHolder(holder: PetsSelectListViewHolder, position: Int) {
        val petsSelectList = petsSelectList[position]
        holder.petName.text = petsSelectList.petName
        holder.petCategory.text = petsSelectList.petCategory
        holder.petImage.setImageResource(petsSelectList.petImage)
    }
}