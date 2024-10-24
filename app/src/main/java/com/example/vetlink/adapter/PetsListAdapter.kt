package com.example.vetlink.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.vetlink.R
import com.example.vetlink.databinding.FragmentMyPetsBinding

class PetsListAdapter(private val petsList: List<PetsList>) : RecyclerView.Adapter<PetsListAdapter.PetsViewHolder>() {

    var listener: RecyclerViewClickListener? = null

    class PetsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val petsMenu: ImageView = itemView.findViewById(R.id.ivMenuMyPetList)
        val petsImageView : ImageView = itemView.findViewById(R.id.ivMyPetsList)
        val petsName : TextView = itemView.findViewById(R.id.tvNameMyPetList)
        val petsBreed : TextView = itemView.findViewById(R.id.tvBreedMyPetList)
        val petsAge : TextView = itemView.findViewById(R.id.tvAgePets)
        val petsWeigth : TextView = itemView.findViewById(R.id.tvWeigthPets)
    }

    fun setClickListener(clickListener: RecyclerViewClickListener){
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
        val petsList = petsList[position]
        holder.petsImageView.setImageResource(petsList.petImage)
        holder.petsName.text = petsList.petName
        holder.petsBreed.text = petsList.petBreed
        holder.petsAge.text = petsList.petAge
        holder.petsWeigth.text = petsList.petWeigth

//        onclick
        holder.petsMenu.setOnClickListener{
            listener?.onItemClicke(it, petsList)
        }
    }
}