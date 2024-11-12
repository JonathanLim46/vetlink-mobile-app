package com.example.vetlink.fragment

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toDrawable
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.vetlink.R
import com.example.vetlink.adapter.ForumPostList
import com.example.vetlink.adapter.ForumPostListAdapter
import com.example.vetlink.adapter.RecyclerViewClickListener
import com.example.vetlink.data.model.forums.Forum
import com.example.vetlink.databinding.FragmentForumPrivateBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_FORUMS = "userForums"

/**
 * A simple [Fragment] subclass.
 * Use the [ForumPrivateFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ForumPrivateFragment() : Fragment(), RecyclerViewClickListener<ForumPostList> {
    // TODO: Rename and change types of parameters
    private lateinit var binding: FragmentForumPrivateBinding

    private var userForums: ArrayList<Forum>? = null

    private lateinit var forumPostList: ArrayList<ForumPostList>
    private lateinit var forumPostListAdapter: ForumPostListAdapter

    val mainButtonBackground = R.drawable.layout_button_main
    val mainTextColor = Color.parseColor("#FFFFFF")
    val secondButtonBackground = R.drawable.layout_button_second
    val secondTextColor = Color.parseColor("#B08BBB")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            userForums = it.getParcelableArrayList<Forum>(ARG_FORUMS)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentForumPrivateBinding.inflate(inflater, container, false)

        initView()

        return binding.root
    }


    private fun initView(){

        with(binding){

            btnAll.setBackgroundResource(mainButtonBackground)
            btnAll.setTextColor(mainTextColor)

            forumPostList = ArrayList()

            forumPostListAdapter = ForumPostListAdapter(requireContext(), forumPostList)
            forumPostListAdapter.setClickListener(this@ForumPrivateFragment)

            rvPostForumPrivate.layoutManager = LinearLayoutManager(requireContext()) // Add this line
            rvPostForumPrivate.adapter = forumPostListAdapter

            if (userForums?.isNotEmpty() == true){
                for (forum in userForums!!) {
                    forumPostList.add(
                        ForumPostList(
                            forum.user.photo,
                            forum.pet_image,
                            forum.user.username,
                            forum.status,
                            forum.title,
                            forum.description,
                            forum.last_seen,
                            forum.characteristics
                        )
                    )
                }
                Log.d("forumPostListPrivate", forumPostList.toString())
            }
            forumPostListAdapter.notifyDataSetChanged()

            btnAll.setOnClickListener{
                buttonState(btnAll, listOf(btnFound, btnLost))
                filterList("all")
            }

            btnFound.setOnClickListener{
                buttonState(btnFound, listOf(btnAll, btnLost))
                filterList("found")
            }

            btnLost.setOnClickListener{
                buttonState(btnLost, listOf(btnAll, btnFound))
                filterList("lost")
            }

        }
    }

    private fun filterList(status: String) {
        val filteredList = when (status.lowercase()) {
            "all" -> userForums // No filter, show all
            "found" -> userForums?.filter { it.status.lowercase() == "found" }
            "lost" -> userForums?.filter { it.status.lowercase() == "lost" }
            else -> userForums // Default to showing all
        }

        forumPostList.clear()
        if (filteredList != null) {
            for (forum in filteredList) {
                forumPostList.add(
                    ForumPostList(
                        forum.user.photo,
                        forum.pet_image,
                        forum.user.username,
                        forum.status,
                        forum.title,
                        forum.description,
                        forum.last_seen,
                        forum.characteristics
                    )
                )
            }
        }

        forumPostListAdapter.notifyDataSetChanged()
    }

    private fun buttonState(selectedButton: Button, otherButton: List<Button>){
        selectedButton.setBackgroundResource(mainButtonBackground)
        selectedButton.setTextColor(mainTextColor)

        for (button in otherButton){
            button.setBackgroundResource(secondButtonBackground)
            button.setTextColor(secondTextColor)
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ForumPrivateFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(userForum: List<Forum>) =
            ForumPrivateFragment().apply {
                arguments = Bundle().apply {
                    putParcelableArrayList(ARG_FORUMS, ArrayList(userForum))
                }
            }
    }

    override fun onItemClicke(view: View, item: ForumPostList) {

    }
}