package com.example.vetlink.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.example.vetlink.R
import com.example.vetlink.databinding.ActivityMenuBinding
import com.example.vetlink.fragment.AccountFragment
import com.example.vetlink.fragment.ChangePasswordFragment
import com.example.vetlink.fragment.ClinicFragment
import com.example.vetlink.fragment.ClinicPageFragment
import com.example.vetlink.fragment.FaqCategoryFragment
import com.example.vetlink.fragment.FaqFragment
import com.example.vetlink.fragment.ForumFormFragment
import com.example.vetlink.fragment.MyPetsFragment
import com.example.vetlink.fragment.PetDetailsFragment
import com.example.vetlink.fragment.ScheduleFragment

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
        val menuTitle = intent.getStringExtra("MENU_TITLE")
        binding.tvMenu.text = menuTitle ?: "Menu"

        changeMenuView(menuTitle.toString())

        binding.ivBackMenu.setOnClickListener{
            finish()
        }

    }

    private fun changeMenuView(menuTitle: String){
        if (menuTitle == "Account"){
            replaceFragmentWithOutIntance(AccountFragment())
        } else if (menuTitle == "Change Password"){
            replaceFragmentWithOutIntance(ChangePasswordFragment())
        } else if (menuTitle == "My Pets"){
            replaceFragmentWithOutIntance(MyPetsFragment())
        } else if (menuTitle == "Pet Details"){
            replaceFragmentWithOutIntance(PetDetailsFragment())
        } else if (menuTitle == "Schedule"){
            replaceFragmentWithOutIntance(ScheduleFragment())
        } else if(menuTitle == "FAQ VetLink"){
            replaceFragmentWithOutIntance(FaqFragment())
        } else if(menuTitle == "Clinic"){
            replaceFragmentWithOutIntance(ClinicPageFragment())
        } else if(menuTitle == "Postingan Baru"){
            replaceFragmentWithOutIntance(ForumFormFragment())
        } else if(menuTitle == "Panduan Aplikasi"){
            val panduanFragment = FaqCategoryFragment.newInstance("Panduan")
            replaceFragment(panduanFragment)
        } else if(menuTitle == "FAQ Pembatalan"){
            val pembatalanFragment = FaqCategoryFragment.newInstance("Pembatalan")
            replaceFragment(pembatalanFragment)
        } else if(menuTitle == "FAQ Kehilangan"){
            val publikasiFragment = FaqCategoryFragment.newInstance("Publikasi")
            replaceFragment(publikasiFragment)
        }
    }

    private fun replaceFragmentWithOutIntance(fragment: Fragment){

        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frame_layout_menu, fragment)
        fragmentTransaction.commit()
    }

    private fun replaceFragment(fragment: FaqCategoryFragment){

        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frame_layout_menu, fragment)
        fragmentTransaction.commit()
    }
}