package com.example.vetlink.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.vetlink.R
import com.squareup.picasso.Picasso
import java.text.SimpleDateFormat
import java.util.Locale

class ClinicListAdapter(
    private val clinicList : List<ClinicList>,
    private val isClinicPage: Boolean
) : RecyclerView.Adapter<ClinicListAdapter.ClinicViewHolder>() {

    private lateinit var listener: RecyclerViewClickListener<ClinicList>

    class ClinicViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val clinicImageView : ImageView = itemView.findViewById(R.id.ivClinic)
        val clinicNameTv : TextView = itemView.findViewById(R.id.tvCardClinicName)
        val clinicLocationTv : TextView = itemView.findViewById(R.id.tvClinicLocation)
        val clinicTimeOpenTv : TextView = itemView.findViewById(R.id.tvClinicTimeOpen)
    }

    fun clickListener(clickListener: RecyclerViewClickListener<ClinicList>){
        this.listener = clickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClinicViewHolder {
        val layoutId = if(isClinicPage){
            R.layout.each_item_clinic_main
        } else {
            R.layout.each_item_clinic_home
        }
        val view = LayoutInflater.from(parent.context).inflate(layoutId,parent,false)
        return ClinicViewHolder(view)
    }

    override fun getItemCount(): Int {
        return clinicList.size
    }

    override fun onBindViewHolder(holder: ClinicViewHolder, position: Int) {
        val clinicList = clinicList[position]
//        holder.clinicImageView.setImageResource(R.drawable.img_rspets)
        holder.clinicNameTv.text = clinicList.clinicName
        holder.clinicLocationTv.text = clinicList.clinicLocation
        holder.clinicTimeOpenTv.text = clinicList.clinicTimeOpen

        Picasso.get().load(clinicList.clinicImage)
            .into(holder.clinicImageView)

        holder.itemView.setOnClickListener{
            listener?.onItemClicke(it, clinicList)
        }
    }
}