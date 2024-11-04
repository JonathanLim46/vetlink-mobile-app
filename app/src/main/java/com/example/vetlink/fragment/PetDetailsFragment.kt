package com.example.vetlink.fragment

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.vetlink.R
import com.example.vetlink.adapter.BreedListAdapter
import com.example.vetlink.adapter.PetTypeListAdapter
import com.example.vetlink.data.model.pets.PetBreed
import com.example.vetlink.data.model.pets.PetType
import com.example.vetlink.databinding.FragmentPetDetailsBinding
import com.example.vetlink.viewModel.MenuActivityViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog

private const val PET_ID = "PET_ID"
private const val METHOD = "METHOD"

class PetDetailsFragment : Fragment() {
    private var pet_id: Int? = null
    private var method: String? = null
    private lateinit var binding: FragmentPetDetailsBinding
    private val sharedMenuActivityViewModel: MenuActivityViewModel by activityViewModels()
    private var selectedImageUri: Uri? = null
    private var selectedPetType: PetType? = null
    private var selectedGender: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            pet_id = it.getInt(PET_ID)
            method = it.getString(METHOD)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPetDetailsBinding.inflate(inflater, container, false)
        initView()
        setupObservers()
        return binding.root
    }

    private fun setupObservers() {
        sharedMenuActivityViewModel.petTypes.observe(viewLifecycleOwner) { petTypes ->
            binding.btnChoosePet.setOnClickListener {
                showButtonSheetCategoryDialog(
                    title = "Choose Pet",
                    options = petTypes,
                    onSelect = { selectedPet ->
                        selectedPetType = selectedPet as? PetType // Cast selectedPet to PetType
                        binding.btnChoosePet.text = selectedPetType?.name // Access name property
                    }
                )
            }
        }
    }

    private fun initView() {
        with(binding) {



            btnMaleGender.setOnClickListener {
                selectGender("Male")
            }

            btnFemaleGender.setOnClickListener {
                selectGender("Female")
            }

            ivAddImagePets.setOnClickListener { openImageChooser() }

            btnChooseBreed.setOnClickListener {
                selectedPetType?.let { petType ->
                    showBreedSelectionDialog(petType)
                } ?: Toast.makeText(requireContext(), "Please choose a pet type first.", Toast.LENGTH_SHORT).show()
            }

            btnSubmitPets.setOnClickListener {
                val namePets = etNamePets.text.toString()
                val agePets = etAgePets.text.toString()
                val weightPets = etWeightPets.text.toString()
                val colorError = ColorStateList.valueOf(Color.RED)

                // Validation
                if (namePets.isEmpty()) {
                    textInputLayoutNamePets.error = "Name is required"
                    textInputLayoutNamePets.setErrorTextColor(colorError)
                    etNamePets.requestFocus()
                } else {
                    textInputLayoutNamePets.isErrorEnabled = false
                }

                if (agePets.isEmpty()) {
                    textInputLayoutAgePets.error = "Age is required"
                    textInputLayoutAgePets.setErrorTextColor(colorError)
                    etAgePets.requestFocus()
                } else {
                    textInputLayoutAgePets.isErrorEnabled = false
                }

                if (weightPets.isEmpty()) {
                    textInputLayoutWeightPets.error = "Weight is required"
                    textInputLayoutWeightPets.setErrorTextColor(colorError)
                    etWeightPets.requestFocus()
                } else {
                    textInputLayoutWeightPets.isErrorEnabled = false
                }

                if (namePets.isNotEmpty() && agePets.isNotEmpty() && weightPets.isNotEmpty()) {
                    Toast.makeText(requireContext(), "method: $method, id pet: $pet_id", Toast.LENGTH_SHORT).show()
                }
            }

            etNotePets.addTextChangedListener(object : TextWatcher {
                private var isLineBreakAllowed = false

                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    if (!isLineBreakAllowed && s?.contains("\n") == true) {
                        val newText = s.toString().replace("\n", "")
                        etNotePets.setText(newText)
                        etNotePets.setSelection(newText.length)
                    } else if (etNotePets.layout != null && etNotePets.layout.lineCount > 1) {
                        isLineBreakAllowed = true
                    }
                }

                override fun afterTextChanged(s: Editable?) {}
            })
        }
    }

    private fun selectGender(gender: String) {
        selectedGender = gender
        binding.btnMaleGender.isSelected = gender == "Male"
        binding.btnFemaleGender.isSelected = gender == "Female"

        // Optionally, you can change the background to reflect selection
        updateButtonBackgrounds()
        Log.d("PetDetailsFragment", "Selected Gender: $selectedGender")
    }

    private fun updateButtonBackgrounds() {
        if (binding.btnMaleGender.isSelected) {
            binding.btnMaleGender.setBackgroundResource(R.drawable.button_selector) // Selected background
        } else {
            binding.btnMaleGender.setBackgroundResource(R.drawable.outline_input) // Default background
        }

        if (binding.btnFemaleGender.isSelected) {
            binding.btnFemaleGender.setBackgroundResource(R.drawable.button_selector) // Selected background
        } else {
            binding.btnFemaleGender.setBackgroundResource(R.drawable.outline_input) // Default background
        }
    }

    @SuppressLint("InflateParams")
    private fun showButtonSheetCategoryDialog(
        title: String,
        options: List<Any>, // Change type to Any to accommodate both PetType and Breed
        onSelect: (Any) -> Unit
    ) {
        val dialog = activity?.let { BottomSheetDialog(it) }
        val view = layoutInflater.inflate(R.layout.layout_bottom_sheet_pet_dialog, null, false)
        val headerDialog = view.findViewById<TextView>(R.id.tvDialog)
        val recyclerView = view.findViewById<RecyclerView>(R.id.rvPetCategoryList)
        headerDialog.text = title
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        val adapter = if (options.isNotEmpty() && options[0] is PetType) {
            PetTypeListAdapter(options as List<PetType>) { selectedOption ->
                Log.d("PetDetailsFragment", "Selected Pet Type ID: ${(selectedOption as PetType).id}")
                onSelect(selectedOption)
                dialog?.dismiss()
            }
        } else {
            BreedListAdapter(options as List<PetBreed>) { selectedOption ->
                Log.d("PetDetailsFragment", "Selected Breed ID: ${(selectedOption as PetBreed).id}")
                onSelect(selectedOption)
                dialog?.dismiss()
            }
        }

        recyclerView.adapter = adapter

        dialog?.apply {
            setCancelable(true)
            setContentView(view)
            show()
            val bottomSheetBehavior = BottomSheetBehavior.from(view.parent as View)
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
        }
    }

    private fun showBreedSelectionDialog(petType: PetType?) {
        petType?.let {
            showButtonSheetCategoryDialog(
                title = "Choose Breed",
                options = it.breeds,
                onSelect = { selectedBreed ->
                    binding.btnChooseBreed.text = (selectedBreed as? PetBreed)?.breed_name // Access breed_name property
                }
            )
        } ?: Toast.makeText(requireContext(), "Please choose a pet type first.", Toast.LENGTH_SHORT).show()
    }

    private fun openImageChooser() {
        Intent(Intent.ACTION_PICK).also {
            it.type = "image/*"
            it.putExtra(Intent.EXTRA_MIME_TYPES, arrayOf("image/jpeg", "image/jpg", "image/png"))
            startActivityForResult(it, REQUEST_CODE_IMAGE_PICKER)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_IMAGE_PICKER && resultCode == Activity.RESULT_OK) {
            data?.data?.let { uri ->
                selectedImageUri = uri
                binding.ivAddImagePets.setImageURI(uri)
            }
        }
    }

    companion object {
        const val REQUEST_CODE_IMAGE_PICKER = 101
        @JvmStatic
        fun newInstance(pet_id: Int, method: String) = PetDetailsFragment().apply {
            arguments = Bundle().apply {
                putInt(PET_ID, pet_id)
                putString(METHOD, method)
            }
        }
    }
}
