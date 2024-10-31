package com.example.vetlink.fragment

import android.graphics.Paint
import android.os.Bundle
import android.text.Editable
import android.text.SpannableString
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.vetlink.R
import com.example.vetlink.databinding.FragmentForumFormBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ForumFormFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ForumFormFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var binding : FragmentForumFormBinding

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
        binding = FragmentForumFormBinding.inflate(inflater, container, false)

        initView()

        return binding.root
    }

    private fun initView(){
        with(binding){
            tvAddPostImagePets.paintFlags = Paint.UNDERLINE_TEXT_FLAG
            tvSelectPhotoOwnPets.paintFlags = Paint.UNDERLINE_TEXT_FLAG

            // Description Box
            etPostDescription.addTextChangedListener(object : TextWatcher {
                private var isLineBreakAllowed = false

                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                    // No action needed before text change
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    // Check if the first line is complete
                    if (!isLineBreakAllowed && s?.contains("\n") == true) {
                        // If a line break is detected before the first line is complete, remove it
                        val newText = s.toString().replace("\n", "")
                        etPostDescription.setText(newText)
                        etPostDescription.setSelection(newText.length) // Move cursor to the end
                    } else if (etPostDescription.layout != null) {
                        // Check if the text exceeds the first line width
                        if (etPostDescription.layout.lineCount > 1 || (etPostDescription.text?.length
                                ?: 0) >= etPostDescription.width
                        ){
                            isLineBreakAllowed = true
                        }
                    }
                }

                override fun afterTextChanged(s: Editable?) {
                    // No action needed after text change
                }
            })


        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ForumFormFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ForumFormFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}