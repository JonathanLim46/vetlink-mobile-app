package com.example.vetlink.fragment

import android.app.Activity
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.viewpager.widget.ViewPager
import com.example.vetlink.Resource
import com.example.vetlink.activity.MainActivity
import com.example.vetlink.activity.MenuActivity
import com.example.vetlink.adapter.ForumPageAdapter
import com.example.vetlink.data.model.forums.Forum
import com.example.vetlink.databinding.FragmentForumBinding
import com.example.vetlink.viewModel.MainActivityViewModel
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.Tab
import com.google.android.material.tabs.TabLayoutMediator
import com.squareup.picasso.MemoryPolicy
import com.squareup.picasso.NetworkPolicy
import com.squareup.picasso.Picasso

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

    var userForum: List<Forum>? = null
    var postForums: List<Forum>? = null

    private lateinit var binding: FragmentForumBinding

    private val sharedMainActivityViewModel: MainActivityViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onResume() {
        super.onResume()
        sharedMainActivityViewModel.getForums() // Trigger data refresh
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentForumBinding.inflate(inflater, container, false)


        initView()
        setupObserver()
        return binding.root
    }

    private fun setupObserver() {
        sharedMainActivityViewModel.getUserHome.observe(viewLifecycleOwner){ resource ->

            when(resource) {
                is Resource.Loading -> {

                }
                is Resource.Success -> {
                    binding.apply {
                        if (resource.data != null){
                            tvMessageHeader.text = "Hello, ${resource.data.name}"
                        }
                    }
                }
                is Resource.Error -> {
                    Log.d("QueueObserver", "Error loading data: ${resource.message}")
                    Toast.makeText(requireContext(), "Failed to load name, please try again.", Toast.LENGTH_SHORT).show()
                }
            }

        }

        sharedMainActivityViewModel.forums.observe(viewLifecycleOwner){ forums ->
            if (forums.isNotEmpty()) {
                binding.tvDataNull.visibility = View.GONE
                binding.layoutForum.visibility = View.VISIBLE
                Log.d("forums", "$forums")
                userForum = forums.filter { it.user.id == sharedMainActivityViewModel.user.value?.id }
                var userForumLost = userForum!!.filter { it.status == "lost" }
                var userForumFound = userForum!!.filter { it.status == "found" }
                postForums = forums

                binding.apply {
                    tvCountPost.text = userForum!!.size.toString()
                    tvCountPostInProgress.text = userForumLost!!.size.toString()
                    tvCountPostFinished.text = userForumFound!!.size.toString()
                }

                // Call setAdapter after data is initialized

                setAdapter()
            } else {
                binding.layoutForum.visibility = View.GONE
                binding.tvDataNull.visibility = View.VISIBLE
            }
        }
    }

    private fun initView() {
        with(binding){
            ivAddPostForum.setOnClickListener{
                val intent = Intent(activity, MenuActivity::class.java)
                intent.putExtra("MENU_TITLE", "Postingan Baru")
                startActivity(intent)
            }

            refreshForum.setOnRefreshListener {
                sharedMainActivityViewModel.getForums()
                refreshForum.isRefreshing = false
            }
        }
    }

    private fun setAdapter(){
        if (postForums != null && userForum != null){
            val tabArrayList = arrayOf("All Board", "My Board")
            val adapter = ForumPageAdapter(requireActivity(), tabArrayList.size, postForums!!, userForum!!)
            binding.apply {
                viewPagerForum.adapter = adapter
                TabLayoutMediator(tabLayoutForum, viewPagerForum) {tab, position ->
                    tab.text = tabArrayList[position]

                }.attach()

                tabLayoutForum.setSelectedTabIndicatorColor(Color.GRAY)
                tabLayoutForum.setBackgroundColor(Color.TRANSPARENT)
                tabLayoutForum.elevation = 0f
            }
        }else {
            Log.e("setAdapter", "Data is not ready to set the adapter")
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