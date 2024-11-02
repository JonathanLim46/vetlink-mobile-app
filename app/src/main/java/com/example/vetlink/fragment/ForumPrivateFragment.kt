package com.example.vetlink.fragment

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toDrawable
import com.example.vetlink.R
import com.example.vetlink.databinding.FragmentForumPrivateBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ForumPrivateFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ForumPrivateFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var binding: FragmentForumPrivateBinding

    val mainButtonBackground = R.drawable.layout_button_main
    val mainTextColor = Color.parseColor("#FFFFFF")
    val secondButtonBackground = R.drawable.layout_button_second
    val secondTextColor = Color.parseColor("#B08BBB")

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
        binding = FragmentForumPrivateBinding.inflate(inflater, container, false)

        initView()

        return binding.root
    }


    private fun initView(){

        with(binding){



            btnAll.setBackgroundResource(mainButtonBackground)
            btnAll.setTextColor(mainTextColor)

            btnAll.setOnClickListener{
                buttonState(btnAll, listOf(btnDone, btnInProgress))
            }

            btnDone.setOnClickListener{
                buttonState(btnDone, listOf(btnAll, btnInProgress))
            }

            btnInProgress.setOnClickListener{
                buttonState(btnInProgress, listOf(btnAll, btnDone))
            }

        }

    }

    private fun buttonState(selectedButton: Button, otherButton: List<Button>){
        selectedButton.setBackgroundResource(mainButtonBackground)
        selectedButton.setTextColor(mainTextColor)

        for (button in otherButton){
            button.setBackgroundResource(secondButtonBackground)
            button.setTextColor(secondTextColor)
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ForumPrivateFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ForumPrivateFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}