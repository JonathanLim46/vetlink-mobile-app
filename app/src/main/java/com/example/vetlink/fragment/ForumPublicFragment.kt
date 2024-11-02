package com.example.vetlink.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.vetlink.R
import com.example.vetlink.adapter.ForumPostList
import com.example.vetlink.adapter.ForumPostListAdapter
import com.example.vetlink.adapter.RecyclerViewClickListener
import com.example.vetlink.databinding.FragmentForumPublicBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ForumPublicFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ForumPublicFragment : Fragment(), RecyclerViewClickListener<ForumPostList> {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var binding: FragmentForumPublicBinding
    private lateinit var forumPostList: ArrayList<ForumPostList>
    private lateinit var forumPostListAdapter: ForumPostListAdapter

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
        binding = FragmentForumPublicBinding.inflate(inflater, container, false)

        initView()

        return binding.root
    }

    private fun initView(){
        with(binding){
            rvPostForumPublic.layoutManager = LinearLayoutManager(requireContext())

            forumPostList = ArrayList()
            addDataToPost()

            forumPostListAdapter = ForumPostListAdapter(forumPostList)
            forumPostListAdapter.notifyDataSetChanged()

            forumPostListAdapter.setClickListener(this@ForumPublicFragment)

            rvPostForumPublic.adapter = forumPostListAdapter

        }
    }

    private fun addDataToPost(){
        forumPostList.add(ForumPostList(R.drawable.img_tontawan, R.drawable.img_cats, "mawarptr",
            "Depok", "In Progress", "Mball Hilang",
            "Lorem ipsum dolor sit amet, consectetur adipiscing elit." +
                    "Fusce iaculis mattis leo id commodo. Etiam sed pretium leo. Nulla molestie orci ut varius" +
                    "porta. Nullam eu justo lacinia, faucibus eros vel, lacinia dolor. Etiam sollicitudin ligula" +
                    "vitae lorem tristique, eget ultricies elit tincidunt. Mauris eget diam nisl. Fusce at purus" +
                    "semper, placerat tortor ac, ultrices arcu. Ut orci orci, interdum vitae arcu quis, bibendum" +
                    "blandit felis. In ornare tellus quis quam ornare, malesuada gravida augue lobortis."))
        forumPostList.add(ForumPostList(R.drawable.img_tontawan, R.drawable.img_cats, "mawarptr",
            "Depok", "In Progress", "Mball Hilang",
            "Lorem ipsum dolor sit amet, consectetur adipiscing elit." +
                    "Fusce iaculis mattis leo id commodo. Etiam sed pretium leo. Nulla molestie orci ut varius" +
                    "porta. Nullam eu justo lacinia, faucibus eros vel, lacinia dolor. Etiam sollicitudin ligula" +
                    "vitae lorem tristique, eget ultricies elit tincidunt. Mauris eget diam nisl. Fusce at purus" +
                    "semper, placerat tortor ac, ultrices arcu. Ut orci orci, interdum vitae arcu quis, bibendum" +
                    "blandit felis. In ornare tellus quis quam ornare, malesuada gravida augue lobortis."))
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ForumPublicFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ForumPublicFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

//    Listener Menu Horizontal
    override fun onItemClicke(view: View, item: ForumPostList) {
        val dialog = activity?.let { BottomSheetDialog(it) }

        val layoutId = when(view.tag){
            "postMenu" -> {
                R.layout.layout_bottom_sheet_post_dialog
            }
            "postComment" -> {
                R.layout.layout_bottom_sheet_comment_dialog
            }

            else -> {R.layout.layout_bottom_sheet_post_dialog}
        }

        val viewLayout = layoutInflater.inflate(layoutId, null, false)

        dialog?.apply {
            setCancelable(true)
            setContentView(viewLayout)


            val bottomSheet = viewLayout.parent as View


            if (view.tag == "postComment") {
                bottomSheet.layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT
            }

            show()


            val bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet)
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
            bottomSheetBehavior.isHideable = true
        }
    }
}