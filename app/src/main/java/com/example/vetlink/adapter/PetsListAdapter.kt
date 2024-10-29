package com.example.vetlink.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.vetlink.R
import com.squareup.picasso.Picasso

class PetsListAdapter(private var petsList: List<PetsList>) :
    RecyclerView.Adapter<PetsListAdapter.PetsViewHolder>() {

    private lateinit var listener: RecyclerViewClickListener<PetsList>

    class PetsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val petsMenu: ImageView = itemView.findViewById(R.id.ivMenuMyPetList)
        val petsImageView : ImageView = itemView.findViewById(R.id.ivMyPetsList)
        val petsName : TextView = itemView.findViewById(R.id.tvNameMyPetList)
        val petsBreed : TextView = itemView.findViewById(R.id.tvBreedMyPetList)
        val petsAge : TextView = itemView.findViewById(R.id.tvAgePets)
        val petsWeigth : TextView = itemView.findViewById(R.id.tvWeigthPets)
    }

    fun setClickListener(clickListener: RecyclerViewClickListener<PetsList>){
        this.listener = clickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PetsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.each_item_pets, parent, false)
        return PetsViewHolder(view)
    }

    override fun getItemCount(): Int {
        return petsList.size
    }

    override fun onBindViewHolder(holder: PetsViewHolder, position: Int) {
        val pet = petsList[position]
        holder.petsName.text = pet.petName
        holder.petsBreed.text = pet.petBreed
        holder.petsAge.text = pet.petAge
        holder.petsWeigth.text = pet.petWeigth

        Picasso.get()
            .load(pet.petImage)
            .into(holder.petsImageView)

        holder.petsMenu.setOnClickListener {
            listener.onItemClicke(it, pet)
        }
    }

    // Function to update the list with filtered items
    fun updateList(newList: List<PetsList>) {
        petsList = newList
        notifyDataSetChanged()
    }
}
