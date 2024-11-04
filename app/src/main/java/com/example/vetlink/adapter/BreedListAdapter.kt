package com.example.vetlink.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.vetlink.R
import com.example.vetlink.data.model.pets.PetBreed

class BreedListAdapter(
    private val breeds: List<PetBreed>,
    private val onItemClick: (PetBreed) -> Unit
) : RecyclerView.Adapter<BreedListAdapter.BreedViewHolder>() {

    inner class BreedViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val breedNameTextView: TextView = itemView.findViewById(R.id.tvPetCategory)

        fun bind(breed: PetBreed) {
            breedNameTextView.text = breed.breed_name
            itemView.setOnClickListener {
                onItemClick(breed)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BreedViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.each_item_pet_category, parent, false)
        return BreedViewHolder(view)
    }

    override fun onBindViewHolder(holder: BreedViewHolder, position: Int) {
        holder.bind(breeds[position])
    }

    override fun getItemCount(): Int = breeds.size
}
