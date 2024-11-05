package com.example.vetlink.activity

import android.app.Activity
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.util.Patterns
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.vetlink.R
import com.example.vetlink.databinding.ActivitySignupBinding
import com.example.vetlink.helper.SessionManager
import com.example.vetlink.repository.AuthRepository
import com.example.vetlink.util.toast
import com.example.vetlink.viewModel.RegisterActivityViewModel
import com.example.vetlink.viewModel.ViewModelFactory
import android.Manifest
import android.content.pm.PackageManager
import android.util.Log
import com.example.vetlink.LoadingAlert
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

class SignupActivity : AppCompatActivity() {

    private var selectedImageUri: Uri? = null
    private lateinit var binding: ActivitySignupBinding
    private lateinit var session: SessionManager
    private lateinit var loadingAlert: LoadingAlert

    private val registerViewModel: RegisterActivityViewModel by viewModels {
        ViewModelFactory(AuthRepository(session))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loadingAlert = LoadingAlert(this)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        session = SessionManager(this)

        registerViewModel.registerResponse.observe(this) { registerResponse ->
            registerResponse?.let {
                toast(registerResponse.message)
                finish()
            }
        }

        registerViewModel.errorMessage.observe(this) { message ->
            message?.let {
                loadingAlert.stopAlertDialog()
                toast(message)
            }
        }

        initView()
    }

    private fun initView() {
        with(binding) {
            ivBack.setOnClickListener {
                finish()
            }
            tvLogin.setOnClickListener {
                finish()
            }

            ivPhotoSignUp.setOnClickListener {
                // Check permissions before opening the image chooser
                if (checkSelfPermission(Manifest.permission.READ_MEDIA_IMAGES) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(arrayOf(Manifest.permission.READ_MEDIA_IMAGES), REQUEST_CODE_IMAGE_PICKER)
                } else {
                    openImageChooser()
                }
            }

            btnSignUp.setOnClickListener {
                validateFormAndRegister()
            }
        }
    }

    private fun validateFormAndRegister() {
        val name = binding.etName.text.toString()
        val username = binding.etUsername.text.toString()
        val email = binding.etEmail.text.toString()
        val phone = binding.etPhone.text.toString()
        val password = binding.etPassword.text.toString()
        val colorError = ColorStateList.valueOf(Color.RED)
        var isFormValid = true

        if (name.isEmpty()) {
            binding.textInputLayoutNameSign.error = "Name is required"
            binding.textInputLayoutNameSign.setErrorTextColor(colorError)
            binding.textInputLayoutNameSign.setErrorIconTintList(colorError)
            binding.etName.requestFocus()
            isFormValid = false
        } else if (name.length <= 3) {
            binding.textInputLayoutNameSign.error = "Name Must Be More Than 3 Characters"
            binding.textInputLayoutNameSign.setErrorTextColor(colorError)
            binding.textInputLayoutNameSign.setErrorIconTintList(colorError)
            binding.etName.requestFocus()
            isFormValid = false
        } else {
            binding.textInputLayoutNameSign.error = null
            binding.textInputLayoutNameSign.isErrorEnabled = false
        }

        if (isFormValid) {
            val photoPart: MultipartBody.Part? = selectedImageUri?.let { uri ->
                val filePath = getFilePathFromUri(uri) // Get the file path from URI
                val file = File(filePath) // Handle the file path correctly
                val requestFile = RequestBody.create("image/*".toMediaTypeOrNull(), file)
                MultipartBody.Part.createFormData("photo", file.name, requestFile)
            }

            loadingAlert.startAlertDialog()

            registerViewModel.registerUser(name, username, email, password, phone, photoPart)
        }
    }

    private fun getFilePathFromUri(uri: Uri): String? {
        val cursor = contentResolver.query(uri, arrayOf(android.provider.MediaStore.Images.Media.DATA), null, null, null)
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
            startActivityForResult(it, REQUEST_CODE_IMAGE_PICKER)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE_IMAGE_PICKER) {
            selectedImageUri = data?.data
            selectedImageUri?.let { uri ->
                // Set the selected image URI to the ImageView
                binding.ivPhotoSignUp.setImageURI(uri)
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_IMAGE_PICKER) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, open the image chooser
                openImageChooser()
            } else {
                toast("Permission denied to access images.")
            }
        }
    }

    companion object {
        const val REQUEST_CODE_IMAGE_PICKER = 101
    }
}