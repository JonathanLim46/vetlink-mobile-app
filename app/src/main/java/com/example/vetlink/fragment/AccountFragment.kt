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
import com.example.vetlink.R
import com.example.vetlink.activity.MenuActivity
import com.example.vetlink.databinding.FragmentAccountBinding

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

        return binding.root
    }

    private fun initView(){
        with(binding){
            val colorError = ColorStateList.valueOf(Color.RED)

            btnChangePassword.setOnClickListener{
                val intent = Intent(activity, MenuActivity::class.java)
                intent.putExtra("MENU_TITLE", "Change Password")
                startActivity(intent)
            }

            btnSubmitAccount.setOnClickListener{
                val nameAcc = etNameAcc.text.toString()
                val emailAcc = etEmailAcc.text.toString()
                val phoneAcc = etPhoneAcc.text.toString()

                if(nameAcc.isEmpty()){
                    textInputLayoutNameAcc.error = "Name is required"
                    textInputLayoutNameAcc.setErrorTextColor(colorError)
                    textInputLayoutNameAcc.setErrorIconTintList(colorError)
                    etNameAcc.requestFocus()
                } else if (nameAcc.length <= 3){
                    textInputLayoutNameAcc.error = "Name must be more than 3 characters"
                    textInputLayoutNameAcc.setErrorTextColor(colorError)
                    textInputLayoutNameAcc.setErrorIconTintList(colorError)
                    etNameAcc.requestFocus()
                } else {
                    textInputLayoutNameAcc.error = null
                    textInputLayoutNameAcc.isErrorEnabled = false
                }

                if(emailAcc.isEmpty()){
                    textInputLayoutEmailAcc.error = "Email is required"
                    textInputLayoutEmailAcc.setErrorTextColor(colorError)
                    textInputLayoutEmailAcc.setErrorIconTintList(colorError)
                    etEmailAcc.requestFocus()
                } else if (!Patterns.EMAIL_ADDRESS.matcher(emailAcc).matches()){
                    textInputLayoutEmailAcc.error = "Email is invalid"
                    textInputLayoutEmailAcc.setErrorTextColor(colorError)
                    textInputLayoutEmailAcc.setErrorIconTintList(colorError)
                    etEmailAcc.requestFocus()
                } else {
                    textInputLayoutEmailAcc.error = null
                    textInputLayoutEmailAcc.isErrorEnabled = false
                }

                if(phoneAcc.isEmpty()){
                    textInputLayoutPhoneAcc.error = "Phone is required"
                    textInputLayoutPhoneAcc.setErrorTextColor(colorError)
                    textInputLayoutPhoneAcc.setErrorIconTintList(colorError)
                    etPhoneAcc.requestFocus()
//                } else if (!Patterns.EMAIL_ADDRESS.matcher(emailAcc).matches()){
//                    textInputLayoutPhoneAcc.error = "Email is invalid"
//                    textInputLayoutPhoneAcc.setErrorTextColor(colorError)
//                    textInputLayoutPhoneAcc.setErrorIconTintList(colorError)
//                    etPhoneAcc.requestFocus()
                } else {
                    textInputLayoutPhoneAcc.error = null
                    textInputLayoutPhoneAcc.isErrorEnabled = false
                }

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