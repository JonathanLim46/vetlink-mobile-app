package com.example.vetlink.activity

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.util.Patterns
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.vetlink.R
import com.example.vetlink.databinding.ActivitySignupBinding

class SignupActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignupBinding

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

                if (name.isEmpty()){
                    textInputLayoutNameSign.error = "Name is required"
                    textInputLayoutNameSign.setErrorTextColor(colorError)
                    textInputLayoutNameSign.setErrorIconTintList(colorError)
                    etName.requestFocus()
                } else if (name.length <= 3){
                    textInputLayoutNameSign.error = "Name Must Be More Than 3 Characters"
                    textInputLayoutNameSign.setErrorTextColor(colorError)
                    textInputLayoutNameSign.setErrorIconTintList(colorError)
                    etName.requestFocus()
                } else {
                    textInputLayoutNameSign.error = null
                    textInputLayoutNameSign.isErrorEnabled = false
                }

                if (username.isEmpty()){
                    textInputLayoutUsernameSign.error = "Username is required"
                    textInputLayoutUsernameSign.setErrorTextColor(colorError)
                    textInputLayoutUsernameSign.setErrorIconTintList(colorError)
                    etUsername.requestFocus()
                } else if (username.length <= 3) {
                    textInputLayoutUsernameSign.error = "Username Must Be More Than 3 Characters"
                    textInputLayoutUsernameSign.setErrorTextColor(colorError)
                    textInputLayoutUsernameSign.setErrorIconTintList(colorError)
                    etUsername.requestFocus()
                } else {
                    textInputLayoutUsernameSign.error = null
                    textInputLayoutUsernameSign.isErrorEnabled = false
                }

                if (email.isEmpty()){
                    textInputLayoutEmail.error = "Email is required"
                    textInputLayoutEmail.setErrorTextColor(colorError)
                    textInputLayoutEmail.setErrorIconTintList(colorError)
                    etEmail.requestFocus()
                } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    textInputLayoutEmail.error = "Email is invalid"
                    textInputLayoutEmail.setErrorTextColor(colorError)
                    textInputLayoutEmail.setErrorIconTintList(colorError)
                    etEmail.requestFocus()
                } else {
                    textInputLayoutEmail.error = null
                    textInputLayoutEmail.isErrorEnabled = false
                }

                if (phone.isEmpty()){
                    textInputLayoutPhone.error = "Phone number is required"
                    textInputLayoutPhone.setErrorTextColor(colorError)
                    textInputLayoutPhone.setErrorIconTintList(colorError)
                    etPhone.requestFocus()
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
//                } else if (password.length < 3) {
//                    textInputLayoutPassword.error = "Password is must be more than 3 characters"
//                    textInputLayoutPassword.setErrorTextColor(colorError)
//                    textInputLayoutPassword.setErrorIconTintList(colorError)
//                    etPassword.requestFocus()
                } else {
                    textInputLayoutPassword.error = null
                    textInputLayoutPassword.isErrorEnabled = false
                }


            }
        }
    }
}