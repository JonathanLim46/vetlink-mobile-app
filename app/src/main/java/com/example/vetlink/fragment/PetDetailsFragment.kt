package com.example.vetlink.fragment

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
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
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import okhttp3.MultipartBody
import com.example.vetlink.activity.SignupActivity
import com.example.vetlink.data.model.pets.PetDetails
import com.example.vetlink.viewModel.MenuActivityViewModel
import com.squareup.picasso.Picasso
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

private const val PET_ID = "PET_ID"
private const val METHOD = "METHOD"

class PetDetailsFragment : Fragment() {
    private var pet_id: Int? = null
    private var method: String? = null
    private lateinit var binding: FragmentPetDetailsBinding
    private val sharedMenuActivityViewModel: MenuActivityViewModel by activityViewModels()
    private var selectedImageUri: Uri? = null
    private var selectedPetType: PetType? = null
    private var selectedPetBreed: PetBreed? = null
    private var selectedGender: String? = null
    private var currentPet: PetDetails? = null

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
        sharedMenuActivityViewModel.addPetResponse.observe(viewLifecycleOwner){ addResponse ->
            if (addResponse?.status == 200){
                activity?.finish()
            }
        }
        sharedMenuActivityViewModel.petDetail.observe(viewLifecycleOwner){ petDetail ->
            if (petDetail != null){
                with(binding){
                    Picasso.get().load(petDetail.photo).centerCrop().fit().into(ivAddImagePets)
                    etNamePets.setText(petDetail.pet_name)
                    etAgePets.setText(petDetail.age.toString())
                    etWeightPets.setText(petDetail.weight)
                    btnChoosePet.text = petDetail.type
                    btnChooseBreed.text = petDetail.breed
                    selectGender(petDetail.gender)
                    etNotePets.setText(petDetail.notes)

                    currentPet = petDetail
                }
            }else{
                Toast.makeText(requireContext(), "An error was occurred!", Toast.LENGTH_SHORT).show()
            }
        }
        sharedMenuActivityViewModel.updateMessagePetUpdate.observe(viewLifecycleOwner){ updateStatus ->
            if (updateStatus != null){
                activity?.finish()
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
                if (method!!.equals("add")){ // add
                    validateAdd()
                }else{
                    validateEdit()
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

    private fun validateEdit() {
        with(binding) {
            val pet = currentPet ?: return

            val updates = mutableMapOf<String, Any>()
            var photoPart: MultipartBody.Part? = null

            // Handling the pet name
            if (etNamePets.text.toString() != pet.pet_name) {
                updates["pet_name"] = etNamePets.text.toString()
            }

            // Handling other fields like age, weight, etc.
            if (etAgePets.text.toString().toIntOrNull() != pet.age) {
                updates["age"] = etAgePets.text.toString().toIntOrNull() ?: pet.age
            }

            // Image handling
            selectedImageUri?.let {
                photoPart = handleImageUri(it)  // Handle the photo as a MultipartBody.Part
            }

            if (etWeightPets.text.toString() != pet.weight) {
                updates["weight"] = etWeightPets.text.toString()
            }

            // Pet Type and Breed handling
            if (btnChoosePet.text.toString() != pet.type) {
                updates["type"] = selectedPetType?.id.toString()  // Send PetType ID
            }

            if (btnChooseBreed.text.toString() != pet.breed) {
                updates["breed"] = selectedPetBreed?.id.toString()  // Send PetBreed ID
            }

            if (selectedGender != pet.gender) {
                updates["gender"] = selectedGender!!
            }

            if (etNotePets.text.toString().takeIf { it.isNotBlank() } != pet.notes?.takeIf { it.isNotBlank() }) {
                updates["notes"] = etNotePets.text.toString().ifEmpty { "" }
            }

            // Proceed with updating the pet if any fields have changed
            if (updates.isNotEmpty() || photoPart != null) {
                val params = mutableMapOf<String, RequestBody>()

                // Convert updates to RequestBody, excluding the photo field
                updates.forEach { (key, value) ->
                    val requestBody = value.toString().toRequestBody("text/plain".toMediaTypeOrNull())
                    params[key] = requestBody
                }

                sharedMenuActivityViewModel.updatePet(pet.id, params, photoPart)
            }
        }
    }

    private fun validateAdd() {
        with(binding){
            val namePets = etNamePets.text.toString()
            val agePets = etAgePets.text.toString()
            val weightPets = etWeightPets.text.toString()
            var petType = selectedPetType?.id
            val petBreed = selectedPetBreed?.id
            val genderPets = selectedGender
            val notePets = etNotePets.text.toString()
            val photoPet: MultipartBody.Part = selectedImageUri.let { uri ->
                val filePath = uri?.let { getFilePathFromUri(it) } // Get the file path from URI
                val file = File(filePath) // Handle the file path correctly
                val requestFile = RequestBody.create("image/*".toMediaTypeOrNull(), file)
                MultipartBody.Part.createFormData("photo", file.name, requestFile)
            }


            val colorError = ColorStateList.valueOf(Color.RED)
            var isFormAddValid = true

            // Validation
            if (namePets.isEmpty()) {
                textInputLayoutNamePets.error = "Name is required"
                textInputLayoutNamePets.setErrorTextColor(colorError)
                etNamePets.requestFocus()
                isFormAddValid = false
            } else {
                textInputLayoutNamePets.isErrorEnabled = false
            }

            if (agePets.isEmpty()) {
                textInputLayoutAgePets.error = "Age is required"
                textInputLayoutAgePets.setErrorTextColor(colorError)
                etAgePets.requestFocus()
                isFormAddValid = false
            } else {
                textInputLayoutAgePets.isErrorEnabled = false
            }

            if (weightPets.isEmpty()) {
                textInputLayoutWeightPets.error = "Weight is required"
                textInputLayoutWeightPets.setErrorTextColor(colorError)
                etWeightPets.requestFocus()
                isFormAddValid = false

            } else {
                textInputLayoutWeightPets.isErrorEnabled = false
            }

            if (petType == null) {
                Toast.makeText(requireContext(), "Please choose a pet type.", Toast.LENGTH_SHORT).show()
                isFormAddValid = false
            }

            if (petBreed == null) {
                Toast.makeText(requireContext(), "Please choose a pet breed.", Toast.LENGTH_SHORT).show()
                isFormAddValid = false
            }

            if (genderPets == null) {
                Toast.makeText(requireContext(), "Please select a gender.", Toast.LENGTH_SHORT).show()
                isFormAddValid = false
            }

            if (selectedImageUri == null) {
                Toast.makeText(requireContext(), "Please add an image.", Toast.LENGTH_SHORT).show()
                isFormAddValid = false
            }

            // If form is valid, display a toast with all parameters
            if (isFormAddValid) {
                sharedMenuActivityViewModel.addPet(namePets, petType.toString(), photoPet, petBreed.toString(), agePets, weightPets, genderPets!!, notePets)
            }
        }
    }

    private fun handleImageUri(uri: Uri): MultipartBody.Part? {
        val filePath = getFilePathFromUri(uri)
        filePath?.let {
            val file = File(filePath)
            val requestFile = RequestBody.create("image/*".toMediaTypeOrNull(), file)
            return MultipartBody.Part.createFormData("photo", file.name, requestFile)
        }
        return null
    }

    private fun getFilePathFromUri(uri: Uri): String? {
        val cursor = requireContext().contentResolver.query(uri, arrayOf(android.provider.MediaStore.Images.Media.DATA), null, null, null)
        return cursor?.use {
            if (it.moveToFirst()) {
                val columnIndex = it.getColumnIndex(android.provider.MediaStore.Images.Media.DATA)
                it.getString(columnIndex)
            } else {
                null
            }
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
        val view = layoutInflater.inflate(R.layout.dialog_bottom_sheet_pet, null, false)
        val headerDialog = view.findViewById<TextView>(R.id.tvDialog)
        val recyclerView = view.findViewById<RecyclerView>(R.id.rvPetCategoryList)
        headerDialog.text = title
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        val adapter = if (options.isNotEmpty() && options[0] is PetType) {
            PetTypeListAdapter(options as List<PetType>) { selectedOption ->
                Log.d("PetDetailsFragment", "Selected Pet Type ID: ${(selectedOption as PetType).id}")
                selectedPetType = selectedOption
                selectedPetBreed = null // Reset breed selection
                onSelect(selectedOption)
                dialog?.dismiss()
            }
        } else {
            BreedListAdapter(options as List<PetBreed>) { selectedOption ->
                Log.d("PetDetailsFragment", "Selected Breed ID: ${(selectedOption as PetBreed).id}")
                selectedPetBreed = selectedOption
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
            startActivityForResult(it, SignupActivity.REQUEST_CODE_IMAGE_PICKER)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == SignupActivity.REQUEST_CODE_IMAGE_PICKER) {
            selectedImageUri = data?.data
            selectedImageUri?.let { uri ->
                // Set the selected image URI to the ImageView
                binding.ivAddImagePets.setImageURI(uri)
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == SignupActivity.REQUEST_CODE_IMAGE_PICKER) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, open the image chooser
                openImageChooser()
            } else {
                Toast.makeText(requireContext(),"Permission denied to access images.", Toast.LENGTH_SHORT).show()
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