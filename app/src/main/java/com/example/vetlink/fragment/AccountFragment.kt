package com.example.vetlink.fragment

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.example.vetlink.R
import com.example.vetlink.activity.MenuActivity
import com.example.vetlink.data.model.user.User
import com.example.vetlink.databinding.FragmentAccountBinding
import com.example.vetlink.helper.Session
import com.example.vetlink.helper.SessionManager
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
 * Use the [AccountFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AccountFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var binding: FragmentAccountBinding
    private val sharedMenuActivityViewModel: MenuActivityViewModel by activityViewModels()
    private var currentUser: User? = null

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

        binding = FragmentAccountBinding.inflate(inflater, container, false)

        initView()
        setupObservers()

        return binding.root
    }

    private fun setupObservers(){
        sharedMenuActivityViewModel.user.observe(viewLifecycleOwner) { user ->
            if (user != null){
                with(binding){
                    if (user.photo != null){
                        Picasso.get().load(user.photo).resize(50,50).centerCrop().into(ivAddImage)
                    } else {
                        ivAddImage.setImageResource(R.drawable.img_default_profile)
                    }
                    etNameAcc.setText(user.name)
                    etPhoneAcc.setText(user.phone)
                    etEmailAcc.setText(user.email)

                    currentUser = user
                }
            }
        }
    }

    private fun initView(){
        with(binding){
            btnChangePassword.setOnClickListener{
                val intent = Intent(activity, MenuActivity::class.java)
                intent.putExtra("MENU_TITLE", "Change Password")
                startActivity(intent)
            }


            btnSubmitAccount.setOnClickListener{
                validateEdit()
            }
        }
    }

    private fun validateEdit(){

        with(binding) {
            val colorError = ColorStateList.valueOf(Color.RED)
            val user = currentUser ?: return
            val updates = mutableMapOf<String, Any>()

            if (etNameAcc.length() > 32) {
                textInputLayoutNameAcc.error = "Name cannot be longer than 32 characters"
                textInputLayoutNameAcc.setErrorTextColor(colorError)
                etNameAcc.requestFocus()
            } else if (etNameAcc.text.toString().isEmpty()) {
                textInputLayoutNameAcc.error = "Name is required"
                textInputLayoutNameAcc.setErrorTextColor(colorError)
                etNameAcc.requestFocus()
            } else if (etNameAcc.text.toString() != user.name) {
                updates["name"] = etNameAcc.text.toString()
                textInputLayoutNameAcc.error = null
                textInputLayoutNameAcc.isErrorEnabled = false
            } else {
                textInputLayoutNameAcc.error = null
                textInputLayoutNameAcc.isErrorEnabled = false
            }

            if (etPhoneAcc.text.toString().isEmpty()){
                textInputLayoutPhoneAcc.error = "Phone is required"
                textInputLayoutPhoneAcc.setErrorTextColor(colorError)
                etPhoneAcc.requestFocus()
            } else if (!Patterns.PHONE.matcher(etPhoneAcc.toString()).matches()){
                textInputLayoutPhoneAcc.error = "Phone is invalid"
                textInputLayoutPhoneAcc.setErrorTextColor(colorError)
                etPhoneAcc.requestFocus()
            } else if (etPhoneAcc.text.toString() != user.name) {
                updates["phone"] = etPhoneAcc.text.toString()
                textInputLayoutPhoneAcc.error = null
                textInputLayoutPhoneAcc.isErrorEnabled = false
            } else {
                textInputLayoutPhoneAcc.error = null
                textInputLayoutPhoneAcc.isErrorEnabled = false
            }

            if (updates.isNotEmpty()) {
                val params = mutableMapOf<String, RequestBody>()

                updates.forEach { (key, value) ->
                    val requestBody = value.toString().toRequestBody("text/plain".toMediaTypeOrNull())
                    params[key] = requestBody
                }

                // view model
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
         * @return A new instance of fragment AccountFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            AccountFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}