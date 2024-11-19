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
import com.google.android.material.textfield.TextInputLayout
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

class SignupActivity : AppCompatActivity() {

    private var selectedImageUri: Uri? = null
    private lateinit var binding: ActivitySignupBinding
    private lateinit var session: SessionManager
    private lateinit var loadingAlert: LoadingAlert

    private var isFormValid = true

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

        registerViewModel.checkEmailResponse.observe(this) { checkEmailResponse ->
            loadingAlert.stopAlertDialog()
            if (checkEmailResponse.status == 200){
                if (checkEmailResponse.isExists == true){
                    binding.textInputLayoutEmail.error = "Email already exists"
                    binding.textInputLayoutEmail.setErrorTextColor(ColorStateList.valueOf(Color.RED))
                    binding.textInputLayoutEmail.setErrorIconTintList(ColorStateList.valueOf(Color.RED))
                    binding.etEmail.requestFocus()
                    isFormValid = false
                }else{
                    isFormValid = true
                    binding.textInputLayoutEmail.error = null
                    binding.textInputLayoutEmail.isErrorEnabled = false
                    // Set end icon mode to custom
                    binding.textInputLayoutEmail.endIconMode = TextInputLayout.END_ICON_CUSTOM

                    // Set a checkmark icon to indicate success
                    binding.textInputLayoutEmail.setEndIconDrawable(R.drawable.baseline_check_24) // Use your check icon drawable resource
                    binding.textInputLayoutEmail.setEndIconTintList(ColorStateList.valueOf(Color.GREEN)) // Set color for the checkmark
                }
            }
        }

        registerViewModel.checkUsernameResponse.observe(this){ checkUsernameResponse ->
            loadingAlert.stopAlertDialog()
            if (checkUsernameResponse.status == 200){
                if (checkUsernameResponse.isExists == true){
                    binding.textInputLayoutUsernameSign.error = "Username already exists"
                    binding.textInputLayoutUsernameSign.setErrorTextColor(ColorStateList.valueOf(Color.RED))
                    binding.textInputLayoutUsernameSign.setErrorIconTintList(ColorStateList.valueOf(Color.RED))
                    binding.etUsername.requestFocus()
                    isFormValid = false
                }else{
                    isFormValid = true
                    binding.textInputLayoutUsernameSign.error = null
                    binding.textInputLayoutUsernameSign.isErrorEnabled = false
                    // Set end icon mode to custom
                    binding.textInputLayoutUsernameSign.endIconMode = TextInputLayout.END_ICON_CUSTOM

                    // Set a checkmark icon to indicate success
                    binding.textInputLayoutUsernameSign.setEndIconDrawable(R.drawable.baseline_check_24) // Use your check icon drawable resource
                    binding.textInputLayoutUsernameSign.setEndIconTintList(ColorStateList.valueOf(Color.GREEN)) // Set color for the checkmark
                }
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

            etName.setOnFocusChangeListener{ _, hasFocus ->
                if (!hasFocus) {
                    val name = etName.text.toString()
                    if (name.isEmpty()) {
                        textInputLayoutNameSign.error = "Name is required"
                    } else if (name.length > 16){
                        textInputLayoutNameSign.error = "Name cannot be longer than 16 characters"
                    }
                    else{
                        textInputLayoutNameSign.error = null
                        textInputLayoutNameSign.isErrorEnabled = false
                    }
                }
            }

            etEmail.setOnFocusChangeListener{ _, hasFocus ->
                if (!hasFocus){
                    val email = etEmail.text.toString()
                    if (email.isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                        loadingAlert.startAlertDialog()
                        registerViewModel.checkEmail(email)
                    }else if (email.isEmpty()){
                        textInputLayoutEmail.error = "Email is required"
                    } else {
                        textInputLayoutEmail.error = "Invalid email format"
                    }
                }
            }

            etUsername.setOnFocusChangeListener { _, hasFocus ->
                if (!hasFocus) {
                    val username = etUsername.text.toString()
                    if (username.isNotEmpty()) {
                        loadingAlert.startAlertDialog()
                        registerViewModel.checkUsername(username)
                    }else if(username.isEmpty()){
                        textInputLayoutUsernameSign.error = "Username is required"
                    }
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

        isFormValid = true

        if (email.isNotEmpty()){
            binding.textInputLayoutNameSign.error = null
            binding.textInputLayoutNameSign.isErrorEnabled = false

            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                binding.textInputLayoutEmail.error = "Invalid Email"
                binding.textInputLayoutEmail.setErrorTextColor(colorError)
                binding.textInputLayoutEmail.setErrorIconTintList(colorError)
                isFormValid = false
            } else {
                binding.textInputLayoutNameSign.error = null
                binding.textInputLayoutNameSign.isErrorEnabled = false
            }

        }else{
            binding.textInputLayoutEmail.error = "Email is required"
            binding.textInputLayoutEmail.setErrorTextColor(colorError)
            binding.textInputLayoutEmail.setErrorIconTintList(colorError)
            isFormValid = false
        }

        if (username.isEmpty()){
            binding.textInputLayoutUsernameSign.error = "Username is required"
            binding.textInputLayoutUsernameSign.setErrorTextColor(colorError)
            binding.textInputLayoutUsernameSign.setErrorIconTintList(colorError)
            isFormValid = false
        } else {
            binding.textInputLayoutUsernameSign.error = null
            binding.textInputLayoutUsernameSign.isErrorEnabled = false
        }

        if (name.isEmpty()) {
            isFormValid = false
        } else if (name.length <= 3) {
            binding.textInputLayoutNameSign.error = "Name Must Be More Than 3 Characters"
            binding.textInputLayoutNameSign.setErrorTextColor(colorError)
            binding.textInputLayoutNameSign.setErrorIconTintList(colorError)
            isFormValid = false
        } else {
            binding.textInputLayoutNameSign.error = null
            binding.textInputLayoutNameSign.isErrorEnabled = false
        }

        if (phone.isEmpty()){
            binding.textInputLayoutPhone.error = "Phone Number is required"
            binding.textInputLayoutPhone.setErrorTextColor(colorError)
            binding.textInputLayoutPhone.setErrorIconTintList(colorError)
            isFormValid = false
        } else {
            binding.textInputLayoutPhone.error = null
            binding.textInputLayoutPhone.isErrorEnabled = false
        }

        if(password.isEmpty()){
            binding.textInputLayoutPassword.error = "Password is required"
            binding.textInputLayoutPassword.setErrorTextColor(colorError)
            binding.textInputLayoutPassword.setErrorIconTintList(colorError)
            isFormValid = false
        } else if(password.length < 8){
            binding.textInputLayoutPassword.error = "Password cannot be less than 8 characters"
            binding.textInputLayoutPassword.setErrorTextColor(colorError)
            binding.textInputLayoutPassword.setErrorIconTintList(colorError)
            isFormValid = false
        }else {
            binding.textInputLayoutPassword.error = null
            binding.textInputLayoutPassword.isErrorEnabled = false
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