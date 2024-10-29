package com.example.vetlink.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView.OnItemClickListener
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.vetlink.R

class PetsCategoryListAdapter(
    private val petsCategoryList: List<PetsCategoryList>,
    private val clickListener: OnItemClickListener? = null
): RecyclerView.Adapter<PetsCategoryListAdapter.PetsCategoryViewHolder>() {

    private var selectedPosition = RecyclerView.NO_POSITION //track selected pos
    private val selectedItems = mutableSetOf<String>() //track selected items

    fun toggleSelection(item: PetsCategoryList) {
        if (selectedItems.contains(item.namePetsCategory)) {
            selectedItems.remove(item.namePetsCategory)
        } else {
            selectedItems.add(item.namePetsCategory)
        }
        notifyDataSetChanged()
    }

    fun getSelectedCategories(): List<String> = selectedItems.toList()


    interface OnItemClickListener{
        fun onItemClick(item: PetsCategoryList)
    }

    class PetsCategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var petsCategoryName : TextView = itemView.findViewById(R.id.tvPetCategory)
        var checkMark: ImageView = itemView.findViewById(R.id.ivCheckMark)
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

        holder.itemView.setBackgroundColor(
            if (position == selectedPosition) Color.LTGRAY
            else Color.WHITE
        )

        holder.checkMark.visibility = if (position == selectedPosition) View.VISIBLE else View.GONE

        holder.itemView.setOnClickListener{
            clickListener?.onItemClick(petsCategoryList)
            selectItem(position)
            notifyDataSetChanged()
        }
    }

    private fun selectItem(position: Int) {
        val previousPosition = selectedPosition
        selectedPosition = position
        notifyItemChanged(previousPosition) // Refresh previous selection
        notifyItemChanged(position)
    }

    fun resetSelection() {
        selectedPosition = RecyclerView.NO_POSITION
        notifyDataSetChanged() // Refresh the view
    }
}