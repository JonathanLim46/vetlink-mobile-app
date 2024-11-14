package com.example.vetlink.fragment

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.vetlink.R
import com.example.vetlink.adapter.CommentListAdapter
import com.example.vetlink.adapter.ForumPostList
import com.example.vetlink.adapter.ForumPostListAdapter
import com.example.vetlink.adapter.RecyclerViewClickListener
import com.example.vetlink.data.model.comment.Comment
import com.example.vetlink.data.model.forums.Forum
import com.example.vetlink.databinding.FragmentForumPrivateBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog

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

            forumPostListAdapter = ForumPostListAdapter(requireContext(), forumPostList, false)
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
                            forum.characteristics,
                            forum.comments
                        )
                    )
                }
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
                        forum.characteristics,
                        forum.comments
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
        val dialog = activity?.let { BottomSheetDialog(it) }

        val layoutId = when(view.tag){
            "postMenu" -> {
                R.layout.dialog_bottom_sheet_post
            }
            "postComment" -> {
                R.layout.dialog_bottom_sheet_comment
            }

            else -> {R.layout.dialog_bottom_sheet_post}
        }

        val viewLayout = layoutInflater.inflate(layoutId, null, false)

        dialog?.apply {
            setCancelable(true)
            setContentView(viewLayout)

            val bottomSheet = viewLayout.parent as View

            if (view.tag == "postComment") {
                bottomSheet.layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT

                // Here is where we display the comments
                val rvComments = viewLayout.findViewById<RecyclerView>(R.id.rvCommentForum)
                rvComments.layoutManager = LinearLayoutManager(context)

                // Assuming you can get the list of comments for the post
                val commentsList = getCommentsForPost(item) // A function that retrieves comments
                val commentAdapter = CommentListAdapter(commentsList)
                rvComments.adapter = commentAdapter
            } else if (view.tag == "postMenu"){
                val deletePost = viewLayout.findViewById<TextView>(R.id.tvThirdLineDialog)
                deletePost.setOnClickListener{
                    dialog.dismiss()
                    deletePostDialog()
                }
            }

            show()


            val bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet)
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
            bottomSheetBehavior.isHideable = true
        }
    }

    private fun deletePostDialog(){
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.dialog_center_logout)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val tvDialogTitle: TextView = dialog.findViewById(R.id.tvDialogHeader)
        val tvDialogDescription: TextView = dialog.findViewById(R.id.tvDialogDescription)
        val btnYes: Button = dialog.findViewById(R.id.btnDialogLogout)
        val btnNo: Button = dialog.findViewById(R.id.btnDialogCancel)

        tvDialogTitle.text = "Delete this post ?"
        tvDialogDescription.text = "Are you sure you want to permanently delete this post."
        btnYes.text = "Delete"
        btnNo.text = "Cancel"

        btnYes.setOnClickListener {

        }
        btnNo.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    // Example function to get comments for a post
    private fun getCommentsForPost(post: ForumPostList): List<Comment> {
        // In practice, you may retrieve this data from a server or database
        return post.postComments
    }
}