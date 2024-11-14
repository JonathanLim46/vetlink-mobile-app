package com.example.vetlink.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
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
import com.example.vetlink.helper.SessionManager
import com.example.vetlink.repository.AuthRepository
import com.example.vetlink.repository.ForumRepository
import com.example.vetlink.repository.PetRepository
import com.example.vetlink.repository.QueueRepository
import com.example.vetlink.repository.VeterinerRepository
import com.example.vetlink.util.toast
import com.example.vetlink.viewModel.MenuActivityViewModel
import com.example.vetlink.viewModel.ViewModelFactory

class MenuActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMenuBinding

    private lateinit var session: SessionManager

    private val menuActivityViewModel: MenuActivityViewModel by viewModels{
        ViewModelFactory(AuthRepository(session), PetRepository(session), QueueRepository(session), VeterinerRepository(session), ForumRepository(session))
    }

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

        session = SessionManager(this)

        initView()

        menuActivityViewModel.fetchProfile()

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
            menuActivityViewModel.getPets()
            menuActivityViewModel.getQueues()
            replaceFragmentWithOutIntance(MyPetsFragment())
        } else if (menuTitle == "Pet Details"){
            val petId = intent.getIntExtra("PET_ID", 0)
            val method = intent.getStringExtra("METHOD")
            menuActivityViewModel.getPetTypeBreed()
            val fragment = PetDetailsFragment().apply {
                arguments = Bundle().apply {
                    putInt("PET_ID", petId)
                    putString("METHOD", method)
                }
            }
            replaceFragmentWithOutIntance(fragment)
            if (method.equals("edit")){
                menuActivityViewModel.getPetDetails(petId)
                Log.d("PET_ID_Edit", petId.toString())
            }
        } else if (menuTitle == "Schedule"){
            menuActivityViewModel.getQueues()
            replaceFragmentWithOutIntance(ScheduleFragment())
        } else if(menuTitle == "FAQ VetLink"){
            replaceFragmentWithOutIntance(FaqFragment())
        } else if(menuTitle == "Clinic"){
            val clinicId = intent.getIntExtra("CLINIC_ID", 0)
            menuActivityViewModel.getDetailClinic(clinicId)
            menuActivityViewModel.getPets()
            replaceFragmentWithOutIntance(ClinicPageFragment())
        } else if(menuTitle == "Postingan Baru"){
            menuActivityViewModel.getPets()
            replaceFragmentWithOutIntance(ForumFormFragment())
        } else if(menuTitle == "Guide App"){
            val panduanFragment = FaqCategoryFragment.newInstance("Guide App")
            replaceFragment(panduanFragment)
        } else if(menuTitle == "Visit Cancellation"){
            val pembatalanFragment = FaqCategoryFragment.newInstance("Cancellation")
            replaceFragment(pembatalanFragment)
        } else if(menuTitle == "Loss Publications"){
            val publikasiFragment = FaqCategoryFragment.newInstance("Loss")
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