package com.example.vetlink.activity

import ClinicList
import ClinicListAdapter
import android.os.Bundle
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
import com.example.vetlink.data.network.AuthApi
import com.example.vetlink.data.network.RetrofitInstance
import com.example.vetlink.databinding.ActivityMainBinding
import com.example.vetlink.helper.Session
import com.google.android.material.bottomnavigation.BottomNavigationView
import retrofit2.create

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var authApi: AuthApi
    private lateinit var session: Session



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)

        session = Session(this)
        authApi = RetrofitInstance.getRetrofit(session).create(AuthApi::class.java)

        enableEdgeToEdge()
        setContentView(binding.root)
        replaceFragment(Home())
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        window.decorView.apply {
            systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_FULLSCREEN
        }

        Toast.makeText(applicationContext, session.getToken().toString(), Toast.LENGTH_SHORT).show()

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

}