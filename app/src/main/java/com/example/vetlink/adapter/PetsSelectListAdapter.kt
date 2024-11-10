package com.example.vetlink.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.vetlink.R
import com.squareup.picasso.Picasso

class PetsSelectListAdapter(
    private val petsSelectList: List<PetsSelectList>,
    private val onPetClick: (String, Int) -> Unit,
) : RecyclerView.Adapter<PetsSelectListAdapter.PetsSelectListViewHolder>() {

    inner class PetsSelectListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val petName: TextView = itemView.findViewById(R.id.tvPetName)
        val petCategory: TextView = itemView.findViewById(R.id.tvPetCategory)
        val petImage: ImageView = itemView.findViewById(R.id.ivPet)

        // Attach the click listener to the itemView
        init {
            itemView.setOnClickListener {
                onPetClick(petsSelectList[adapterPosition].petName,petsSelectList[adapterPosition].petId) // Pass the pet ID to the listener
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PetsSelectListViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.each_item_pet_select, parent, false)
        return PetsSelectListViewHolder(view)
    }

    override fun getItemCount(): Int = petsSelectList.size

    override fun onBindViewHolder(holder: PetsSelectListViewHolder, position: Int) {
        val pet = petsSelectList[position]
        holder.petName.text = pet.petName
        holder.petCategory.text = pet.petCategory
        Picasso.get().load(pet.petImage).resize(50, 50).centerCrop().into(holder.petImage)
    }
}
