package com.example.vetlink.activity

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.provider.ContactsContract.CommonDataKinds.Email
import android.text.TextWatcher
import android.util.Log
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.vetlink.LoadingAlert
import com.example.vetlink.R
import com.example.vetlink.data.model.auth.LoginResponse
import com.example.vetlink.data.network.AuthApi
import com.example.vetlink.data.network.RetrofitInstance
import com.example.vetlink.databinding.ActivityLoginBinding
import com.example.vetlink.helper.Session
import com.example.vetlink.helper.SessionManager
import com.example.vetlink.repository.AuthRepository
import com.example.vetlink.util.toast
import com.example.vetlink.viewModel.LoginActivityViewModel
import com.example.vetlink.viewModel.ViewModelFactory
import com.google.android.material.textfield.TextInputLayout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    private lateinit var session: SessionManager

    private lateinit var loadingAlert: LoadingAlert


    private val loginViewModel: LoginActivityViewModel by viewModels {
        ViewModelFactory(AuthRepository(session))
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        loadingAlert = LoadingAlert(this)

        session = SessionManager(this)
        loginViewModel.loginResponse.observe(this) { loginResponse ->
            loginResponse?.let {
                if (loginResponse.status == 200) {
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    toast(loginResponse.data.token)
                    finish()
                }else{
                    loadingAlert.stopAlertDialog()
                    toast(loginResponse.message)
                }
            }
        }

        loginViewModel.errorMessage.observe(this) { message ->
            message?.let {
                loadingAlert.stopAlertDialog()
                toast(message)
            }
        }

        initView()
    }

    private fun initView() {
        with(binding){

            btnLogin.setOnClickListener{
                val identifier = etIdentifier.text.toString()
                val password = etPassword.text.toString()

                val colorError = ColorStateList.valueOf(Color.RED)


                //check if email is empty
                if (identifier.isEmpty()){
                    textInputLayoutIdentifierLog.error = "Email is required"
                    textInputLayoutIdentifierLog.setErrorTextColor(colorError)
                    textInputLayoutIdentifierLog.setErrorIconTintList(colorError)
                    etIdentifier.requestFocus()
                    return@setOnClickListener
                } else {
                    textInputLayoutIdentifierLog.error = null
                    textInputLayoutIdentifierLog.isErrorEnabled = false
                }

                //check if password is empty
                if(password.isEmpty()){
                    textInputLayoutPasswordLog.error = "Password is required"
                    textInputLayoutPasswordLog.setErrorTextColor(colorError)
                    textInputLayoutPasswordLog.setErrorIconTintList(colorError)
                    etPassword.requestFocus()
                    return@setOnClickListener
                } else{
                    textInputLayoutPasswordLog.error = null
                    textInputLayoutPasswordLog.isErrorEnabled = false
                }

                loadingAlert.startAlertDialog()
                loginViewModel.loginUser(identifier, password)

            }

            tvRegister.setOnClickListener{
                val intent = Intent(this@LoginActivity, SignupActivity::class.java)
                startActivity(intent)
            }
        }
    }


    /*
    * TODO
    *  cari tau parceable
    *  kirim user yang aktif ke main activity
    *  cara dapet user ketika restart app atau pun paused
    * */
}