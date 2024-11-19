package com.example.vetlink.fragment

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
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
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import com.example.vetlink.LocationPermissionHelper
import com.example.vetlink.R
import com.example.vetlink.Resource
import com.example.vetlink.activity.MenuActivity
import com.example.vetlink.adapter.ClinicList
import com.example.vetlink.adapter.ClinicListAdapter
import com.example.vetlink.adapter.RecyclerViewClickListener
import com.example.vetlink.databinding.FragmentClinicBinding
import com.example.vetlink.viewModel.MainActivityViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import okio.IOException
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
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

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
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireContext())
        getLastLocation()
        setupObservers()
        initView()


        // Inflate the layout for this fragment
        return binding.root
    }

    fun initView() {
        with(binding){

            fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireContext())
            getLastLocation()

            allClinicList = ArrayList()
            clinicList = ArrayList()

            rvClinicPage.layoutManager = LinearLayoutManager(requireContext())

//          searchbar dan searchview
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
        sharedMainActivityViewModel.veteriners.observe(viewLifecycleOwner) { resource ->

            when (resource) {
                is Resource.Loading -> {
                    getLastLocation()
                    binding.shimmerClinicMain.startShimmer()
                }
                is Resource.Success -> {
                    allClinicList.clear()
                    clinicList.clear()

                    resource.data?.forEach { allVet ->
                        val openTimeFormatted = outputFormat.format(inputFormat.parse(allVet.open_time)!!)
                        val closeTimeFormatted = outputFormat.format(inputFormat.parse(allVet.close_time)!!)
                        allClinicList.add(
                            ClinicList(
                                allVet.id,
                                allVet.clinic_image,
                                allVet.clinic_name,
                                allVet.city,
                                "Buka | $openTimeFormatted - $closeTimeFormatted"
                            )
                        )
                    }

                    val filterClinic = allClinicList.filter {
                        it.clinicLocation.equals(binding.tvLocationClinic.text.toString(), ignoreCase = true)
                    }

                    Log.d("Location", "${binding.tvLocationClinic.text}")
                    Log.d("List", "Filtered list size: ${filterClinic.size}")

                    if (filterClinic.isNotEmpty()) {
                        showClinic()
                        filterClinic.forEach { veteriner ->
                            clinicList.add(
                                ClinicList(
                                    veteriner.clinicId,
                                    veteriner.clinicImage,
                                    veteriner.clinicName,
                                    veteriner.clinicLocation,
                                    veteriner.clinicTimeOpen
                                )
                            )
                        }
                    } else {
                        Log.d("Data Clinic:", "No data found for selected city")
                        binding.shimmerClinicMain.apply {
                            stopShimmer()
                            visibility = View.GONE
                        }
                    }
                }
                is Resource.Error -> {
                    binding.shimmerClinicMain.hideShimmer()
                    Log.d("QueueObserver", "Error loading data: ${resource.message}")
                    Toast.makeText(requireContext(), "Failed to load profile, please try again.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun filterList(query: String){

        with(binding){
            Log.d("Query:", query)

            if(!::clinicListAdapter.isInitialized){
                clinicListAdapter = ClinicListAdapter(clinicList, true)
                rvClinicPage.adapter = clinicListAdapter
            } else {
                clinicListAdapter.notifyDataSetChanged()
            }

            clinicList.clear()
            if(query.isEmpty()){
                dataClinic()
            }else {
                Log.d("Query:", query)
                Log.d("Clinic List", allClinicList.size.toString())
                for (clinic in allClinicList){
                    if (clinic.clinicName.lowercase().contains(query.lowercase())){
                        clinicList.add(clinic)
                    } else if (clinic.clinicLocation.lowercase().contains(query.lowercase())){
                        clinicList.add(clinic)
                    }
                }
            }
            clinicListAdapter.notifyDataSetChanged()
            clinicListAdapter.clickListener(this@ClinicFragment)
        }
    }

    private fun dataClinic(){
        with(binding){
            val isClinicPage = true

            clinicList.clear()
            for(clinic in allClinicList){
                if (clinic.clinicLocation.lowercase().contains(tvLocationClinic.text.toString().lowercase())){
                    clinicList.add(clinic)
                }
            }


            if(!::clinicListAdapter.isInitialized){
                clinicListAdapter = ClinicListAdapter(clinicList, isClinicPage)
                rvClinicPage.adapter = clinicListAdapter
            } else {
                clinicListAdapter.notifyDataSetChanged()
            }

            clinicListAdapter.clickListener(this@ClinicFragment)
        }
    }

    override fun onItemClicke(view: View, item: ClinicList) {
        val intent = Intent(activity, MenuActivity::class.java)
        intent.putExtra("MENU_TITLE", "Clinic")
        intent.putExtra("CLINIC_ID", item.clinicId)
        startActivity(intent)
    }

    // LOCATION
    @SuppressLint("MissingPermission")
    private fun getLastLocation(){
        LocationPermissionHelper.checkLocationPermission(requireActivity())
        if(LocationPermissionHelper.hasAccessCoarsePermission(requireActivity())){
            fusedLocationProviderClient.lastLocation.addOnSuccessListener { location ->
                if (location!=null){
                    displayLocationDetails(location)
                }
                else {
                    if(isAdded){
                        Toast.makeText(requireContext(), "Location not available", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        } else {
            LocationPermissionHelper.requestCoarseLocationPermission(requireActivity())
        }
    }

    private fun displayLocationDetails(location: Location){
        if(location != null){
            try {
                if (isAdded){
                    val city = LocationPermissionHelper.getCityFromLocation(requireContext(), location.latitude, location.longitude)

                    if(city != null){
                        Log.d("Location City : ", "$city")
                        binding.tvLocationClinic.text = city

                        dataClinic()
                    } else {
                        if (isAdded) {
                            Toast.makeText(requireContext(), "Unable to get current city", Toast.LENGTH_SHORT).show()
                            binding.tvLocationClinic.text = "Unknown"
                        }
                    }
                }
            }catch (e: java.io.IOException){
                if (isAdded){
                    Toast.makeText(requireContext(), "Geocoder service not available", Toast.LENGTH_SHORT).show()
                    Log.e("Location", "Geocoder Failed", e)
                }
            }
        } else {
            if (isAdded){
                Toast.makeText(requireContext(), "Location not available", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showClinic(){
        binding.shimmerClinicMain.apply {
            stopShimmer()
            visibility = View.GONE
        }
        binding.rvClinicPage.visibility = View.VISIBLE
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