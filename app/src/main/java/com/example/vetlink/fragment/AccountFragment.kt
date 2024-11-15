package com.example.vetlink.fragment

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.ColorStateList
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import com.example.vetlink.R
import com.example.vetlink.Resource
import com.example.vetlink.activity.MenuActivity
import com.example.vetlink.activity.SignupActivity
import com.example.vetlink.data.model.user.User
import com.example.vetlink.databinding.FragmentAccountBinding
import com.example.vetlink.helper.Session
import com.example.vetlink.helper.SessionManager
import com.example.vetlink.viewModel.MenuActivityViewModel
import com.squareup.picasso.Picasso
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [AccountFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AccountFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var binding: FragmentAccountBinding
    private val sharedMenuActivityViewModel: MenuActivityViewModel by activityViewModels()
    private var currentUser: User? = null

    private var selectedImageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        binding = FragmentAccountBinding.inflate(inflater, container, false)

        initView()
        setupObservers()

        return binding.root
    }

    private fun setupObservers(){
        sharedMenuActivityViewModel.user.observe(viewLifecycleOwner) { resource ->

            when(resource){
                is Resource.Loading ->{
                    binding.shimmerAccount.startShimmer()
                }
                is Resource.Success ->{

                    showAccount()
                    if (resource.data != null){
                        with(binding){
                            if (resource.data.photo != null){
                                Picasso.get().load(resource.data.photo).resize(50,50).centerCrop().into(layoutAccount.ivAddImage)
                            } else {
                                layoutAccount.ivAddImage.setImageResource(R.drawable.img_default_profile)
                            }
                            layoutAccount.etNameAcc.setText(resource.data.name)
                            layoutAccount.etPhoneAcc.setText(resource.data.phone)
                            layoutAccount.etEmailAcc.setText(resource.data.email)

                            currentUser = resource.data
                        }
                    }

                }
                is Resource.Error ->{
                    Log.d("QueueObserver", "Error loading data: ${resource.message}")
                    binding.shimmerAccount.hideShimmer()
                    Toast.makeText(context, "Error: ${resource.message}", Toast.LENGTH_SHORT).show()
                }
            }

        }
    }

    private fun initView(){
        with(binding){
            layoutAccount.btnChangePassword.setOnClickListener{
                val intent = Intent(activity, MenuActivity::class.java)
                intent.putExtra("MENU_TITLE", "Change Password")
                startActivity(intent)
            }


            layoutAccount.btnSubmitAccount.setOnClickListener{
                validateEdit()
                activity?.finish()
            }

            layoutAccount.ivAddImage.setOnClickListener { openImageChooser() }
        }
    }

    private fun validateEdit(){

        with(binding) {
            val colorError = ColorStateList.valueOf(Color.RED)
            val user = currentUser ?: return
            val updates = mutableMapOf<String, Any>()
            var photo: MultipartBody.Part? = null

            selectedImageUri?.let {
                photo = handleImageUri(it)
            }

            if (layoutAccount.etNameAcc.length() > 32) {
                layoutAccount.textInputLayoutNameAcc.error = "Name cannot be longer than 32 characters"
                layoutAccount.textInputLayoutNameAcc.setErrorTextColor(colorError)
                layoutAccount.etNameAcc.requestFocus()
            } else if (layoutAccount.etNameAcc.text.toString().isEmpty()) {
                layoutAccount.textInputLayoutNameAcc.error = "Name is required"
                layoutAccount.textInputLayoutNameAcc.setErrorTextColor(colorError)
                layoutAccount.etNameAcc.requestFocus()
            } else if (layoutAccount.etNameAcc.text.toString() != user.name) {
                updates["name"] = layoutAccount.etNameAcc.text.toString()
                layoutAccount.textInputLayoutNameAcc.error = null
                layoutAccount.textInputLayoutNameAcc.isErrorEnabled = false
            } else {
                layoutAccount.textInputLayoutNameAcc.error = null
                layoutAccount.textInputLayoutNameAcc.isErrorEnabled = false
            }

            if (layoutAccount.etPhoneAcc.text.toString().isEmpty()){
                layoutAccount.textInputLayoutPhoneAcc.error = "Phone is required"
                layoutAccount.textInputLayoutPhoneAcc.setErrorTextColor(colorError)
                layoutAccount.etPhoneAcc.requestFocus()
            } else if (!Patterns.PHONE.matcher(layoutAccount.etPhoneAcc.toString()).matches()){
                layoutAccount.textInputLayoutPhoneAcc.error = "Phone is invalid"
                layoutAccount.textInputLayoutPhoneAcc.setErrorTextColor(colorError)
                layoutAccount.etPhoneAcc.requestFocus()
            } else if (layoutAccount.etPhoneAcc.text.toString() != user.name) {
                updates["phone"] = layoutAccount.etPhoneAcc.text.toString()
                layoutAccount.textInputLayoutPhoneAcc.error = null
                layoutAccount.textInputLayoutPhoneAcc.isErrorEnabled = false
            } else {
                layoutAccount.textInputLayoutPhoneAcc.error = null
                layoutAccount.textInputLayoutPhoneAcc.isErrorEnabled = false
            }

            if (updates.isNotEmpty() || photo != null) {
                val params = mutableMapOf<String, RequestBody>()

                updates.forEach { (key, value) ->
                    val requestBody = value.toString().toRequestBody("text/plain".toMediaTypeOrNull())
                    params[key] = requestBody
                }

                sharedMenuActivityViewModel.updateUser(params, photo)
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

    private fun openImageChooser(){
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
                binding.layoutAccount.ivAddImage.setImageURI(uri)
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

    private fun showAccount(){
        binding.shimmerAccount.apply {
            stopShimmer()
            visibility = View.GONE
        }
        binding.layoutAccount.root.visibility = View.VISIBLE
    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment AccountFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            AccountFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}