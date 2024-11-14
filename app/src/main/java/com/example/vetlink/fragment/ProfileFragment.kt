package com.example.vetlink.fragment

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.SpannableString
import android.text.style.UnderlineSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.vetlink.R
import com.example.vetlink.Resource
import com.example.vetlink.activity.LoginActivity
import com.example.vetlink.activity.MenuActivity
import com.example.vetlink.databinding.FragmentProfileBinding
import com.example.vetlink.helper.SessionManager
import com.example.vetlink.viewModel.MainActivityViewModel
import com.squareup.picasso.Picasso

class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding
    private val sharedMainActivityViewModel: MainActivityViewModel by activityViewModels()
    private lateinit var session: SessionManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        session = SessionManager(requireContext())

        initView()
        setupObservers()

        return binding.root
    }

    private fun setupObservers() {
        // Observe user data from shared ViewModel
        sharedMainActivityViewModel.getUserHome.observe(viewLifecycleOwner) { resource ->

            when(resource) {
                is Resource.Loading -> {

                }
                is Resource.Success -> {
                    if (resource.data != null) {
                        binding.tvUsernameProfile.text = resource.data.name
                        val email = resource.data.email ?: "Email not available"

                        val spannableEmail = SpannableString(email).apply {
                            setSpan(UnderlineSpan(), 0, length, 0)
                        }
                        binding.tvEmailProfile.text = spannableEmail
                        if (resource.data.photo != null) {
                            Picasso.get().load(resource.data.photo).resize(50, 50).centerCrop().into(binding.ivPhotoProfile)
                        }else{
                            binding.ivPhotoProfile.setImageResource(R.drawable.img_default_profile)
                        }

                    } else {
                        binding.tvUsernameProfile.text = "User"
                        binding.tvEmailProfile.text = "No email available"
                    }
                }
                is Resource.Error -> {
                    Log.d("QueueObserver", "Error loading data: ${resource.message}")
                    Toast.makeText(requireContext(), "Failed to load profile, please try again.", Toast.LENGTH_SHORT).show()
                    binding.tvUsernameProfile.text = "User"
                    binding.tvEmailProfile.text = "No email available"
                }
            }

        }



        // Observe error messages
        sharedMainActivityViewModel.errorMessageUser.observe(viewLifecycleOwner) { errorMessage ->
            errorMessage?.let {
                Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
            }
        }

        sharedMainActivityViewModel.logoutSuccess.observe(viewLifecycleOwner) { success ->
            if (success){
                startActivity(Intent(activity, LoginActivity::class.java))
                activity?.finish()
            }else{
                Toast.makeText(requireContext(), "Logout failed. Try again later.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun initView() {
        with(binding) {
            val intent = Intent(activity, MenuActivity::class.java)

            ivAccount.setOnClickListener {
                intent.putExtra("MENU_TITLE", "Account")
                startActivity(intent)
            }

            myPetsMenu.setOnClickListener {
                intent.putExtra("MENU_TITLE", "My Pets")
                startActivity(intent)
            }

            scheduleMenu.setOnClickListener {
                intent.putExtra("MENU_TITLE", "Schedule")
                startActivity(intent)
            }

            faqMenu.setOnClickListener {
                intent.putExtra("MENU_TITLE", "FAQ VetLink")
                startActivity(intent)
            }

            btnLogout.setOnClickListener {
                val message = "Are you sure you want to log out? To access it again, please log back into your account."
                showLogoutConfirmationDialog(message)
            }
        }
    }

    private fun showLogoutConfirmationDialog(message: String?) {
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.layout_center_logout_dialog)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val tvDialogDescription: TextView = dialog.findViewById(R.id.tvDialogDescription)
        val btnYes: Button = dialog.findViewById(R.id.btnDialogLogout)
        val btnNo: Button = dialog.findViewById(R.id.btnDialogCancel)

        tvDialogDescription.text = message

        btnYes.setOnClickListener {
            sharedMainActivityViewModel.performLogout()
            dialog.dismiss()
        }
        btnNo.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }
}
