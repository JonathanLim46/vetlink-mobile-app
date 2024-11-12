package com.example.vetlink.fragment

import android.app.DatePickerDialog
import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.vetlink.R
import com.example.vetlink.activity.MenuActivity
import com.example.vetlink.adapter.PetsSelectList
import com.example.vetlink.adapter.PetsSelectListAdapter
import com.example.vetlink.databinding.FragmentClinicPageBinding
import com.example.vetlink.viewModel.MenuActivityViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.squareup.picasso.Picasso
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class ClinicPageFragment : Fragment() {

    private lateinit var binding: FragmentClinicPageBinding
    private var petsSelectList: ArrayList<PetsSelectList>? = null
    private var petsSelectListAdapter: PetsSelectListAdapter? = null
    private val sharedMenuActivityViewModel: MenuActivityViewModel by activityViewModels()
    private var clinicId: Int? = null
    private var petId: Int? = null
    private var appointmentDate: String? = null
    private var longitude: Float? = null
    private var latitude: Float? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentClinicPageBinding.inflate(inflater, container, false)

        setupObservers()
        initView()
        return binding.root
    }

    private fun setupObservers() {
        sharedMenuActivityViewModel.veterinerDetail.observe(viewLifecycleOwner) { veterinerDetail ->
            with(binding) {
                tvClinicPage.text = veterinerDetail.clinic_name
                tvClinicLocationPage.text = veterinerDetail.city
                Picasso.get().load(veterinerDetail.clinic_image).into(ivClinicPage)
                clinicId = veterinerDetail.id
                latitude = veterinerDetail.latitude
                longitude = veterinerDetail.longitude
                Log.d("Location", "$latitude, $longitude")
            }
        }

        sharedMenuActivityViewModel.pets.observe(viewLifecycleOwner) { pets ->
            petsSelectList = ArrayList()
            pets.forEach { pet ->
                petsSelectList!!.add(PetsSelectList(pet.id, pet.pet_name, pet.type, pet.photo))
            }
            petsSelectListAdapter?.notifyDataSetChanged()
        }

        sharedMenuActivityViewModel.addQueueResponse.observe(viewLifecycleOwner){ response ->
            if(response == 201){
                val dialog = BottomSheetDialog(requireContext())
                val viewLayout = layoutInflater.inflate(R.layout.layout_bottom_sheet_success_dialog, null, false)
                dialog.setContentView(viewLayout)
                dialog.setCancelable(true)

                val btnDone = viewLayout.findViewById<Button>(R.id.btnDone)
                btnDone.setOnClickListener {
                    startActivity(Intent(activity, MenuActivity::class.java).apply {
                        putExtra("MENU_TITLE", "Schedule")
                    })
                    activity?.finish()

                }
                dialog.show()
            }else{
                Toast.makeText(requireContext(), "Failed to add queue", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun initView() {

        with(binding) {
            etDateClinicPage.setOnClickListener {
                showDatePicker()
            }
            btnVisit.setOnClickListener {
                visitClinic()
            }
            tvSelectPetsDialog.setOnClickListener {
                selectPetsDialog()
            }
            ivLocationButton.setOnClickListener {
                val mapUri = Uri.parse("https://maps.google.com/maps?daddr=$latitude,$longitude")
                val intent = Intent(Intent.ACTION_VIEW, mapUri)

                startActivity(intent)

//                val gmmIntentUri = Uri.parse("google.streetview:cbll=$latitude, $longitude") //latitude | longitude
//
//                // Create an Intent from gmmIntentUri. Set the action to ACTION_VIEW
//                val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
//                // Make the Intent explicit by setting the Google Maps package
//                mapIntent.setPackage("com.google.android.apps.maps")
//
//                // Attempt to start an activity that can handle the Intent
//                startActivity(mapIntent)

            }
        }
    }

    private fun showDatePicker() {
        val calendar = Calendar.getInstance()
        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { _, year, month, dayOfMonth ->
                val date = "$dayOfMonth/${month + 1}/$year"
                binding.etDateClinicPage.setText(date)
                binding.tvVisitDateUserSelect.text = date

                // Use Calendar to set the date properly
                calendar.set(year, month, dayOfMonth)

                val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

                appointmentDate = dateFormat.format(calendar.time)
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )

        datePickerDialog.datePicker.minDate = System.currentTimeMillis() - 1000
        datePickerDialog.setOnShowListener{
            datePickerDialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(Color.BLUE)
            datePickerDialog.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(Color.GRAY)

        }
        datePickerDialog.show()
    }

    private fun visitClinic() {
        if (clinicId == null) {
            Toast.makeText(requireContext(), "Clinic ID is null", Toast.LENGTH_SHORT).show()
            return
        }
        if (petId == null) {
            Toast.makeText(requireContext(), "Pet ID is null", Toast.LENGTH_SHORT).show()
            return
        }
        if (appointmentDate == null) {
            Toast.makeText(requireContext(), "Appointment date is null", Toast.LENGTH_SHORT).show()
            return
        }
        sharedMenuActivityViewModel.addQueue(petId!!, clinicId!!, appointmentDate!!)
    }

    private fun selectPetsDialog() {
        // Initialize dialog
        val dialog = BottomSheetDialog(requireContext())
        val view = layoutInflater.inflate(R.layout.layout_bottom_sheet_select_pet_dialog, null, false)

        val rvSelectPet = view.findViewById<RecyclerView>(R.id.rvSelectPetForum)
        rvSelectPet.layoutManager = LinearLayoutManager(requireContext())
        rvSelectPet.adapter = petsSelectListAdapter

        dialog.setContentView(view)
        dialog.setCancelable(true)
        dialog.show()

        // Initialize petsSelectListAdapter with click listener and update selected pet
        petsSelectListAdapter = PetsSelectListAdapter(petsSelectList!!) { selectedPetName, selectedPetId ->
            petId = selectedPetId
            binding.tvPetUserSelect.text = selectedPetName
            binding.tvSelectPetsDialog.text = selectedPetName
            Toast.makeText(requireContext(), "Selected Pet ID: $petId", Toast.LENGTH_SHORT).show()
        }
        petsSelectListAdapter!!.notifyDataSetChanged()

    }
}
