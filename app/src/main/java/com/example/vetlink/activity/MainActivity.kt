package com.example.vetlink.activity

import ClinicList
import ClinicListAdapter
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.vetlink.R

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var clinicList: ArrayList<ClinicList>
    private lateinit var clinicListAdapter: ClinicListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        init()

    }

    private fun init() {
        recyclerView = findViewById(R.id.rvClinicList)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)

        clinicList = ArrayList()

        addDataToList()

        clinicListAdapter = ClinicListAdapter((clinicList))
        recyclerView.adapter = clinicListAdapter
    }

    private fun addDataToList(){
        clinicList.add(ClinicList(R.drawable.rspets, "Klinik IPB", "Sukmajaya, Depok", "Buka | 07.00 - 15.00"))
        clinicList.add(ClinicList(R.drawable.rspets, "Klinik IPB", "Sukmajaya, Depok", "Buka | 07.00 - 15.00"))
        clinicList.add(ClinicList(R.drawable.rspets, "Klinik IPB", "Sukmajaya, Depok", "Buka | 07.00 - 15.00"))
        clinicList.add(ClinicList(R.drawable.rspets, "Klinik IPB", "Sukmajaya, Depok", "Buka | 07.00 - 15.00"))
    }
}