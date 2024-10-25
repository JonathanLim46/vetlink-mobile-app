package com.example.vetlink.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.vetlink.R

class FaqCategoryListAdapter(private val faqCategoryList: List<FaqCategoryList>) : RecyclerView.Adapter<FaqCategoryListAdapter.FaqCategoryViewHolder>() {

    class FaqCategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val faqIV: ImageView = itemView.findViewById(R.id.ivFAQ)
        val faqHeader: TextView = itemView.findViewById(R.id.tvFAQCategory)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FaqCategoryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.each_item_faq_category, parent, false)
        return FaqCategoryViewHolder(view)
    }

    override fun getItemCount(): Int {
        return faqCategoryList.size
    }

    override fun onBindViewHolder(holder: FaqCategoryViewHolder, position: Int) {
        val faqCategoryList = faqCategoryList[position]
        holder.faqIV.setImageResource(faqCategoryList.faqIV)
        holder.faqHeader.text = faqCategoryList.faqHeader
    }

}