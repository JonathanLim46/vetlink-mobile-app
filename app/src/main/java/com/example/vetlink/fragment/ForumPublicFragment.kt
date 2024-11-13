package com.example.vetlink.fragment

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
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
import com.example.vetlink.adapter.CommentList
import com.example.vetlink.adapter.CommentListAdapter
import com.example.vetlink.adapter.ForumPostList
import com.example.vetlink.adapter.ForumPostListAdapter
import com.example.vetlink.adapter.RecyclerViewClickListener
import com.example.vetlink.data.model.comment.Comment
import com.example.vetlink.data.model.forums.Forum
import com.example.vetlink.data.model.user.User
import com.example.vetlink.data.model.user.UserBasic
import com.example.vetlink.databinding.FragmentForumPublicBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import org.w3c.dom.Text

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_FORUMS = "otherForums"

/**
 * A simple [Fragment] subclass.
 * Use the [ForumPublicFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ForumPublicFragment: Fragment(), RecyclerViewClickListener<ForumPostList> {
    // TODO: Rename and change types of parameters
    private var otherForums: ArrayList<Forum>? = null
    private lateinit var binding: FragmentForumPublicBinding
    private lateinit var forumPostList: ArrayList<ForumPostList>
    private lateinit var forumPostListAdapter: ForumPostListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            otherForums = it.getParcelableArrayList(ARG_FORUMS)
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

            forumPostListAdapter = ForumPostListAdapter(requireContext(), forumPostList, true)
            forumPostListAdapter.notifyDataSetChanged()

            forumPostListAdapter.setClickListener(this@ForumPublicFragment)

            rvPostForumPublic.adapter = forumPostListAdapter

            if (otherForums?.isNotEmpty() == true){
                val lostForums = otherForums!!.filter { it.status.lowercase() == "lost" }
                for (forum in lostForums) {
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

        }
    }

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

    // Example function to get comments for a post
    private fun getCommentsForPost(post: ForumPostList): List<Comment> {
        // In practice, you may retrieve this data from a server or database
        return post.postComments
    }

    private fun deletePostDialog(){
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.layout_center_logout_dialog)
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
        fun newInstance(otherForums: List<Forum>) =
            ForumPublicFragment().apply {
                arguments = Bundle().apply {
                    putParcelableArrayList(ARG_FORUMS, ArrayList(otherForums))
                }
            }
    }

//    Listener Menu Horizontal

}