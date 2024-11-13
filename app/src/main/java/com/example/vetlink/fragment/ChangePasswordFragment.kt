package com.example.vetlink.fragment

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.example.vetlink.R
import com.example.vetlink.databinding.ActivityMenuBinding
import com.example.vetlink.databinding.FragmentChangePasswordBinding
import com.example.vetlink.viewModel.MenuActivityViewModel
import com.squareup.picasso.Picasso
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

        return binding.root
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
                val params = mutableMapOf<String, RequestBody>()

                updates.forEach { (key, value) ->
                    val requestBody = value.toString().toRequestBody("text/plain".toMediaTypeOrNull())
                    params[key] = requestBody
                }

                sharedMenuActivityViewModel.updateProfile(params, photo = null)
            }
        }
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