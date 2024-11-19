package com.example.vetlink.fragment

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.Paint
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.vetlink.R
import com.example.vetlink.activity.SignupActivity
import com.example.vetlink.adapter.PetForumSelectAdapter
import com.example.vetlink.adapter.RecyclerViewClickListener
import com.example.vetlink.data.model.pets.Pet
import com.example.vetlink.databinding.FragmentForumFormBinding
import com.example.vetlink.viewModel.MenuActivityViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.squareup.picasso.Picasso
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class ForumFormFragment : Fragment(), RecyclerViewClickListener<Pet> {
    private lateinit var binding: FragmentForumFormBinding
    private val sharedmenuActivityViewModel: MenuActivityViewModel by activityViewModels()
    private lateinit var petAdapter: PetForumSelectAdapter

    private var imageUri: Uri? = null
    private var petImage: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentForumFormBinding.inflate(inflater, container, false)
        setupObservers()
        initView()

        return binding.root
    }

    private fun setupObservers() {
        // If you need any ViewModel observables, define them here
    }

    private fun initView() {
        with(binding) {
            // Underline the text for better UX
            tvAddPostImagePets.paintFlags = tvAddPostImagePets.paintFlags or Paint.UNDERLINE_TEXT_FLAG
            tvSelectPhotoOwnPets.paintFlags = tvSelectPhotoOwnPets.paintFlags or Paint.UNDERLINE_TEXT_FLAG

            // Description Box with TextWatcher
            etPostDescription.addTextChangedListener(object : TextWatcher {
                private var isLineBreakAllowed = false

                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                    // No action needed before text change
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    if (!isLineBreakAllowed && s?.contains("\n") == true) {
                        val newText = s.toString().replace("\n", "")
                        etPostDescription.setText(newText)
                        etPostDescription.setSelection(newText.length)
                    } else if (etPostDescription.layout != null) {
                        if (etPostDescription.layout.lineCount > 1 || (s?.length ?: 0) >= etPostDescription.width) {
                            isLineBreakAllowed = true
                        }
                    }
                }

                override fun afterTextChanged(s: Editable?) {
                    // No action needed after text change
                }
            })

            tvSelectPhotoOwnPets.setOnClickListener {
                selectPetsDialog()
            }

            tvAddPostImagePets.setOnClickListener {
                // Permission granted, open image picker
                openImageChooser()
            }

            btnSubmitPost.setOnClickListener {
                val title = etPostTitle.text
                val lastSeen = etPostLastSeen.text
                val characteristics = etPostCharacteristics.text
                val description = etPostDescription.text

                textInputLayoutPostTitle.error = null
                textInputLayoutPostTitle.isErrorEnabled = false

                if(title.toString().length > 32){
                    textInputLayoutPostTitle.isErrorEnabled = true
                    textInputLayoutPostTitle.error = "Title cannot exceed 36 characters"
                    textInputLayoutPostTitle.setErrorTextColor(ColorStateList.valueOf(Color.RED))
                    Toast.makeText(requireContext(), "Title cannot exceed 36 characters", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                if (title?.isNotEmpty() == true && lastSeen?.isNotEmpty() == true && characteristics?.isNotEmpty() == true && description?.isNotEmpty() == true) {
                    val params: MutableMap<String, RequestBody> = mutableMapOf(
                        "title" to title.toString().toRequestBody("text/plain".toMediaTypeOrNull()),
                        "last_seen" to lastSeen.toString().toRequestBody("text/plain".toMediaTypeOrNull()),
                        "characteristics" to characteristics.toString().toRequestBody("text/plain".toMediaTypeOrNull()),
                        "description" to description.toString().toRequestBody("text/plain".toMediaTypeOrNull())
                    )

                    var photoPart: MultipartBody.Part? = null

                    if (imageUri != null) {
                        try {
                            val inputStream = requireContext().contentResolver.openInputStream(imageUri!!)
                            val file = File.createTempFile("upload", ".jpg", requireContext().cacheDir)
                            file.outputStream().use { output ->
                                inputStream?.copyTo(output)
                            }

                            val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
                            photoPart = MultipartBody.Part.createFormData("pet_image_file", file.name, requestFile)

                        } catch (e: Exception) {
                            e.printStackTrace()
                            Toast.makeText(requireContext(), "Failed to prepare image for upload", Toast.LENGTH_SHORT).show()
                            return@setOnClickListener
                        }
                    } else if (petImage != null) {
                        // If user used an existing URL (string)
                        val petImageUrl = petImage
                        if (petImageUrl!!.isNotEmpty()) {
                            params["pet_image"] = petImageUrl.toRequestBody("text/plain".toMediaTypeOrNull())
                        }
                    }

                    Log.d("params", "$params")
                    // Call the ViewModel function to add a forum post
                    sharedmenuActivityViewModel.addForum(params, photoPart)
                    sharedmenuActivityViewModel.messageAddForum.observe(viewLifecycleOwner) {
                        if (it == 201) {
                            activity?.finish()
                            Toast.makeText(requireContext(), "Post added successfully", Toast.LENGTH_SHORT).show()
                        }else{
                            Toast.makeText(requireContext(), "Failed to add post", Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    Toast.makeText(requireContext(), "Please fill out all fields", Toast.LENGTH_SHORT).show()
                }
            }
        }
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
            imageUri = data?.data
            imageUri?.let { uri ->
                // Set the selected image URI to the ImageView
                petImage = null
                binding.ivPostImagePets.setImageURI(uri)
                binding.tvNullPostImagePets.visibility = View.GONE
                Log.d("imageUri", "$imageUri")
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == ForumFormFragment.REQUEST_CODE_IMAGE_PICKER) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, open the image chooser
                openImageChooser()
            } else {
                Toast.makeText(requireContext(),"Permission denied to access images.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private var dialog: BottomSheetDialog? = null

    private fun selectPetsDialog() {
        val activityContext = activity ?: return
        dialog = BottomSheetDialog(activityContext)
        val view = layoutInflater.inflate(R.layout.dialog_bottom_sheet_select_pet, null, false)

        val recyclerView = view.findViewById<RecyclerView>(R.id.rvSelectPetForum)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // Observe pets and set up the adapter
        sharedmenuActivityViewModel.pets.observe(viewLifecycleOwner) { pets ->
            petAdapter = pets.data?.let { PetForumSelectAdapter(it) }!!
            recyclerView.adapter = petAdapter
            petAdapter.setClickListener(this@ForumFormFragment)
        }

        dialog?.apply {
            setCancelable(true)
            setContentView(view)
            val bottomSheetBehavior = BottomSheetBehavior.from(view.parent as View)
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
            show()
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ForumFormFragment().apply {
                arguments = Bundle().apply {

                }
            }
        private const val REQUEST_CODE_IMAGE_PICKER = 102
    }

    override fun onItemClicke(view: View, item: Pet) {
        // Handle item click events here
        with(binding) {
            etPostTitle.setText("${item.pet_name} Hilang")
            Log.d("pet-image", "${item.photo}")
            petImage = item.photo
            Picasso.get().load(item.photo).into(ivPostImagePets)
            tvNullPostImagePets.visibility = View.GONE
            etPostLastSeen.setText("Hilang pada tanggal (tanggal), disetikar (kota)")
            etPostCharacteristics.setText("${item.type} dengan ras ${item.breed} dengan jenis kelamin ${item.gender}")
            imageUri = null
        }

        // Dismiss the dialog when an item is clicked
        dialog?.dismiss()
    }
}
