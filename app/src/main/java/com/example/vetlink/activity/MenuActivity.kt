package com.example.vetlink.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.example.vetlink.R
import com.example.vetlink.databinding.ActivityMainBinding
import com.example.vetlink.databinding.ActivityMenuBinding
import com.example.vetlink.fragment.AccountFragment
import com.example.vetlink.fragment.ChangePasswordFragment
import com.example.vetlink.fragment.MyPetsFragment
import com.example.vetlink.fragment.ProfileFragment

class MenuActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMenuBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMenuBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        window.decorView.apply {
            systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_FULLSCREEN
        }

        initView()

    }

    @SuppressLint("MissingInflatedId")
    private fun initView(){
        var menuTitle = intent.getStringExtra("MENU_TITLE")
        binding.tvMenu.text = menuTitle ?: "Menu"

        if (menuTitle == "Account"){
            replaceFragment(AccountFragment())
        } else if (menuTitle == "Change Password"){
            replaceFragment(ChangePasswordFragment())
        }

        binding.ivBackMenu.setOnClickListener{
            finish()
        }

    }

    private fun replaceFragment(fragment: Fragment){

        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frame_layout_menu, fragment)
        fragmentTransaction.commit()
    }
}