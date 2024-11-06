package com.example.vetlink.fragment

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.vetlink.R
import com.example.vetlink.activity.MenuActivity
import com.example.vetlink.adapter.PetsCategoryList
import com.example.vetlink.adapter.PetsCategoryListAdapter
import com.example.vetlink.adapter.PetsList
import com.example.vetlink.adapter.PetsListAdapter
import com.example.vetlink.adapter.RecyclerViewClickListener
import com.example.vetlink.data.model.pets.Pet
import com.example.vetlink.databinding.FragmentMyPetsBinding
import com.example.vetlink.viewModel.MenuActivityViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import org.w3c.dom.Text

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class MyPetsFragment : Fragment(),
    RecyclerViewClickListener<PetsList>,
    PetsCategoryListAdapter.OnItemClickListener {

    private var param1: String? = null
    private var param2: String? = null
    private lateinit var binding: FragmentMyPetsBinding
    private lateinit var petsList: ArrayList<PetsList>
    private lateinit var petsListAdapter: PetsListAdapter
    private var selectedCategory:String? = null
    private val sharedMenuActivityViewModel: MenuActivityViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onResume() {
        super.onResume()
        sharedMenuActivityViewModel.getPets() // Trigger data refresh
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMyPetsBinding.inflate(inflater, container, false)
        initView()
        setupObservers()
        return binding.root
    }

    @SuppressLint("MissingInflatedId", "UseCompatLoadingForDrawables")
    override fun onItemClicke(view: View, item: PetsList) {
        val dialog = activity?.let { BottomSheetDialog(it) }
        val viewLayout = layoutInflater.inflate(R.layout.layout_bottom_sheet_post_dialog, null, false)

        val firstLine = viewLayout.findViewById<TextView>(R.id.tvFirstLineDialog)
        val lineDone = viewLayout.findViewById<View>(R.id.lineDone)
        val secondLine = viewLayout.findViewById<TextView>(R.id.tvSecondLineDialog)
        val thirdLine = viewLayout.findViewById<TextView>(R.id.tvThirdLineDialog)

        firstLine.visibility = View.GONE
        lineDone.visibility = View.GONE

        secondLine.text = "Edit Pet"
        thirdLine.text = "Delete Pet"

        if (viewLayout.parent != null) {
            (viewLayout.parent as ViewGroup).removeView(viewLayout)
        }

        dialog?.apply {
            setCancelable(true)
            setContentView(viewLayout)

            //tombol edit
            secondLine.setOnClickListener{
                dialog.dismiss()
            }

            //tombol hapus
            thirdLine.setOnClickListener{
                dialog.dismiss()
                deletePet(item.petId)
            }

            show()

            val bottomSheetBehavior = BottomSheetBehavior.from(viewLayout.parent as View)
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
            bottomSheetBehavior.isHideable = true
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun initView() {
        with(binding) {
            rvMyPetsList.layoutManager = LinearLayoutManager(requireContext())
            petsList = ArrayList()
            petsListAdapter = PetsListAdapter(petsList)
            rvMyPetsList.adapter = petsListAdapter
            petsListAdapter.setClickListener(this@MyPetsFragment)

            srlPets.setOnRefreshListener {

                petsListAdapter.notifyDataSetChanged()
                srlPets.isRefreshing = false
            }

            tvAddPet.setOnClickListener {
                val intent = Intent(activity, MenuActivity::class.java)
                intent.putExtra("MENU_TITLE", "Pet Details")
                intent.putExtra("METHOD", "add")
                startActivity(intent)
            }

            tvFilter.setOnClickListener {
                showBottomSheetFilter()
            }
        }
    }

    private fun showBottomSheetFilter() {
        val dialog = activity?.let { BottomSheetDialog(it) }
        val viewFilter = layoutInflater.inflate(R.layout.layout_bottom_sheet_filter_dialog, null, false)

        val recyclerView = viewFilter.findViewById<RecyclerView>(R.id.rvFilterList)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // Create pets category list and adapter
        val petsCategoryList = ArrayList<PetsCategoryList>()
        sharedMenuActivityViewModel.pets.value?.let {
            addDataPetsCategoryToList(petsCategoryList, it)
        }

        val petsCategoryListAdapter = PetsCategoryListAdapter(petsCategoryList, this)
        recyclerView.adapter = petsCategoryListAdapter

        // Init reset button
        val buttonReset = viewFilter.findViewById<Button>(R.id.btnReset)
        buttonReset.setOnClickListener {
            selectedCategory = null // Clear selected category
            petsCategoryListAdapter.resetSelection() // Reset the adapter selection
        }

        val buttonApply = viewFilter.findViewById<Button>(R.id.btnApply)
        buttonApply.setOnClickListener {
            if (selectedCategory == null) {
                petsListAdapter.updateList(petsList)
            } else {
                val filteredList = petsList.filter { it.petType == selectedCategory }
                petsListAdapter.updateList(filteredList) // Filter by the selected category
            }
            dialog?.dismiss()
        }

        dialog?.apply {
            setCancelable(true)
            setContentView(viewFilter)
            show()
            val bottomSheetBehavior = BottomSheetBehavior.from(viewFilter.parent as View)
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
            bottomSheetBehavior.isHideable = true
        }
    }

    override fun onItemClick(item: PetsCategoryList) {
        selectedCategory = item.namePetsCategory
        Toast.makeText(requireContext(), selectedCategory, Toast.LENGTH_SHORT).show()
    }

    private fun setupObservers() {
        sharedMenuActivityViewModel.pets.observe(viewLifecycleOwner) { pets ->
            if (pets != null) {
                petsList.clear() // Clear the list before adding new data
                petsList.addAll(pets.map { pet ->
                    PetsList(pet.id, pet.photo, pet.type, pet.pet_name, pet.breed, pet.age.toString(), pet.weight, pet.gender)
                })
                binding.tvCountTotalPets.text = pets.size.toString()
                petsListAdapter.notifyDataSetChanged() // Notify the adapter to refresh
            }
        }

        sharedMenuActivityViewModel.petDetail.observe(viewLifecycleOwner){ pet ->
            if (pet != null) {
                val intent = Intent(activity, MenuActivity::class.java)
                intent.putExtra("MENU_TITLE", "Pet Details")
                intent.putExtra("METHOD", "edit")
                startActivity(intent)
            }else{ //if error was ocured
                Toast.makeText(requireContext(), "Terjadi Kesalahan!", Toast.LENGTH_SHORT).show()
            }
        }

        sharedMenuActivityViewModel.queues.observe(viewLifecycleOwner) { queues ->
            if (queues != null) {
                val ongoingCount = queues.count { it.status == "ongoing" }
                binding.tvCountScheduledVisit.text = ongoingCount.toString()
            } else {
                binding.tvCountScheduledVisit.text = "0"
            }
        }

        sharedMenuActivityViewModel.deletePetMessage.observe(viewLifecycleOwner){message ->
            if (message != null) {
                Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
                sharedMenuActivityViewModel.getPets()
            }
        }

        sharedMenuActivityViewModel._errorMessageDeletePet.observe(viewLifecycleOwner){ message ->
            if (message != null){
                Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun addDataPetsCategoryToList(petsCategory: ArrayList<PetsCategoryList>, pets: List<Pet>) {
        petsCategory.clear()
        val uniqueTypes = pets.map { it.type }.distinct()
        uniqueTypes.forEach { type ->
            petsCategory.add(PetsCategoryList(type))
        }
    }

    private fun deletePet(petId: Int){
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.layout_center_logout_dialog)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val tvDialogTitle: TextView = dialog.findViewById(R.id.tvDialogHeader)
        val tvDialogDescription: TextView = dialog.findViewById(R.id.tvDialogDescription)
        val btnYes: Button = dialog.findViewById(R.id.btnDialogLogout)
        val btnNo: Button = dialog.findViewById(R.id.btnDialogCancel)

        tvDialogTitle.text = "Delete this pet?"
        tvDialogDescription.text = "Are you sure you will permanently delete this pet."
        btnYes.text = "Delete"
        btnNo.text = "Cancel"

        btnYes.setOnClickListener {
            sharedMenuActivityViewModel.deletePet(petId)
            dialog.dismiss()
        }

        btnNo.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            MyPetsFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
