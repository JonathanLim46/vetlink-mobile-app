package com.example.vetlink

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.vetlink.activity.LoginActivity
import com.example.vetlink.activity.MainActivity
import com.example.vetlink.data.network.AuthApi
import com.example.vetlink.databinding.FragmentProfileBinding
import com.example.vetlink.helper.Session
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class Profile : Fragment() {

    private lateinit var binding: FragmentProfileBinding
    private lateinit var session: Session
    private lateinit var authApi: AuthApi

    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Init session and AuthApi from MainActivity
        session = (activity as MainActivity).getSession()
        authApi = (activity as MainActivity).getAuthApi()

        // Inflate the layout using ViewBinding
        binding = FragmentProfileBinding.inflate(inflater, container, false)

        // Call initView
        initView()

        return binding.root
    }

    private fun initView() {
        // Access views via binding and set up your listeners
        with(binding) {
            btnLogout.setOnClickListener {
                showLogoutConfirmationDialog()
            }
        }
    }

    private fun showLogoutConfirmationDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle("Yakin Logout?")
            .setMessage("Anda yakin ingin logout?")
            .setPositiveButton("Ya") { dialog, which ->
                performLogout()
            }
            .setNegativeButton("Tidak") { dialog, which ->
                dialog.dismiss()
            }
            .show()
    }

    private fun performLogout() {
        val call = authApi.logout()
        call.enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    // Clear the session and navigate to LoginActivity
                    session.clearToken()
                    startActivity(Intent(activity, LoginActivity::class.java))
                    activity?.finish() // Close the current activity
                } else {
                    // Handle error response (e.g., show a message)
                    Toast.makeText(requireContext(), "Logout failed", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Toast.makeText(requireContext(), "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Profile().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
