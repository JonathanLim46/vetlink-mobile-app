package com.example.vetlink.activity

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.provider.ContactsContract.CommonDataKinds.Email
import android.text.TextWatcher
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
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

    private lateinit var authApi: AuthApi
    private lateinit var session: SessionManager

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

        session = SessionManager(this)
        loginViewModel.loginResponse.observe(this) { loginResponse ->
            loginResponse?.let {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                toast(loginResponse.data.token)
            }
        }

        loginViewModel.errorMessage.observe(this) { message ->
            message?.let {
                // Handle error
            }
        }

        authApi = RetrofitInstance.getRetrofit(session).create(AuthApi::class.java)

        initView()
    }

    private fun initView() {
        with(binding){

            btnLogin.setOnClickListener{
                val email = etEmail.text.toString()
                val password = etPassword.text.toString()

                val colorError = ColorStateList.valueOf(Color.RED)


                //check if email is empty
                if (email.isEmpty()){
                    textInputLayoutEmailLog.error = "Email is required"
                    textInputLayoutEmailLog.setErrorTextColor(colorError)
                    textInputLayoutEmailLog.setErrorIconTintList(colorError)
                    etEmail.requestFocus()
                    return@setOnClickListener
                } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    textInputLayoutEmailLog.error = "Email is invalid"
                    textInputLayoutEmailLog.setErrorTextColor(colorError)
                    textInputLayoutEmailLog.setErrorIconTintList(colorError)
                    etEmail.requestFocus()
                    return@setOnClickListener
                } else {
                    textInputLayoutEmailLog.error = null
                    textInputLayoutEmailLog.isErrorEnabled = false
                }

                //check if password is empty
                if(password.isEmpty()){
                    textInputLayoutPasswordLog.error = "Password is required"
                    textInputLayoutPasswordLog.setErrorTextColor(colorError)
                    textInputLayoutPasswordLog.setErrorIconTintList(colorError)
                    etPassword.requestFocus()
                    return@setOnClickListener
//                } else if (password.length < 7){
//                    textInputLayoutPasswordLog.error = "Password must be minimum 8 characters"
//                    textInputLayoutPasswordLog.setErrorTextColor(ColorStateList.valueOf(Color.RED))
//                    textInputLayoutPasswordLog.setErrorIconTintList(ColorStateList.valueOf(Color.RED))
//                    etPassword.requestFocus()
//                    return@setOnClickListener
                } else{
                    textInputLayoutPasswordLog.error = null
                    textInputLayoutPasswordLog.isErrorEnabled = false
                }

                loginViewModel.loginUser(email, password)
//                loginUser(email, password)

            }

            tvRegister.setOnClickListener{
                val intent = Intent(this@LoginActivity, SignupActivity::class.java)
                startActivity(intent)
            }
        }
    }

    private fun loginUser(email: String, password: String){
//        val call  = authApi.login(email, password)
//        call.enqueue(object: Callback<LoginResponse>{
//            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
//                if (response.isSuccessful){
//                    val loginResponse = response.body()
//                    loginResponse.let {
//                        if (it?.status == 200){
//                            //store token
//                            it.data.token.let { it1 -> session.setToken(it1) }
//
//                            val user = it.data.user
//                            val intent = Intent(this@LoginActivity, MainActivity::class.java)
//                            intent.putExtra("user", user)
//                            startActivity(intent)
//                            finish()
//
//                        }else{
//                            Toast.makeText(this@LoginActivity, it?.message, Toast.LENGTH_SHORT).show()
//                        }
//                    }
//                }else{
//                    Toast.makeText(this@LoginActivity, "Login Failed", Toast.LENGTH_SHORT).show()
//                }
//            }
//
//            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
//                Toast.makeText(this@LoginActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
//            }
//        })
    }

    /*
    * TODO
    *  cari tau parceable
    *  kirim user yang aktif ke main activity
    *  cara dapet user ketika restart app atau pun paused
    * */
}