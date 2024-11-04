package com.example.vetlink.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.vetlink.R
import com.example.vetlink.data.model.pets.PetType

class PetTypeListAdapter(
    private val petTypes: List<PetType>,
    private val onItemClick: ((PetType) -> Unit)?
): RecyclerView.Adapter<PetTypeListAdapter.PetTypeViewHolder>() {

    private var selectedPosition = RecyclerView.NO_POSITION

    class PetTypeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var petTypeName : TextView = itemView.findViewById(R.id.tvPetCategory)
        var checkMark: ImageView = itemView.findViewById(R.id.ivCheckMark)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PetTypeViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.each_item_pet_category, parent, false)
        return PetTypeViewHolder(view)
    }

    override fun getItemCount(): Int = petTypes.size

    override fun onBindViewHolder(holder: PetTypeViewHolder, position: Int) {
        val petType = petTypes[position]
        holder.petTypeName.text = petType.name

        // Show or hide the check mark based on whether the item is selected
        holder.checkMark.visibility = if (position == selectedPosition) View.VISIBLE else View.GONE

        holder.itemView.setOnClickListener {
            // Update the selected position
            val previousPosition = selectedPosition
            selectedPosition = position

            // Notify item changes to refresh the previous and current item
            notifyItemChanged(previousPosition)
            notifyItemChanged(selectedPosition)

            // Call the onItemClick listener
            onItemClick?.invoke(petType)
        }
    }
}