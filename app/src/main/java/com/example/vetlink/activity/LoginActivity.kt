package com.example.vetlink.activity

import android.content.Intent
import android.os.Bundle
import android.provider.ContactsContract.CommonDataKinds.Email
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.vetlink.R
import com.example.vetlink.data.model.auth.LoginResponse
import com.example.vetlink.data.network.AuthApi
import com.example.vetlink.data.network.RetrofitInstance
import com.example.vetlink.databinding.ActivityLoginBinding
import com.example.vetlink.helper.Session
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    private lateinit var authApi: AuthApi
    private lateinit var session: Session


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

        session = Session(this)
        authApi = RetrofitInstance.getRetrofit(session).create(AuthApi::class.java)

        initView()
    }

    private fun initView() {
        with(binding){
            btnLogin.setOnClickListener{
                val email = etEmail.text.toString()
                val password = etPassword.text.toString()

                //check if email is empty
                if (email.isEmpty()){
                    etEmail.error = "Email is required"
                    etEmail.requestFocus()
                    return@setOnClickListener
                }
                //check if password is empty
                if(password.isEmpty()){
                    etPassword.error = "Password is required"
                    etPassword.requestFocus()
                    return@setOnClickListener
                }

                //check the email format
                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    // Email format is invalid
                    etEmail.error = "Email is invalid"
                    etEmail.requestFocus()
                    return@setOnClickListener
                }

                loginUser(email, password)

            }

            tvRegister.setOnClickListener{
                val intent = Intent(this@LoginActivity, SignupActivity::class.java)
                startActivity(intent)
            }
        }
    }

    private fun loginUser(email: String, password: String){
        val call  = authApi.login(email, password)
        call.enqueue(object: Callback<LoginResponse>{
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if (response.isSuccessful){
                    val loginResponse = response.body()
                    loginResponse.let {
                        if (it?.status == 200){
                            //store token
                            it.data.token.let { it1 -> session.setToken(it1) }

                            val user = it.data.user
                            val intent = Intent(this@LoginActivity, MainActivity::class.java)
                            intent.putExtra("user", user)
                            startActivity(intent)
                            finish()

                        }else{
                            Toast.makeText(this@LoginActivity, it?.message, Toast.LENGTH_SHORT).show()
                        }
                    }
                }else{
                    Toast.makeText(this@LoginActivity, "Login Failed", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                Toast.makeText(this@LoginActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    /*
    * TODO
    *  cari tau parceable
    *  kirim user yang aktif ke main activity
    *  cara dapet user ketika restart app atau pun paused
    * */
}