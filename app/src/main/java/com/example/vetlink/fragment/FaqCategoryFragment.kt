package com.example.vetlink.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.view.marginTop
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.vetlink.R
import com.example.vetlink.adapter.FaqList
import com.example.vetlink.adapter.FaqListAdapter
import com.example.vetlink.databinding.FragmentFaqCategoryBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [FaqCategoryFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class FaqCategoryFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var data: String? = null
    private lateinit var binding: FragmentFaqCategoryBinding
    private lateinit var faqList: ArrayList<FaqList>
    private lateinit var faqListAdapter: FaqListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
            data = it.getString("key")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentFaqCategoryBinding.inflate(inflater, container, false)

        initView()

        return binding.root
    }

    @SuppressLint("SetTextI18n")
    private fun initView(){
        with(binding){
            val tvDesc = tvFAQCategoryDesc.layoutParams as ViewGroup.MarginLayoutParams

            rvFAQCategory.layoutManager = LinearLayoutManager(requireContext())
            val isFaqCategory = true

            if(data == "Guide App"){
                ivFAQCategory.setImageResource(R.drawable.img_guide)
                tvFAQCategoryHeader.text = "Guide to Using the App"
                tvFAQCategoryDesc.text = "VetLink is an app designed to make it easier for pet " +
                        "owners to find veterinary clinics, book visits, and interact on community " +
                        "forums. Here is a quick guide to using the main features of VetLink:"
                tvDesc.setMargins(0, 15, 0, 0)

                faqList = ArrayList()
                addDataToFAQPanduan()

                faqListAdapter = FaqListAdapter(faqList, isFaqCategory)
                rvFAQCategory.adapter = faqListAdapter

            }
            else if (data == "Cancellation"){
                val constraintSet = ConstraintSet()
                constraintSet.clone(constraintLayout)

                ivFAQCategory.setImageResource(R.drawable.img_cancel)
                tvFAQCategoryHeader.text = "Clinic Visit Cancellation"

                faqList = ArrayList()
                addDataToFAQPembatalan()

                faqListAdapter = FaqListAdapter(faqList, isFaqCategory)
                rvFAQCategory.adapter = faqListAdapter

                tvFAQCategoryDesc.text = "Cancellation Notification: The relevant clinic will receive " +
                        "your cancellation notification automatically, and you will also get a notification " +
                        "that the visit has been cancelled."

                constraintSet.connect(
                    R.id.tvFAQCategoryDesc,
                    ConstraintSet.TOP,
                    R.id.rvFAQCategory,
                    ConstraintSet.BOTTOM
                )

                constraintSet.connect(
                    R.id.rvFAQCategory,
                    ConstraintSet.TOP,
                    R.id.tvFAQCategoryHeader,
                    ConstraintSet.BOTTOM
                )

                constraintSet.clear(
                    R.id.rvFAQCategory,
                    ConstraintSet.BOTTOM
                )

                constraintSet.applyTo(constraintLayout)


            }
            else if (data == "Loss"){

                ivFAQCategory.setImageResource(R.drawable.img_how_to_post)
                tvFAQCategoryHeader.text = "Animal Loss Publications"
                tvFAQCategoryDesc.text = "On VetLink, the Lost Animal Publication feature allows you " +
                        "to report lost pets and assist others in finding them. With this feature, you " +
                        "can create a post that includes important information such as a description of " +
                        "the animal and a photo of the animal."
                tvDesc.setMargins(0, 15, 0, 0)

                faqList = ArrayList()
                addDataToPublikasi()

                faqListAdapter = FaqListAdapter(faqList, isFaqCategory)
                rvFAQCategory.adapter = faqListAdapter

            }
        }
    }

    private fun addDataToFAQPanduan(){
        faqList.add(FaqList("1. Visit Reservation",
            "You can search and select nearby veterinary clinics, view doctors' schedules, " +
                    "and make a reservation for a visit directly through the app. There is also an " +
                    "option to cancel or change the schedule if needed."))

        faqList.add(FaqList("2. Animal Loss Forum",
            "This forum allows you to make posts about lost animals or provide information " +
                    "if you find an animal. You can also reply or interact with other posts to help " +
                    "other users find their pets."))

        faqList.add(FaqList("3. Post Management",
            "All the posts you've ever made can be managed through the ‘My Board’ menu, " +
                    "where you can mark posts as completed or view a history of previous posts."))

        faqList.add(FaqList("4. Visit History",
            "This feature allows you to see a record of your past veterinary clinic visits, " +
                    "so you can better monitor your pet's health."))

        faqList.add(FaqList("5. Feature Updates",
            "Some additional features, such as rating or reviewing clinics, are in development " +
                    "and will be available in future updates."))
    }

    private fun addDataToFAQPembatalan(){
        faqList.add(FaqList("1. Log in to the VetLink App",
            "Open the app and login to your account."))

        faqList.add(FaqList("2. Access Profile Menu and Select Schedule Menu",
            "Check the list of excursions you have booked."))

        faqList.add(FaqList("3. Check the Upcoming Section",
            "Select the visit you want to cancel by long-pressing on the visit."))

        faqList.add(FaqList("4. Click the Cancel Visit Option",
            "You will be asked to confirm the cancellation, once confirmed the visit will be cancelled."))
    }

    private fun addDataToPublikasi(){
        faqList.add(FaqList("1. Making Posts",
            "Go to the ‘Forum’ menu and tap the plus sign to create a new post. Fill in the " +
                    "details about the missing animal, including physical characteristics, location, " +
                    "and when it was last seen. Don't forget to add a photo to make it easier to recognise."))

        faqList.add(FaqList("2. Interact with Other Users",
            "You can reply to comments or questions from other users who may have information " +
                    "related to the whereabouts of the animal. This feature facilitates communication and " +
                    "increases the chances of the animal being found."))

        faqList.add(FaqList("3. Marking a Post as Completed",
            "If your animal has been found, you can mark the post as ‘Completed’ to let the " +
                    "community know that the issue is resolved."))
    }

    companion object {
        private lateinit var arguments: Bundle

        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter   1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment FaqCategoryFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(data: String): FaqCategoryFragment {
            val fragment = FaqCategoryFragment()
            val bundle = Bundle()
            bundle.putString("key", data)
            fragment.arguments = bundle
            return fragment
        }

    }
}