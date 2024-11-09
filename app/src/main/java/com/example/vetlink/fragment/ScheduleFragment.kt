package com.example.vetlink.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.vetlink.R
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
        sharedMenuActivityViewModel.queues.observe(viewLifecycleOwner){ queues ->
            var upComing = queues.filter { it.status == "ongoing" }
            var history = queues.filter { it.status == "finished" }
            var cancel = queues.filter { it.status == "canceled" }

            binding.tvCountUpcoming.text = if (upComing.isNotEmpty()) "(" + upComing.size.toString() + ")" else "(0)"
            binding.tvCountHistorySchedule.text = if (history.isNotEmpty()) "(" + history.size.toString() + ")" else "(0)"
            binding.tvCountCancelSchedule.text = if (cancel.isNotEmpty()) "(" + cancel.size.toString() + ")" else "(0)"

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
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun initView(){
        with(binding){

            rvUpComing.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
            rvHistory.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
            rvCancel.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)

            scheduleList = ArrayList()
            addDataToUpComing()

            scheduleListAdapter = ScheduleListAdapter(scheduleList)
            scheduleListAdapter.notifyDataSetChanged()

            rvUpComing.adapter = scheduleListAdapter
            rvHistory.adapter = scheduleListAdapter
            rvCancel.adapter = scheduleListAdapter

            srlSchedule.setOnRefreshListener {
                srlSchedule.isRefreshing = false
                scheduleListAdapter.notifyDataSetChanged()
            }

        }
    }

    private fun addDataToUpComing(){
        scheduleList.add(
            ScheduleList("4", "Friday, October 2024",
            "Klinik Hewan IPB", "Mbal",
            "Dramaga, Kab. Bogor")
        )
        scheduleList.add(
            ScheduleList("4", "Friday, October 2024",
                "Klinik Hewan IPB", "Mbal",
                "Dramaga, Kab. Bogor")
        )
        scheduleList.add(
            ScheduleList("4", "Friday, October 2024",
                "Klinik Hewan IPB", "Mbal",
                "Dramaga, Kab. Bogor")
        )
        scheduleList.add(
            ScheduleList("4", "Friday, October 2024",
                "Klinik Hewan IPB", "Mbal",
                "Dramaga, Kab. Bogor")
        )
        scheduleList.add(
            ScheduleList("4", "Friday, October 2024",
                "Klinik Hewan IPB", "Mbal",
                "Dramaga, Kab. Bogor")
        )
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