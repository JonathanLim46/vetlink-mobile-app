package com.example.vetlink.fragment

import android.app.Activity
import android.content.Intent
import android.location.Location
import android.location.LocationManager
import com.example.vetlink.adapter.ClinicList
import com.example.vetlink.adapter.ClinicListAdapter
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.vetlink.LocationPermissionHelper
import com.example.vetlink.R
import com.example.vetlink.Resource
import com.example.vetlink.activity.MainActivity
import com.example.vetlink.activity.MenuActivity
import com.example.vetlink.adapter.RecyclerViewClickListener
import com.example.vetlink.adapter.ScheduleList
import com.example.vetlink.adapter.ScheduleListAdapter
import com.example.vetlink.databinding.FragmentHomeBinding
import com.example.vetlink.viewModel.MainActivityViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.squareup.picasso.Picasso
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Locale

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment : Fragment(), RecyclerViewClickListener<ClinicList> {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var clinicList: ArrayList<ClinicList>
    private lateinit var allClinicList: ArrayList<ClinicList>
    private lateinit var clinicListAdapter: ClinicListAdapter
    private var cityNow: String? = null

    // Schedule
    val dateFormat = SimpleDateFormat("dd", Locale.getDefault())
    val monthFormat = SimpleDateFormat("MMM", Locale.getDefault())

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    private val sharedMainActivityViewModel: MainActivityViewModel by activityViewModels()
    val inputFormat = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
    val outputFormat = SimpleDateFormat("HH:mm", Locale.getDefault())

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

    override fun onResume() {
        super.onResume()
        clinicList.clear()
        getLastLocation()


        sharedMainActivityViewModel.getVeteriners()
        sharedMainActivityViewModel.getQueues()
        sharedMainActivityViewModel.getLatestQueue()
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode){
            LocationPermissionHelper.BASIC_PERMISSION_REQUESTCODE -> {
                if (!LocationPermissionHelper.hasAccessCoarsePermission(requireActivity())){
                    Toast.makeText(requireContext(),
                        "Location Permission is needed, please turn on to run this App",
                        Toast.LENGTH_SHORT).show()

                    if (!LocationPermissionHelper.shouldShowRequestPermissionRationale(requireActivity())){
                        LocationPermissionHelper.launchPermissionSettings(requireActivity())
                        requireActivity().finish()
                    } else {
                        LocationPermissionHelper.checkLocationPermission(requireActivity())
                    }
                } else {
                    Toast.makeText(requireContext(), "Location Permission is Granted", Toast.LENGTH_LONG).show()
                }
            }
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

        clinicList.clear()
        cityNow = sharedMainActivityViewModel.cityNow
        if (cityNow != null) {
            dataClinic(cityNow!!)
        } else {
            getLastLocation()
        }

        return binding.root
    }

    private fun setupObservers() {
        // Observe user data from shared ViewModel
        sharedMainActivityViewModel.getUserHome.observe(viewLifecycleOwner) { resource ->

            when(resource){
                is Resource.Loading -> {
                    binding.shimmerToolBar.startShimmer()
                }
                is Resource.Success -> {
                    showToolBar()

                    if (resource.data != null){
                        binding.layoutHomeToolbar.tvNameHome.text = resource.data.name
                        if (resource.data.photo != null){
                            Picasso.get().load(resource.data.photo).resize(50, 50).centerCrop().into(binding.layoutHomeToolbar.ivPhotoHome)
                        }else{
                            binding.layoutHomeToolbar.ivPhotoHome.setImageResource(R.drawable.img_default_profile)
                        }
                    }
                }
                is Resource.Error -> {
                    Log.d("QueueObserver", "Error loading data: ${resource.message}")
                    Toast.makeText(requireContext(), "Failed connect to server", Toast.LENGTH_SHORT).show()
                    binding.shimmerLayout.hideShimmer()
                    binding.layoutHomeToolbar.ivPhotoHome.setImageResource(R.drawable.img_default_profile)
                }
            }

        }

        sharedMainActivityViewModel.latestQueue.observe(viewLifecycleOwner){ resource ->
            with(binding){

                when(resource) {
                    is Resource.Loading -> {
                        binding.shimmerVisitHistory.startShimmer()
                    }
                    is Resource.Success -> {
                        showVisitHistory()
                        if (resource.data != null){
                            binding.layoutVisitHistoryNull.visibility = View.GONE
                            binding.layoutVisitHistory.visibility = View.VISIBLE

                            layoutHomeVisitHistory.tvClinicNameHistory.text = resource.data.clinic_name
                            if (resource.data.clinic_image != null){
                                Picasso.get().load(resource.data.clinic_image).resize(150, 150).centerCrop().into(binding.layoutHomeVisitHistory.ivClinicHistory)
                            }else{
                                binding.layoutHomeVisitHistory.ivClinicHistory.setImageResource(R.drawable.img_rspets)
                            }
                            layoutHomeVisitHistory.tvClinicLocationHistory.text = resource.data.city

                            layoutHomeVisitHistory.btnReturnVisit.setOnClickListener{
                                val intent = Intent(activity, MenuActivity::class.java)
                                intent.putExtra("MENU_TITLE", "Clinic")
                                intent.putExtra("CLINIC_ID", resource.data.id)
                                startActivity(intent)
                            }

                        } else {
                            Log.d("Queue Latest : ", "Queue is null")
                            layoutVisitHistory.visibility = View.GONE
                            layoutVisitHistoryNull.visibility = View.VISIBLE
                        }
                        // button return visit

                        // btnReturnVisit.setOnClickListener{
                        //
                        // }
                    }
                    is Resource.Error -> {
                        Log.d("QueueObserver", "Error loading data: ${resource.message}")
                        Toast.makeText(requireContext(), "Failed to load visit history", Toast.LENGTH_SHORT).show()
                        binding.shimmerLayout.hideShimmer()
                        layoutVisitHistory.visibility = View.GONE
                        layoutVisitHistoryNull.visibility = View.VISIBLE
                    }
                }


            }
        }

        // Queue
        sharedMainActivityViewModel.queues.observe(viewLifecycleOwner){ resource ->
            when(resource) {
                is Resource.Loading -> {
                    binding.shimmerLayout.startShimmer()
                }
                is Resource.Success -> {
                    showHomeHeader()

                    val upComing = resource.data?.filter { it.status == "ongoing" }
                    val firstDataUpComing = upComing?.firstOrNull()

                    with(binding){
                        if(firstDataUpComing != null){
                            includeHome.layoutHomeNull.visibility = View.GONE
                            includeHome.layoutHome.visibility = View.VISIBLE
                            Log.d("QueueObserver", "Upcoming queue found: $firstDataUpComing")
                            includeHome.tvVisit.text = "There is " + upComing?.size.toString() + " visit(s) for your pets."
                            includeHome.tvTanggal.text = dateFormat.format(firstDataUpComing.appointment_time)
                            includeHome.tvBulan.text = monthFormat.format(firstDataUpComing.appointment_time)
                            includeHome.tvPetName.text = firstDataUpComing.pet.name
                            includeHome.tvClinicName.text = firstDataUpComing.veteriner.clinic_name
                        } else {
                            Log.d("QueueObserver", "No upcoming visits, showing empty view")
                            includeHome.layoutHome.visibility = View.GONE
                            includeHome.layoutHomeNull.visibility = View.VISIBLE
                        }
                    }
                }
                is Resource.Error -> {
                    Log.d("QueueObserver", "Error loading data: ${resource.message}")
                    binding.shimmerLayout.hideShimmer()
                    Toast.makeText(context, "Error: ${resource.message}", Toast.LENGTH_SHORT).show()

                    with(binding) {
                        includeHome.layoutHome.visibility = View.GONE
                        includeHome.layoutHomeNull.visibility = View.VISIBLE
                    }
                }
            }


        }

        // veteriner
        sharedMainActivityViewModel.veteriners.observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Loading -> {
                    getLastLocation()
                    binding.shimmerClinic.startShimmer()
                }
                is Resource.Success -> {
                    if (resource.data != null && resource.data.isNotEmpty()) {
                        Log.d("City Now ", "$cityNow")
                        val filteredClinics = resource.data.filter { it.city == cityNow }

                        Log.d("filter list", "${filteredClinics.size}")

                        if (filteredClinics.isNotEmpty()) {
                            binding.tvClinicNull.visibility = View.GONE
                            allClinicList.clear()
                            showClinic()
                            filteredClinics.take(5).forEach { veteriner ->
                                val openTimeFormatted = outputFormat.format(inputFormat.parse(veteriner.open_time)!!)
                                val closeTimeFormatted = outputFormat.format(inputFormat.parse(veteriner.close_time)!!)
                                allClinicList.add(
                                    ClinicList(
                                        veteriner.id,
                                        veteriner.clinic_image,
                                        veteriner.clinic_name,
                                        veteriner.city,
                                        "Buka | $openTimeFormatted - $closeTimeFormatted"
                                    )
                                )
                            }
                            clinicList.clear()
                            clinicList.addAll(allClinicList)
                            clinicListAdapter.notifyDataSetChanged()
                            getLastLocation()
                        } else {
                            showClinicNull()
                        }
                    } else {
                        showClinicNull()
                    }
                }
                is Resource.Error -> {
                    Log.d("QueueObserver", "Error loading data: ${resource.message}")
                    Toast.makeText(requireContext(), "Failed to load clinic", Toast.LENGTH_SHORT).show()
                    binding.shimmerLayout.hideShimmer()
                }
            }
        }

        sharedMainActivityViewModel.forums.observe(viewLifecycleOwner) { forums ->
            var firstData = forums?.filter { it.status == "lost" }?.sortedByDescending { it.id }
                ?.firstOrNull()

            with(binding){
                if (firstData != null) {
                    Log.d("Response Data: ", "${firstData}")

                    tvUserNamePostMissingSpotlight.text = firstData.user.username

                    tvStatusPostMissingSpotlight.text = firstData.status.capitalize()
                    tvTitleSeenPostSpotlight.text = firstData.title
                    tvLastSeenPostSpotlight.text = firstData.last_seen
                    tvCharacteristicsPostSpotlight.text = if (firstData.characteristics != null) {
                        firstData.characteristics
                    } else {
                        "-"
                    }
                    tvDescriptionPostSpotlight.text = firstData.description
                    if (firstData.pet_image != null){
                        Picasso.get().load(firstData.pet_image).resize(250, 250).centerCrop().into(binding.ivPetMissing)
                    }else{
                        binding.ivPetMissing.setImageResource(R.drawable.img_cats)
                    }

                    if (firstData.user.photo != null){
                        Picasso.get().load(firstData.user.photo).resize(50, 50).centerCrop().into(binding.ivUserPhotoPostMissingSpotlight)
                    }else{
                        binding.ivUserPhotoPostMissingSpotlight.setImageResource(R.drawable.img_default_profile)
                    }
                }
            }
        }


    }

    private fun initView() {

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireContext())
        with(binding){

            rvClinicList.setHasFixedSize(true)
            rvClinicList.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)

            clinicList = ArrayList()
            allClinicList = ArrayList()

            clinicListAdapter = ClinicListAdapter(clinicList, false)
            rvClinicList.adapter = clinicListAdapter
            clinicListAdapter.clickListener(this@HomeFragment)

            srlList.setOnRefreshListener {
                clinicList.clear()
                getLastLocation()
                sharedMainActivityViewModel.getVeteriners()
                binding.tvClinicNull.visibility = View.GONE
                clinicListAdapter.notifyDataSetChanged()
                srlList.isRefreshing = false
            }

            tvViewMoreClinic.setOnClickListener {
                (activity as? MainActivity)?.navigateToTab(R.id.clinicPage)
            }

            tvViewMoreForum.setOnClickListener {
                (activity as? MainActivity)?.navigateToTab(R.id.forumPage)
            }

            includeHome.ivCreateSchedule.setOnClickListener{
                (activity as? MainActivity)?.navigateToTab(R.id.clinicPage)
            }

            includeHome.layoutHome.setOnClickListener{
                val intent = Intent(requireContext(), MenuActivity::class.java)
                intent.putExtra("MENU_TITLE", "Schedule")
                startActivity(intent)
            }

        }
    }

    private fun dataClinic(city: String){

        clinicList.clear()
        for (clinic in allClinicList){
            if (clinic.clinicLocation.lowercase().contains(city.lowercase())){
                clinicList.add(clinic)
            }
        }
        clinicListAdapter.notifyDataSetChanged()
    }

    // LOCATION
    private fun getLastLocation(){
        LocationPermissionHelper.checkLocationPermission(requireActivity())

        if(LocationPermissionHelper.hasAccessCoarsePermission(requireActivity())){
            fusedLocationProviderClient.lastLocation.addOnSuccessListener { location ->
                if(location != null){
                    displayLocationDetails(location)
                } else {
                    clearClinicList("Location not available")
                    getLastLocation()
                }
            }
        } else {
            LocationPermissionHelper.requestCoarseLocationPermission(requireActivity())
            clearClinicList("Location permission is needed")
        }
    }

    private fun displayLocationDetails(location: Location){
        if(location != null){
            try {
                if (isAdded){
                    val city = LocationPermissionHelper.getCityFromLocation(requireContext(), location.latitude, location.longitude)

                    if(city != null){
                        Log.d("Location City : ", "$city")
                        cityNow = city
                        sharedMainActivityViewModel.cityNow = city
                        dataClinic(city)
                    } else {
                        if (isAdded) {
                            Toast.makeText(requireContext(), "Unable to get current city", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }catch (e: IOException){
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

    private fun clearClinicList(message: String) {
        clinicList.clear()
        clinicListAdapter.notifyDataSetChanged()
        if (isAdded) {
            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
        }
    }

    private fun showHomeHeader(){
        binding.shimmerLayout.apply {
            stopShimmer()
            visibility = View.GONE
        }
        binding.includeHome.root.visibility = View.VISIBLE
    }

    private fun showToolBar(){
        binding.shimmerToolBar.apply {
            stopShimmer()
            visibility = View.GONE
        }
        binding.layoutHomeToolbar.root.visibility = View.VISIBLE
    }

    private fun showVisitHistory(){
        binding.shimmerVisitHistory.apply {
            stopShimmer()
            visibility = View.GONE
        }
        binding.layoutHomeVisitHistory.root.visibility = View.VISIBLE
    }

    private fun showClinic(){
        binding.shimmerClinic.apply {
            stopShimmer()
            visibility = View.GONE
        }
        binding.rvClinicList.visibility = View.VISIBLE
    }

    private fun showClinicNull(){
        binding.shimmerClinic.apply {
            stopShimmer()
            visibility = View.GONE
        }
        binding.tvClinicNull.visibility = View.VISIBLE
    }

    override fun onItemClicke(view: View, item: ClinicList) {
        val intent = Intent(activity, MenuActivity::class.java)
        intent.putExtra("MENU_TITLE", "Clinic")
        intent.putExtra("CLINIC_ID", item.clinicId)
        startActivity(intent)
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

}