package com.example.vetlink.activity

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.util.Patterns
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.vetlink.R
import com.example.vetlink.databinding.ActivitySignupBinding
import com.example.vetlink.helper.SessionManager
import com.example.vetlink.repository.AuthRepository
import com.example.vetlink.util.toast
import com.example.vetlink.viewModel.RegisterActivityViewModel
import com.example.vetlink.viewModel.ViewModelFactory

class SignupActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignupBinding
    private lateinit var session: SessionManager

    private val registerViewModel: RegisterActivityViewModel by viewModels {
        ViewModelFactory(AuthRepository(session))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        session = SessionManager(this)

        registerViewModel.registerResponse.observe(this) { registerResponse ->
            registerResponse?.let { //this code meaning
                toast(registerResponse.message)
                finish()
            }
        }

        registerViewModel.errorMessage.observe(this){ message ->
            message?.let {
                toast(message)
            }
        }

        initView()
    }

    private fun initView() {
        with(binding){
            ivBack.setOnClickListener{
                finish()
            }
            tvLogin.setOnClickListener{
                finish()
            }

            btnSignUp.setOnClickListener{
                val name = etName.text.toString()
                val username = etUsername.text.toString()
                val email = etEmail.text.toString()
                val phone = etPhone.text.toString()
                val password = etPassword.text.toString()
                val colorError = ColorStateList.valueOf(Color.RED)
                var isFormValid = true

                if (name.isEmpty()){
                    textInputLayoutNameSign.error = "Name is required"
                    textInputLayoutNameSign.setErrorTextColor(colorError)
                    textInputLayoutNameSign.setErrorIconTintList(colorError)
                    etName.requestFocus()
                    isFormValid = false
                } else if (name.length <= 3){
                    textInputLayoutNameSign.error = "Name Must Be More Than 3 Characters"
                    textInputLayoutNameSign.setErrorTextColor(colorError)
                    textInputLayoutNameSign.setErrorIconTintList(colorError)
                    etName.requestFocus()
                    isFormValid = false
                } else {
                    textInputLayoutNameSign.error = null
                    textInputLayoutNameSign.isErrorEnabled = false
                }

                if (username.isEmpty()){
                    textInputLayoutUsernameSign.error = "Username is required"
                    textInputLayoutUsernameSign.setErrorTextColor(colorError)
                    textInputLayoutUsernameSign.setErrorIconTintList(colorError)
                    etUsername.requestFocus()
                    isFormValid = false
                } else if (username.length <= 3) {
                    textInputLayoutUsernameSign.error = "Username Must Be More Than 3 Characters"
                    textInputLayoutUsernameSign.setErrorTextColor(colorError)
                    textInputLayoutUsernameSign.setErrorIconTintList(colorError)
                    etUsername.requestFocus()
                    isFormValid = false
                } else {
                    textInputLayoutUsernameSign.error = null
                    textInputLayoutUsernameSign.isErrorEnabled = false
                }

                if (email.isEmpty()){
                    textInputLayoutEmail.error = "Email is required"
                    textInputLayoutEmail.setErrorTextColor(colorError)
                    textInputLayoutEmail.setErrorIconTintList(colorError)
                    etEmail.requestFocus()
                    isFormValid = false
                } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    textInputLayoutEmail.error = "Email is invalid"
                    textInputLayoutEmail.setErrorTextColor(colorError)
                    textInputLayoutEmail.setErrorIconTintList(colorError)
                    etEmail.requestFocus()
                    isFormValid = false
                } else {
                    textInputLayoutEmail.error = null
                    textInputLayoutEmail.isErrorEnabled = false
                }

                if (phone.isEmpty()){
                    textInputLayoutPhone.error = "Phone number is required"
                    textInputLayoutPhone.setErrorTextColor(colorError)
                    textInputLayoutPhone.setErrorIconTintList(colorError)
                    etPhone.requestFocus()
                    isFormValid = false
//                } else if (Patterns.PHONE.matcher(phone).matches()) {
//                    textInputLayoutPhone.error = "Phone number is invalid"
//                    textInputLayoutPhone.setErrorTextColor(colorError)
//                    textInputLayoutPhone.setErrorIconTintList(colorError)
//                    etPhone.requestFocus()
                } else {
                    textInputLayoutPhone.error = null
                    textInputLayoutPhone.isErrorEnabled = false
                }

                if (password.isEmpty()){
                    textInputLayoutPassword.error = "Password is required"
                    textInputLayoutPassword.setErrorTextColor(colorError)
                    textInputLayoutPassword.setErrorIconTintList(colorError)
                    etPassword.requestFocus()
                    isFormValid = false
//                } else if (password.length < 3) {
//                    textInputLayoutPassword.error = "Password is must be more than 3 characters"
//                    textInputLayoutPassword.setErrorTextColor(colorError)
//                    textInputLayoutPassword.setErrorIconTintList(colorError)
//                    etPassword.requestFocus()
                } else {
                    textInputLayoutPassword.error = null
                    textInputLayoutPassword.isErrorEnabled = false
                }

                if (isFormValid){
                    registerViewModel.registerUser(name, username, email, password, phone)
                }

            }
        }
    }
}