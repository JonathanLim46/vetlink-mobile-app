package com.example.vetlink.fragment

import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.vetlink.R
import com.example.vetlink.activity.MenuActivity
import com.example.vetlink.adapter.FaqCategoryList
import com.example.vetlink.adapter.FaqCategoryListAdapter
import com.example.vetlink.adapter.FaqList
import com.example.vetlink.adapter.FaqListAdapter
import com.example.vetlink.adapter.RecyclerViewClickListener
import com.example.vetlink.databinding.FragmentFaqBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [FaqFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class FaqFragment : Fragment(), RecyclerViewClickListener<FaqCategoryList>{
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var binding: FragmentFaqBinding
    private lateinit var faqList: ArrayList<FaqList>
    private lateinit var faqListAdapter: FaqListAdapter
    private lateinit var faqCategoryList: ArrayList<FaqCategoryList>
    private lateinit var faqCategoryListAdapter: FaqCategoryListAdapter

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
        binding = FragmentFaqBinding.inflate(inflater, container, false)

        initView()

        return binding.root
    }

    private fun initView(){
        with(binding){

            rvFAQ.layoutManager = LinearLayoutManager(requireContext())

            faqList = ArrayList()
            addDataToFaq()

            val isFaqCategory = false
            faqListAdapter = FaqListAdapter(faqList, isFaqCategory)
            rvFAQ.adapter = faqListAdapter


            rvFAQIV.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
            faqCategoryList = ArrayList()
            addDataToFAQIV()
            faqCategoryListAdapter = FaqCategoryListAdapter(faqCategoryList)
            rvFAQIV.adapter = faqCategoryListAdapter

            faqCategoryListAdapter.clickListener(this@FaqFragment)
        }
    }

    private fun addDataToFAQIV() {
        faqCategoryList.add(
            FaqCategoryList(
                R.drawable.img_guide,
                "Guide to Using\nthe App",
                "Guide App"
            )
        )
        faqCategoryList.add(
            FaqCategoryList(
                R.drawable.img_cancel,
                "Clinic Visit\nCancellation",
                "Visit Cancellation"
            )
        )
        faqCategoryList.add(
            FaqCategoryList(
                R.drawable.img_how_to_post,
                "Animal Loss\nPublications",
                "Loss Publications"
            )
        )
    }

    private fun addDataToFaq(){
        faqList.add(FaqList("How to reply or interact with other posts in the Forum?",
                "To reply to or interact with a post on the VetLink Forum, open the app and " +
                        "select the ‘Forum’ menu, then search for the relevant post. Tap the comment icon " +
                        "below the post to open the comment field, write your reply in the box provided, and " +
                        "tap the ‘Submit’ button to post the comment."))

        faqList.add(FaqList("How do I mark forum posts that have been found or are no longer relevant?",
            "To mark your posts on the VetLink forums as found or irrelevant, open the app and select the ‘Forums’ " +
                    "menu, then access ‘My Board’ to view your past posts. Find the post you want to mark, tap the three-dot " +
                    "icon in the top right corner, and select ‘Finish Post’ to mark it as finished."))

        faqList.add(FaqList("Do visit reservations require additional fees?",
            "No, the visit reservation is free of charge. All application activities are charged to the registered Veterinary Clinic."))

        faqList.add(FaqList("How do I rate or review a veterinary clinic I have visited?",
            "Currently, the feature is still under development. We will bring this feature gradually and will be informed through app update information."))

        faqList.add(FaqList("Can I download my reservation or visit confirmation from the app?",
            "The visit process does not require proof of reservation or confirmation of the visit, as it is already taken care of by the Veterinary Clinic you are visiting."))

        faqList.add(FaqList("How do I find my posts back on the forum if they were made a long time ago?",
            "To look back at the posts you've made on VetLink, open the app and select the Forum menu, + " +
                    "then access the My Board option to display all your posts. Tap the Completed Posts button “ + " +
                    "button to see a list of posts that have been marked as completed or irrelevant."))
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment FaqFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            FaqFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun onItemClicke(view: View, item: FaqCategoryList) {
        val intent = Intent(activity, MenuActivity::class.java)
        intent.putExtra("MENU_TITLE", item.faqContext)
        startActivity(intent)
    }

}