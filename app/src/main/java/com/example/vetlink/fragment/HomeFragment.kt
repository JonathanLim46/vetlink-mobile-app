package com.example.vetlink.fragment

import android.annotation.SuppressLint
import android.content.Intent
import com.example.vetlink.adapter.ClinicList
import com.example.vetlink.adapter.ClinicListAdapter
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.vetlink.R
import com.example.vetlink.activity.MainActivity
import com.example.vetlink.data.model.user.User
import com.example.vetlink.databinding.FragmentHomeBinding
import com.example.vetlink.viewModel.MainActivityViewModel
import com.squareup.picasso.Picasso
import java.util.Collections

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
private lateinit var clinicList: ArrayList<ClinicList>
private lateinit var clinicListAdapter: ClinicListAdapter

/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding

    private val sharedMainActivityViewModel: MainActivityViewModel by activityViewModels()

    // TODO: Rename and change types of parameters
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

        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        initView()
        setupObservers()

        return binding.root
    }

    private fun setupObservers() {
        // Observe user data from shared ViewModel
        sharedMainActivityViewModel.user.observe(viewLifecycleOwner) { user ->
            if (user != null){
                binding.tvNameHome.text = user.name
                if (user.photo != null){
                    Picasso.get().load(user.photo).resize(50, 50).centerCrop().into(binding.ivPhotoHome)
                }else{
                    binding.ivPhotoHome.setImageResource(R.drawable.default_profile)
                }
            }

        }
    }

    private fun initView() {
        with(binding){
            val isClinicPage = false

            rvClinicList.setHasFixedSize(true)
            rvClinicList.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)

            clinicList = ArrayList()
            addDataToList()

            clinicListAdapter = ClinicListAdapter(clinicList, isClinicPage)
            clinicListAdapter.notifyDataSetChanged()
            rvClinicList.adapter = clinicListAdapter

            srlList.setOnRefreshListener {

                srlList.isRefreshing = false
                clinicListAdapter.notifyDataSetChanged()
            }

            tvNameHome.text = "Loading.."

//            View More
            tvViewMoreClinic.setOnClickListener{
                val intent = Intent(activity, MainActivity::class.java)
                intent.putExtra("fragment", "clinicFragment")
                startActivity(intent)
            }

            tvViewMoreForum.setOnClickListener{
                val intent = Intent(activity, MainActivity::class.java)
                intent.putExtra("fragment", "forumFragment")
                startActivity(intent)
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
         * @return A new instance of fragment Home.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HomeFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    private fun addDataToList(){
        clinicList.add(ClinicList(R.drawable.img_rspets, "Klinik IPB", "Sukmajaya, Depok", "Buka | 07.00 - 15.00"))
        clinicList.add(ClinicList(R.drawable.img_rspets, "Klinik IPB", "Sukmajaya, Depok", "Buka | 07.00 - 15.00"))
        clinicList.add(ClinicList(R.drawable.img_rspets, "Klinik IPB", "Sukmajaya, Depok", "Buka | 07.00 - 15.00"))
        clinicList.add(ClinicList(R.drawable.img_rspets, "Klinik IPB", "Sukmajaya, Depok", "Buka | 07.00 - 15.00"))
    }
}