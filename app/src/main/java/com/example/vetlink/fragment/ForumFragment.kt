package com.example.vetlink.fragment

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.ViewPager
import com.example.vetlink.activity.MainActivity
import com.example.vetlink.activity.MenuActivity
import com.example.vetlink.adapter.ForumPageAdapter
import com.example.vetlink.databinding.FragmentForumBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.Tab
import com.google.android.material.tabs.TabLayoutMediator

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ForumFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ForumFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var binding: FragmentForumBinding

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
        binding = FragmentForumBinding.inflate(inflater, container, false)

        initView()

        return binding.root
    }

    private fun initView() {
        with(binding){

            setAdapter()

            ivAddPostForum.setOnClickListener{
                val intent = Intent(activity, MenuActivity::class.java)
                intent.putExtra("MENU_TITLE", "Postingan Baru")
                startActivity(intent)
            }
        }
    }

    private fun setAdapter(){
        val tabArrayList = arrayOf("All Board", "My Board")
        val adapter = ForumPageAdapter(requireActivity(), tabArrayList.size)
        binding.apply {
            viewPagerForum.adapter = adapter
            TabLayoutMediator(tabLayoutForum, viewPagerForum) {tab, position ->
                tab.text = tabArrayList[position]
            }.attach()

            tabLayoutForum.setSelectedTabIndicatorColor(Color.GRAY)
            tabLayoutForum.setBackgroundColor(Color.TRANSPARENT)
            tabLayoutForum.elevation = 0f
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment Forum.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ForumFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}