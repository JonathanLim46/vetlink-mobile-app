package com.example.vetlink.activity

import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.example.vetlink.R
import com.example.vetlink.data.model.user.User
import com.example.vetlink.databinding.ActivityMainBinding
import com.example.vetlink.fragment.ClinicFragment
import com.example.vetlink.fragment.ForumFragment
import com.example.vetlink.fragment.HomeFragment
import com.example.vetlink.fragment.ProfileFragment
import com.example.vetlink.helper.SessionManager
import com.example.vetlink.repository.AuthRepository
import com.example.vetlink.repository.ForumRepository
import com.example.vetlink.repository.PetRepository
import com.example.vetlink.repository.QueueRepository
import com.example.vetlink.repository.VeterinerRepository
import com.example.vetlink.viewModel.MainActivityViewModel
import com.example.vetlink.viewModel.ViewModelFactory

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var session: SessionManager

    private val mainActivityViewModel: MainActivityViewModel by viewModels {
        ViewModelFactory(AuthRepository(session), veterinerRepository = VeterinerRepository(session), queueRepository = QueueRepository(session), forumRepository = ForumRepository(session))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        session = SessionManager(this)

        enableEdgeToEdge()
        setupUI()
        mainActivityViewModel.fetchUser() // Fetch user data when activity is created
        mainActivityViewModel.getVeteriners()
        mainActivityViewModel.getQueues()
        mainActivityViewModel.getForums()
    }

    private fun setupUI() {
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        window.statusBarColor = ContextCompat.getColor(this, R.color.statusBar)


        window.decorView.apply {
            systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_FULLSCREEN
        }

        replaceFragment(HomeFragment()) // Start with HomeFragment

        binding.bottomNavigation.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.homePage -> replaceFragment(HomeFragment())
                R.id.clinicPage -> replaceFragment(ClinicFragment())
                R.id.forumPage -> replaceFragment(ForumFragment())
                R.id.profilePage -> replaceFragment(ProfileFragment())
                else -> false
            }
            true
        }

//        View More
        val viewMore = intent.getStringExtra("fragment")
        if (viewMore == "clinicFragment"){
            binding.bottomNavigation.selectedItemId = R.id.clinicPage
            replaceFragment(ClinicFragment())
        } else if (viewMore == "forumFragment"){
            binding.bottomNavigation.selectedItemId = R.id.forumPage
            replaceFragment(ForumFragment())
        }

        observeViewModel()
    }

    private fun observeViewModel() {
        // Observe error messages
        mainActivityViewModel.errorMessageUser.observe(this) { errorMessage ->
            errorMessage?.let {
                Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.frame_layout, fragment)
            .commit()
    }
}