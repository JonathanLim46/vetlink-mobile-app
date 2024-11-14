package com.example.vetlink.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.vetlink.R
import com.example.vetlink.data.model.pets.Pet
import com.squareup.picasso.Picasso

class PetForumSelectAdapter(private val pets: List<Pet>) : RecyclerView.Adapter<PetForumSelectAdapter.PetForumSelectViewHolder>() {

    private var listener: RecyclerViewClickListener<Pet>? = null

    // ViewHolder class to hold item view references
    inner class PetForumSelectViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val petName: TextView = itemView.findViewById(R.id.tvPetName)
        val petCategory: TextView = itemView.findViewById(R.id.tvPetCategory)
        val petImage: ImageView = itemView.findViewById(R.id.ivPet)

        init {
            // Setting click listener on the entire item view
            itemView.setOnClickListener {
                listener?.let {
                    val position = adapterPosition
                    if (position != RecyclerView.NO_POSITION) {
                        it.onItemClicke(itemView, pets[position])
                    }
                }
            }
        }
    }

    // Method to create a ViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PetForumSelectViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.each_item_pet_select, parent, false)
        return PetForumSelectViewHolder(view)
    }

    // Method to bind data to ViewHolder
    override fun onBindViewHolder(holder: PetForumSelectViewHolder, position: Int) {
        val pet = pets[position]

        holder.petName.text = pet.pet_name
        holder.petCategory.text = pet.type

        // Load pet image using Picasso
        if (!pet.photo.isNullOrEmpty()) {
            Picasso.get().load(pet.photo).resize(50, 50).centerCrop().into(holder.petImage)
        } else {
            // Optional: Set a default placeholder image if no photo is available
            holder.petImage.setImageResource(R.drawable.img_schedule_label)
        }
    }

    // Method to return item count
    override fun getItemCount(): Int {
        return pets.size
    }

    // Method to set click listener
    fun setClickListener(clickListener: RecyclerViewClickListener<Pet>) {
        this.listener = clickListener
    }
}
