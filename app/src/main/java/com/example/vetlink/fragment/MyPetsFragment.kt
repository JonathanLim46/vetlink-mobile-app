package com.example.vetlink.fragment

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.vetlink.R
import com.example.vetlink.activity.MenuActivity
import com.example.vetlink.adapter.PetsCategoryList
import com.example.vetlink.adapter.PetsCategoryListAdapter
import com.example.vetlink.adapter.PetsList
import com.example.vetlink.adapter.PetsListAdapter
import com.example.vetlink.adapter.RecyclerViewClickListener
import com.example.vetlink.databinding.FragmentMyPetsBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [MyPetsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MyPetsFragment : Fragment(), RecyclerViewClickListener<PetsList>{
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var binding: FragmentMyPetsBinding
    private lateinit var petsList: ArrayList<PetsList>
    private lateinit var petsListAdapter: PetsListAdapter


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

        binding = FragmentMyPetsBinding.inflate(inflater, container, false)

        initView()

        return binding.root
    }

    @SuppressLint("MissingInflatedId", "UseCompatLoadingForDrawables")
    override fun onItemClicke(view: View, item: PetsList) {
        val dialog = activity?.let { BottomSheetDialog(it) }
        val viewLayout = layoutInflater.inflate(R.layout.layout_bottom_sheet_post_dialog, null, false)

        val firstLine = viewLayout.findViewById<TextView>(R.id.tvFirstLineDialog)
        val lineDone = viewLayout.findViewById<View>(R.id.lineDone)
        val secondLine = viewLayout.findViewById<TextView>(R.id.tvSecondLineDialog)
        val thirdLine = viewLayout.findViewById<TextView>(R.id.tvThirdLineDialog)

        firstLine.visibility = View.GONE
        lineDone.visibility = View.GONE

        secondLine.text = "Edit Pet"
        thirdLine.text = "Delete Pet"

        if (viewLayout.parent != null){
            (viewLayout.parent as ViewGroup).removeView(viewLayout)
        }

        //        Buat Dialog
        dialog?.apply {
            setCancelable(true)
            setContentView(viewLayout)

            show()

//            Bottomsheet height & dragable
            val bottomSheetBehavior = BottomSheetBehavior.from(viewLayout.parent as View)
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
            bottomSheetBehavior.isHideable = true

        }

    }

    @SuppressLint("NotifyDataSetChanged")
    private fun initView(){
        with(binding){


            rvMyPetsList.layoutManager = LinearLayoutManager(requireContext())
            petsList = ArrayList()
            addDataToList()

            petsListAdapter = PetsListAdapter(petsList)
            petsListAdapter.notifyDataSetChanged()
            rvMyPetsList.adapter = petsListAdapter

            petsListAdapter.setClickListener(this@MyPetsFragment)

            srlPets.setOnRefreshListener {
                srlPets.isRefreshing = false
                petsListAdapter.notifyDataSetChanged()
            }

            tvAddPet.setOnClickListener{
                val intent = Intent(activity, MenuActivity::class.java)
                intent.putExtra("MENU_TITLE", "Pet Details")
                startActivity(intent)
            }

            tvFilter.setOnClickListener{
                showBottomSheetFilter()
            }


        }
    }

    private fun showBottomSheetFilter(){

        val dialog = activity?.let { BottomSheetDialog(it) }
        val viewFilter = layoutInflater.inflate(R.layout.layout_bottom_sheet_filter_dialog, null, false)

        val recyclerView = viewFilter.findViewById<RecyclerView>(R.id.rvFilterList)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        val petsCategoryList : ArrayList<PetsCategoryList> = ArrayList()
        addDataPetsCategoryToList(petsCategoryList)

        val petsCategoryListAdapter = PetsCategoryListAdapter(petsCategoryList)
        recyclerView.adapter = petsCategoryListAdapter

        //        Buat Dialog
        dialog?.apply {
            setCancelable(true)
            setContentView(viewFilter)

            show()

//            Bottomsheet height & dragable
            val bottomSheetBehavior = BottomSheetBehavior.from(viewFilter.parent as View)
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
            bottomSheetBehavior.isHideable = true

        }

    }

    private fun addDataToList(){
        petsList.add(PetsList(R.drawable.img_cats, "Mball", "Persian",
            5.toString(), 29.toString()
        ))

        petsList.add(PetsList(R.drawable.img_cats, "Mball", "Persian",
            5.toString(), 29.toString()
        ))

        petsList.add(PetsList(R.drawable.img_cats, "Mball", "Persian",
            5.toString(), 29.toString()
        ))

        petsList.add(PetsList(R.drawable.img_cats, "Mball", "Persian",
            5.toString(), 29.toString()
        ))

        petsList.add(PetsList(R.drawable.img_cats, "Mball", "Persian",
            5.toString(), 29.toString()
        ))
    }

    private fun addDataPetsCategoryToList(petsCategory: ArrayList<PetsCategoryList>){
        petsCategory.add(PetsCategoryList("Dogs"))
        petsCategory.add(PetsCategoryList("Cats"))
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment MyPetsFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            MyPetsFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}