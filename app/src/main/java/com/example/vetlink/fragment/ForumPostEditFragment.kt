package com.example.vetlink.fragment

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import com.example.vetlink.R
import com.example.vetlink.activity.SignupActivity
import com.example.vetlink.data.model.forums.Forum
import com.example.vetlink.databinding.FragmentForumPostEditBinding
import com.example.vetlink.viewModel.MainActivityViewModel
import com.squareup.picasso.Picasso
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.net.URI

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

/**
 * A simple [Fragment] subclass.
 * Use the [ForumPostEditFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ForumPostEditFragment : Fragment() {
    private var forum: Forum? = null
    private var petImageUri: Uri? = null
    private val sharedMainActivityViewModel: MainActivityViewModel by activityViewModels()

    private lateinit var binding: FragmentForumPostEditBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment


        binding = FragmentForumPostEditBinding.inflate(inflater, container, false)

        initViews()
        setupObserver()

        return binding.root
    }

    private fun setupObserver() {
        sharedMainActivityViewModel.forum.observe(viewLifecycleOwner) { forum ->
            this.forum = forum
            Log.d("currentForum", "$forum")
        }
    }


    private fun initViews() {
        with(binding) {
            sharedMainActivityViewModel.forum.observe(viewLifecycleOwner) { forum ->

                etPostTitle.setText(forum.title)
                etPostLastSeen.setText(forum.last_seen)
                etPostCharacteristics.setText(forum.characteristics)
                etPostDescription.setText(forum.description)
                tvNullPostImagePets.visibility = View.GONE
                Picasso.get().load(forum.pet_image).into(ivPostImagePets)
            }

            ivPostImagePets.setOnClickListener {
                openImageChooser()
            }

            btnSubmitPost.setOnClickListener {
                val updates = mutableMapOf<String, Any>()
                var photoPart: MultipartBody.Part? = null

                if (etPostTitle.text.toString().takeIf { it.isNotBlank() } != forum!!.title?.takeIf { it.isNotBlank() }) {
                    updates["title"] = etPostTitle.text.toString().ifEmpty { "" }
                }

                if (etPostLastSeen.text.toString().takeIf { it.isNotBlank() } != forum!!.last_seen?.takeIf { it.isNotBlank() }){
                    updates["last_seen"] = etPostLastSeen.text.toString().ifEmpty { "" }
                }

                if (etPostCharacteristics.text.toString().takeIf { it.isNotBlank() } != forum!!.characteristics?.takeIf { it.isNotBlank() }){
                    updates["characteristics"] = etPostCharacteristics.text.toString().ifEmpty { "" }
                }

                if (etPostDescription.text.toString().takeIf { it.isNotBlank() } != forum!!.description?.takeIf { it.isNotBlank() }){
                    updates["description"] = etPostDescription.text.toString().ifEmpty { "" }
                }

                // Image handling
                petImageUri?.let {
                    photoPart = handleImageUri(it)  // Handle the photo as a MultipartBody.Part
                }

                if (updates.isNotEmpty() || photoPart != null) {
                    val params = mutableMapOf<String, RequestBody>()

                    // Convert updates to RequestBody, excluding the photo field
                    updates.forEach { (key, value) ->
                        val requestBody = value.toString().toRequestBody("text/plain".toMediaTypeOrNull())
                        params[key] = requestBody
                    }

//                    Log.d("params", "$params $photoPart")
                    sharedMainActivityViewModel.updateForum(forum!!.id, params, photoPart)
                    sharedMainActivityViewModel.updateForumPostStatus.observe(viewLifecycleOwner) {
                        if (it == 200) {
                            Toast.makeText(requireContext(), "Post updated successfully", Toast.LENGTH_SHORT).show()
                            requireActivity().supportFragmentManager.popBackStack()
                        } else if (it == -1) {
                            Toast.makeText(requireContext(), "Failed to update post. Please try again.", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }
    }

    private fun handleImageUri(uri: Uri): MultipartBody.Part? {
        val filePath = getFilePathFromUri(uri)
        filePath?.let {
            val file = File(filePath)
            val requestFile = RequestBody.create("image/*".toMediaTypeOrNull(), file)
            return MultipartBody.Part.createFormData("pet_image_file", file.name, requestFile)
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
            petImageUri = data?.data
            petImageUri?.let { uri ->
                // Set the selected image URI to the ImageView
                binding.ivPostImagePets.setImageURI(uri)
                binding.tvNullPostImagePets.visibility = View.GONE
                Log.d("imageUri", "$petImageUri")
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == ForumPostEditFragment.REQUEST_CODE_IMAGE_PICKER) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, open the image chooser
                openImageChooser()
            } else {
                Toast.makeText(requireContext(),"Permission denied to access images.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ForumPostEditFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance() =
            ForumPostEditFragment().apply {
                arguments = Bundle().apply {

                }
            }
        private const val REQUEST_CODE_IMAGE_PICKER = 102
    }
}