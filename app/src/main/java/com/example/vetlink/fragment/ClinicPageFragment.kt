package com.example.vetlink.fragment

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.vetlink.R
import com.example.vetlink.activity.MenuActivity
import com.example.vetlink.adapter.PetsSelectList
import com.example.vetlink.adapter.PetsSelectListAdapter
import com.example.vetlink.databinding.FragmentClinicPageBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import java.util.Calendar

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


/**
 * A simple [Fragment] subclass.
 * Use the [ClinicPageFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ClinicPageFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var binding : FragmentClinicPageBinding
    private lateinit var petsSelectList: ArrayList<PetsSelectList>
    private lateinit var petsSelectListAdapter: PetsSelectListAdapter


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
        binding = FragmentClinicPageBinding.inflate(inflater, container, false)

        initView()

        return binding.root
    }

    private fun initView(){

        with(binding){

            etDateClinicPage.setOnClickListener{
                val calender = Calendar.getInstance()

                val year = calender.get(Calendar.YEAR)
                val month = calender.get(Calendar.MONTH)
                val day = calender.get(Calendar.DAY_OF_MONTH)

                val datePickerDialog = DatePickerDialog(
                    requireContext(),
                    { view, year, monthOfYear, dayOfMonth ->
                        val date = (dayOfMonth.toString() + "/" + (monthOfYear + 1) + "/" + year)
                        etDateClinicPage.setText(date)
                        tvVisitDateUserSelect.setText(date)
                    },

                    year,
                    month,
                    day
                )

                datePickerDialog.show()
            }

            btnVisit.setOnClickListener{
                visitClinic()
            }

            tvSelectPetsDialog.setOnClickListener {
                selectPetsDialog()
            }

        }


    }

    private fun visitClinic(){
        val dialog = activity?.let { it1 -> BottomSheetDialog(it1) }
        val viewLayout = layoutInflater.inflate(R.layout.layout_bottom_sheet_success_dialog, null, false)

        val intent = Intent(activity, MenuActivity::class.java)
        intent.putExtra("MENU_TITLE", "Schedule")

        dialog?.apply {

            setCancelable(true)
            setContentView(viewLayout)

            show()

            val bottomSheetBehavior = BottomSheetBehavior.from(viewLayout.parent as View)
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
            bottomSheetBehavior.isHideable = true

            val btnDone = viewLayout.findViewById<Button>(R.id.btnDone)
            btnDone.setOnClickListener{
                startActivity(intent)
                activity?.finish()
            }
        }

        dialog?.setOnDismissListener{
            startActivity(intent)
            activity?.finish()
        }
    }

    private fun selectPetsDialog(){
        val dialog = activity?.let { BottomSheetDialog(it) }

        val view = layoutInflater.inflate(R.layout.layout_bottom_sheet_select_pet_dialog, null, false)

        val rvSelectPet = view.findViewById<RecyclerView>(R.id.rvSelectPetForum)
        rvSelectPet.layoutManager = LinearLayoutManager(requireContext())

        petsSelectList = ArrayList()
        addDataToPetList()

        petsSelectListAdapter = PetsSelectListAdapter(petsSelectList)
        rvSelectPet.adapter = petsSelectListAdapter


        dialog?.apply {
            setCancelable(true)
            setContentView(view)

            show()

            val bottomSheetBehavior = BottomSheetBehavior.from(view.parent as View)
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
            bottomSheetBehavior.isHideable = true
        }
    }

    private fun addDataToPetList(){
        petsSelectList.add(PetsSelectList("Mball", "Cat", R.drawable.img_cats))
        petsSelectList.add(PetsSelectList("Mball", "Cat", R.drawable.img_cats))
        petsSelectList.add(PetsSelectList("Mball", "Cat", R.drawable.img_cats))
        petsSelectList.add(PetsSelectList("Mball", "Cat", R.drawable.img_cats))
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ClinicPageFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ClinicPageFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}