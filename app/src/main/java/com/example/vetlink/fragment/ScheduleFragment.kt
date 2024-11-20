package com.example.vetlink.fragment

import android.annotation.SuppressLint
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
import com.example.vetlink.R
import com.example.vetlink.Resource
import com.example.vetlink.adapter.ScheduleList
import com.example.vetlink.adapter.ScheduleListAdapter
import com.example.vetlink.data.model.queue.Queue
import com.example.vetlink.databinding.FragmentScheduleBinding
import com.example.vetlink.viewModel.MenuActivityViewModel
import java.text.SimpleDateFormat
import java.util.Locale

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ScheduleFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ScheduleFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var binding: FragmentScheduleBinding
    private lateinit var scheduleList: ArrayList<ScheduleList>
    private lateinit var scheduleListAdapter: ScheduleListAdapter
    private val sharedMenuActivityViewModel: MenuActivityViewModel by activityViewModels()
    val dateFormat = SimpleDateFormat("d", Locale.getDefault())
    val fullDateFormat = SimpleDateFormat("EEEE, MMMM yyyy", Locale.getDefault())


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
        binding = FragmentScheduleBinding.inflate(inflater, container, false)

        initView()
        setupObservers()
        return binding.root
    }

    private fun setupObservers(){
        sharedMenuActivityViewModel.queues.observe(viewLifecycleOwner){ resource ->

            when(resource){
                is Resource.Loading ->{
                    binding.shimmerSchedule.startShimmer()
                }
                is Resource.Success ->{

                    if(resource.data != null){
                        showSchedule()
                        binding.layoutScheduleNull.visibility = View.GONE
                        binding.scrollViewSchedule.visibility = View.VISIBLE
                        Log.d("Schedule", "${resource.data.size}")

                        var pending = resource.data.filter { it.status == "pending" }
                        var upComing = resource.data.filter { it.status == "ongoing" }
                        var history = resource.data.filter { it.status == "finished" }
                        var cancel = resource.data.filter { it.status == "canceled" }

                        binding.tvCountPending.text = if (pending.isNotEmpty()) "(" + pending.size.toString() + ")" else "0"
                        binding.tvCountUpcoming.text = if (upComing.isNotEmpty()) "(" + upComing.size.toString() + ")" else "(0)"
                        binding.tvCountHistorySchedule.text = if (history.isNotEmpty()) "(" + history.size.toString() + ")" else "(0)"
                        binding.tvCountCancelSchedule.text = if (cancel.isNotEmpty()) "(" + cancel.size.toString() + ")" else "(0)"

                        var pendingList = ArrayList(pending.map { queue ->
                            ScheduleList(
                                scheduleDate = dateFormat.format(queue.appointment_time),
                                scheduleFullDate = fullDateFormat.format(queue.appointment_time),
                                scheduleClinicName = queue.veteriner.clinic_name,
                                schedulePetName = queue.pet.name,
                                scheduleClinicFullLocation = queue.veteriner.city
                            )
                        })
                        binding.rvPending.adapter = ScheduleListAdapter(pendingList)
                        binding.rvPending.adapter?.notifyDataSetChanged()

                        var upComingList = ArrayList(upComing.map { queue ->
                            ScheduleList(
                                scheduleDate = dateFormat.format(queue.appointment_time),
                                scheduleFullDate = fullDateFormat.format(queue.appointment_time),
                                scheduleClinicName = queue.veteriner.clinic_name,
                                schedulePetName = queue.pet.name,
                                scheduleClinicFullLocation = queue.veteriner.city
                            )
                        })
                        binding.rvUpComing.adapter = ScheduleListAdapter(upComingList)
                        binding.rvUpComing.adapter?.notifyDataSetChanged()

                        var historyList = ArrayList(history.map { queue ->
                            ScheduleList(
                                scheduleDate = dateFormat.format(queue.appointment_time),
                                scheduleFullDate = fullDateFormat.format(queue.appointment_time),
                                scheduleClinicName = queue.veteriner.clinic_name,
                                schedulePetName = queue.pet.name,
                                scheduleClinicFullLocation = queue.veteriner.city
                            )
                        })
                        binding.rvHistory.adapter = ScheduleListAdapter(historyList)
                        binding.rvHistory.adapter?.notifyDataSetChanged()

                        var cancelList = ArrayList(cancel.map { queue ->
                            ScheduleList(
                                scheduleDate = dateFormat.format(queue.appointment_time),
                                scheduleFullDate = fullDateFormat.format(queue.appointment_time),
                                scheduleClinicName = queue.veteriner.clinic_name,
                                schedulePetName = queue.pet.name,
                                scheduleClinicFullLocation = queue.veteriner.city
                            )
                        })

                        binding.rvCancel.adapter = ScheduleListAdapter(cancelList)
                        binding.rvCancel.adapter?.notifyDataSetChanged()



                        if (pendingList.isEmpty() && upComingList.isEmpty() && historyList.isEmpty() && cancelList.isEmpty()){
                            showSchedule()
                            binding.scrollViewSchedule.visibility = View.GONE
                            binding.layoutScheduleNull.visibility = View.VISIBLE
                        } else {
                            showSchedule()
                            binding.layoutScheduleNull.visibility = View.GONE
                            binding.scrollViewSchedule.visibility = View.VISIBLE
                        }

                    }

                }
                is Resource.Error ->{
                    Log.d("QueueObserver", "Error loading data: ${resource.message}")
                    binding.shimmerSchedule.hideShimmer()
                    Toast.makeText(context, "Error: ${resource.message}", Toast.LENGTH_SHORT).show()
                }
            }


        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun initView(){
        with(binding){

            rvPending.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
            rvUpComing.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
            rvHistory.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
            rvCancel.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)

            srlSchedule.setOnRefreshListener {
                sharedMenuActivityViewModel.getQueues()
                srlSchedule.isRefreshing = false
            }

        }
    }

    private fun showSchedule(){
        binding.shimmerSchedule.apply {
            stopShimmer()
            visibility = View.GONE
        }
    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ScheduleFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ScheduleFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}