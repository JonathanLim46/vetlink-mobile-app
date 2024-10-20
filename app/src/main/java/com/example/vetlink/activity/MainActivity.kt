package com.example.vetlink.activity

import ClinicList
import ClinicListAdapter
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputBinding
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.vetlink.Clinic
import com.example.vetlink.Forum
import com.example.vetlink.Home
import com.example.vetlink.Profile
import com.example.vetlink.R
import com.example.vetlink.data.model.user.ProfileResponse
import com.example.vetlink.data.model.user.User
import com.example.vetlink.data.network.AuthApi
import com.example.vetlink.data.network.RetrofitInstance
import com.example.vetlink.data.network.UserApi
import com.example.vetlink.databinding.ActivityMainBinding
import com.example.vetlink.helper.Session
import com.google.android.material.bottomnavigation.BottomNavigationView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.create

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var authApi: AuthApi
    private lateinit var session: Session
    private lateinit var userApi: UserApi
    private var currentUser: User? = null


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)

        session = Session(this)
        authApi = RetrofitInstance.getRetrofit(session).create(AuthApi::class.java)
        userApi = RetrofitInstance.getRetrofit(session).create(UserApi::class.java)

        // This block will run after the data has been fetched
        enableEdgeToEdge()
        setContentView(binding.root)
        replaceFragment(Home()) // Now you can replace the fragment safely
        fetchData()


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        window.decorView.apply {
            systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_FULLSCREEN
        }

        // Fetch data and wait for completion




        binding.bottomNavigation.setOnItemSelectedListener {
            when(it.itemId) {
                R.id.homePage -> replaceFragment(Home())
                R.id.clinicPage -> replaceFragment(Clinic())
                R.id.forumPage -> replaceFragment(Forum())
                R.id.profilePage -> replaceFragment(Profile())


                else ->{

                }
            }
            true
        }
    }

    private fun initView(){
        with(binding){

        }
    }

    fun getSession(): Session{
        return session
    }

    fun getAuthApi(): AuthApi {
        return authApi
    }

    private fun replaceFragment(fragment : Fragment){

        val fragmentManager = supportFragmentManager
        val fragmentTransaction  = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frame_layout,fragment)
        fragmentTransaction.commit()
    }

    private fun fetchData(){
        val callUser = userApi.getProfile()
        callUser.enqueue(object: Callback<ProfileResponse> {
            override fun onResponse(call: Call<ProfileResponse>, response: Response<ProfileResponse>) {
                if (response.isSuccessful) {
                    // Since 'data' is not a list, treat it as a single user object
                    val user = response.body()?.data

                    if (user != null) {
                        currentUser = user
                        // Notify fragments about the user data change
                        updateFragments()
                    } else {
                        Toast.makeText(this@MainActivity, "Data pengguna tidak ditemukan", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this@MainActivity, "Terjadi error saat mengambil data", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ProfileResponse>, t: Throwable) {
                Toast.makeText(this@MainActivity, "Terjadi error saat mengambil data", Toast.LENGTH_SHORT).show()
                Log.e("API Error", t.message.toString())  // Log the error message for debugging
            }
        })
    }

    private fun updateFragments() {
        // Example: Iterate through the fragments and call an update method
        for (fragment in supportFragmentManager.fragments) {
            if (fragment is Home) {
                fragment.updateUserData(currentUser)
            }
            // Add other fragments as needed
        }
    }

    fun getCurrentUser(): User? {
        return currentUser
    }

    fun setCurrentUser(user: User) {
        currentUser = user
    }
}