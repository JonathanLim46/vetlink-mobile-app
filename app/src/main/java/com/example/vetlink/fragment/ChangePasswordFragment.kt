package com.example.vetlink.fragment

import android.app.Dialog
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import com.example.vetlink.R
import com.example.vetlink.activity.LoginActivity
import com.example.vetlink.databinding.FragmentChangePasswordBinding
import com.example.vetlink.viewModel.MenuActivityViewModel
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ChangePasswordFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ChangePasswordFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var binding: FragmentChangePasswordBinding
    private val sharedMenuActivityViewModel: MenuActivityViewModel by activityViewModels()

    private val params = mutableMapOf<String, RequestBody>()


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
        // Inflate the layout for this fragment

        binding = FragmentChangePasswordBinding.inflate(inflater, container, false)

        initView()
        setupObservers()

        return binding.root
    }

    private fun setupObservers(){
        sharedMenuActivityViewModel.logoutSuccess.observe(viewLifecycleOwner) { success ->
            if (success){
                startActivity(Intent(activity, LoginActivity::class.java))
                activity?.finish()
            }else{
                Toast.makeText(requireContext(), "Logout failed. Try again later.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun initView(){

        with(binding){


            btnSaveNewPassword.setOnClickListener{
                validateNewPassword()
            }
        }
    }

    private fun validateNewPassword(){
        with(binding){
            val colorError = ColorStateList.valueOf(Color.RED)
            val newPWD = etChangePassword.text.toString()
            val updates = mutableMapOf<String, Any>()

            if (newPWD.isEmpty()){
                textInputLayoutNewPassword.error = "New Password is required"
                textInputLayoutNewPassword.setErrorTextColor(colorError)
                textInputLayoutNewPassword.setErrorIconTintList(colorError)
                etChangePassword.requestFocus()
            } else if (newPWD.length < 8){
                textInputLayoutNewPassword.error = "Password must be greater than or equal to 8 characters"
                textInputLayoutNewPassword.setErrorTextColor(colorError)
                textInputLayoutNewPassword.setErrorIconTintList(colorError)
                etChangePassword.requestFocus()
            } else{
                updates["password"] = etChangePassword.text.toString()
                textInputLayoutNewPassword.error = null
                textInputLayoutNewPassword.isErrorEnabled = false
            }

            if (updates.isNotEmpty()) {
                updates.forEach { (key, value) ->
                    val requestBody = value.toString().toRequestBody("text/plain".toMediaTypeOrNull())
                    params[key] = requestBody
                }
                alertDialog()
            }
        }
    }

    private fun alertDialog(){
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.dialog_center_logout)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val title: TextView = dialog.findViewById(R.id.tvDialogHeader)
        val tvDialogDescription: TextView = dialog.findViewById(R.id.tvDialogDescription)
        val btnYes: Button = dialog.findViewById(R.id.btnDialogLogout)
        val btnNo: Button = dialog.findViewById(R.id.btnDialogCancel)

        title.text = "Change Your Password"
        tvDialogDescription.text = "Are you sure you want to change your password? Your account will be automatically logged out and the application will ask you to log in again."
        btnYes.text = "Yes"
        btnNo.text = "No"

        btnYes.setOnClickListener{
            sharedMenuActivityViewModel.updateUser(params, photo = null)
            sharedMenuActivityViewModel.performLogout()
            dialog.dismiss()
        }

        btnNo.setOnClickListener{
            dialog.dismiss()
        }

        dialog.show()
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ChangePasswordFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ChangePasswordFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}