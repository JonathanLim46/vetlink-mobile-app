package com.example.vetlink.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.vetlink.R

class FaqListAdapter(private val faqList: List<FaqList>,
        private val isFaqCategory: Boolean) : RecyclerView.Adapter<FaqListAdapter.FaqViewHolder>(){

    class FaqViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val faqHeader: TextView = itemView.findViewById(R.id.tvFaqHeader)
        val faqDesc: TextView = itemView.findViewById(R.id.tvFaqDesc)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FaqViewHolder {
        val layoutId = if(isFaqCategory){
            R.layout.each_item_faq_category_rule
        } else {
            R.layout.each_item_faq_questions
        }
        val view = LayoutInflater.from(parent.context).inflate(layoutId, parent, false)
        return FaqViewHolder(view)
    }

    override fun getItemCount(): Int {
        return faqList.size
    }

    override fun onBindViewHolder(holder: FaqViewHolder, position: Int) {
        val faqList = faqList[position]
        holder.faqHeader.text = faqList.faqHeader
        holder.faqDesc.text = faqList.faqDesc
    }


}