package com.example.vetlink.fragment

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import com.example.vetlink.R
import com.example.vetlink.activity.MenuActivity
import com.example.vetlink.adapter.ClinicList
import com.example.vetlink.adapter.ClinicListAdapter
import com.example.vetlink.adapter.RecyclerViewClickListener
import com.example.vetlink.databinding.FragmentClinicBinding
import com.example.vetlink.viewModel.MainActivityViewModel
import java.text.SimpleDateFormat
import java.util.Locale

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ClinicFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ClinicFragment : Fragment(), RecyclerViewClickListener<ClinicList>{
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    val inputFormat = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
    val outputFormat = SimpleDateFormat("HH:mm", Locale.getDefault())

    private lateinit var binding: FragmentClinicBinding
    private lateinit var allClinicList: ArrayList<ClinicList>
    private lateinit var clinicList: ArrayList<ClinicList>
    private lateinit var clinicListAdapter: ClinicListAdapter

    private val sharedMainActivityViewModel: MainActivityViewModel by activityViewModels()


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

        binding = FragmentClinicBinding.inflate(inflater, container, false)

        initView()
        setupObservers()

        // Inflate the layout for this fragment
        return binding.root
    }

    fun initView() {
        with(binding){
            val isClinicPage = true
            allClinicList = ArrayList()
            clinicList = ArrayList()
            rvClinicPage.layoutManager = LinearLayoutManager(requireContext())
            clinicListAdapter = ClinicListAdapter(clinicList, isClinicPage)
            clinicListAdapter.notifyDataSetChanged()
            rvClinicPage.adapter = clinicListAdapter
            clinicListAdapter.clickListener(this@ClinicFragment)

//            searchbar dan searchview

            searchBarClinic.setOnClickListener{
                searchView.show()
            }

            searchView.editText.setOnEditorActionListener(object: TextView.OnEditorActionListener{
                override fun onEditorAction(p0: TextView?, p1: Int, p2: KeyEvent?): Boolean {
                    val text: String = p0?.text.toString()
                    searchView.hide()
                    searchBarClinic.setText(text)
                    return true
                }

            })

            searchView.editText.addTextChangedListener(object: TextWatcher{
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

//                  Search Suggestions
//                    val searchSuggest = arrayOf("Klinik IPB", "Klinik Sukmajaya", "Klinik Sempur")
//
//                    rvSearchView.layoutManager = LinearLayoutManager(requireContext())

                }

                override fun afterTextChanged(p0: Editable?) {
                    filterList(p0.toString())
                }

            })



        }
    }

    private fun setupObservers(){
        sharedMainActivityViewModel.veteriners.observe(viewLifecycleOwner) { veteriners ->

            allClinicList.clear()
            veteriners?.forEach{ veteriner ->
                val openTimeFormatted = outputFormat.format(inputFormat.parse(veteriner.open_time)!!)
                val closeTimeFormatted = outputFormat.format(inputFormat.parse(veteriner.close_time)!!)
                allClinicList.add(ClinicList(veteriner.clinic_image, veteriner.clinic_name, veteriner.city, "Buka | $openTimeFormatted - $closeTimeFormatted"))
            }
            clinicListAdapter.notifyDataSetChanged()
            clinicList.addAll(allClinicList)
        }
    }

    private fun filterList(query: String){
        with(binding){
            clinicList.clear()
            if(query.isEmpty()){
                clinicList.addAll(allClinicList)
            }else {
                for (clinic in allClinicList){
                    if (clinic.clinicName.lowercase().contains(query.lowercase())){
                        clinicList.add(clinic)
                    } else if (clinic.clinicLocation.lowercase().contains(query.lowercase())){
                        clinicList.add(clinic)
                    }
                }
            }
            clinicListAdapter.notifyDataSetChanged()
        }

    }

    override fun onItemClicke(view: View, item: ClinicList) {
        val intent = Intent(activity, MenuActivity::class.java)
        intent.putExtra("MENU_TITLE", "Clinic")
        startActivity(intent)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment Clinic.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ClinicFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }


}