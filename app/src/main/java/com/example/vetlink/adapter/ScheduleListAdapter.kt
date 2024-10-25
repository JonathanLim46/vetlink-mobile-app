package com.example.vetlink.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.vetlink.R
import org.w3c.dom.Text

class ScheduleListAdapter(private val scheduleList: List<ScheduleList>): RecyclerView.Adapter<ScheduleListAdapter.ScheduleListViewHolder>() {

    class ScheduleListViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val scheduleDate: TextView = itemView.findViewById(R.id.tvDateSchedule)
        val scheduleFullDate: TextView = itemView.findViewById(R.id.tvFullDate)
        val scheduleClinicName: TextView = itemView.findViewById(R.id.tvClinicNameSchedule)
        val schedulePetName: TextView = itemView.findViewById(R.id.tvPetNameSchedule)
        val scheduleClinicFullLocation: TextView = itemView.findViewById(R.id.tvScheduleClinicLocation)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScheduleListViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.each_item_schedule_card, parent, false)
        return ScheduleListViewHolder(view)
    }

    override fun getItemCount(): Int {
        return scheduleList.size
    }

    override fun onBindViewHolder(holder: ScheduleListViewHolder, position: Int) {
        val scheduleList = scheduleList[position]
        holder.scheduleDate.text = scheduleList.scheduleDate
        holder.scheduleFullDate.text = scheduleList.scheduleFullDate
        holder.scheduleClinicName.text = scheduleList.scheduleClinicName
        holder.schedulePetName.text = scheduleList.schedulePetName
        holder.scheduleClinicFullLocation.text = scheduleList.scheduleClinicFullLocation
    }
}