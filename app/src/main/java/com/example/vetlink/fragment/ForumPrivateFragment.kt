package com.example.vetlink.fragment

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.activityViewModels
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
import com.example.vetlink.viewModel.MainActivityViewModel
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
    private val sharedMainActivityViewModel: MainActivityViewModel by activityViewModels()

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
                            forum.id,
                            forum.user.photo,
                            forum.pet_image,
                            forum.user.username,
                            forum.status,
                            forum.title,
                            forum.description,
                            forum.last_seen,
                            forum.characteristics,
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
                        forum.id,
                        forum.user.photo,
                        forum.pet_image,
                        forum.user.username,
                        forum.status,
                        forum.title,
                        forum.description,
                        forum.last_seen,
                        forum.characteristics,
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

        if (view.tag == "postShare"){
            val header = "Lost Animal Announcement - ${item.postHeader}"
            val lastSeen = "Last seen in ${item.postLastSeen}"
            val characteristics = "With the following characteristics : ${item.postCharacteristics}"
            val contactInfo = "Jika Anda memiliki informasi atau melihat kucing ini, harap hubungi [Nomor Kontak] secepatnya." +
                    "\n*note: harap isi nomor telepon atau kontak yang sesuai."
            val message = """
$header

$lastSeen

$characteristics

$contactInfo
""".trimIndent()
            val sendIntent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, message)
                type = "text/plain"
            }
            val shareIntent = Intent.createChooser(sendIntent, null)
            startActivity(shareIntent)
            return
        }

        val layoutId = when(view.tag){
            "postMenu" -> {
                R.layout.dialog_bottom_sheet_post
            }
            "postComment" -> {
                sharedMainActivityViewModel.getComments(item.postId!!)
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

                sharedMainActivityViewModel.forumsComments.observe(viewLifecycleOwner){ comments ->
                    val commentAdapter = CommentListAdapter(comments)
                    rvComments.adapter = commentAdapter
                }

                val btnSendComment = viewLayout.findViewById<ImageView>(R.id.ivSendComment)
                val etReplyComment = viewLayout.findViewById<TextView>(R.id.etReplyComment)

                // Initial state: disable the button if there's no text
                btnSendComment.isEnabled = false
                btnSendComment.setColorFilter(Color.GRAY) // Optional: Make the button visually look disabled

                // Add a TextWatcher to the EditText
                etReplyComment.addTextChangedListener(object : TextWatcher {
                    override fun afterTextChanged(s: Editable?) {
                        // If the text is not empty, enable the button
                        if (!s.isNullOrEmpty()) {
                            btnSendComment.isEnabled = true
                            btnSendComment.setColorFilter(Color.BLUE) // Optional: Change the button color to indicate it's enabled
                        } else {
                            btnSendComment.isEnabled = false
                            btnSendComment.setColorFilter(Color.GRAY) // Optional: Change the button color to indicate it's disabled
                        }
                    }

                    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                        // Not used
                    }

                    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                        // Not used
                    }
                })

                // Handle the click event
                btnSendComment.setOnClickListener {
                    val comment = etReplyComment.text.toString()
                    sharedMainActivityViewModel.addComment(item.postId!!, comment)
                    sharedMainActivityViewModel.addCommentStatus.observe(viewLifecycleOwner){ addCommentStatus ->
                        if (addCommentStatus == 201) {
                            sharedMainActivityViewModel.getComments(item.postId)
                            etReplyComment.text = null
                        }
                    }
                }

            } else if (view.tag == "postMenu"){
                val deletePost = viewLayout.findViewById<TextView>(R.id.tvThirdLineDialog)
                deletePost.setOnClickListener{
                    dialog.dismiss()
                    deletePostDialog(item.postId!!)
                }

                val markFound = viewLayout.findViewById<TextView>(R.id.tvFirstLineDialog)
                markFound.setOnClickListener{
                    item.postId?.let { it1 -> sharedMainActivityViewModel.updateForum(it1) }
                    sharedMainActivityViewModel.updateForumStatus.observe(viewLifecycleOwner){
                        if (it == 200) {
                            Toast.makeText(requireContext(), "Post updated successfully", Toast.LENGTH_SHORT).show()
                            sharedMainActivityViewModel.getForums()
                            dialog.dismiss()
                        }else{
                            Toast.makeText(requireContext(), "Post update failed", Toast.LENGTH_SHORT).show()
                        }
                    }
                }

                val editPost = viewLayout.findViewById<TextView>(R.id.tvSecondLineDialog)
                editPost.setOnClickListener{
                    dialog.dismiss()
                }
            }

            show()


            val bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet)
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
            bottomSheetBehavior.isHideable = true
        }
    }

    private fun deletePostDialog(postId: Int){
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
            sharedMainActivityViewModel.deleteForum(postId)
            sharedMainActivityViewModel.deleteForumStatus.observe(viewLifecycleOwner){
                if (it == 200) {
                    Toast.makeText(requireContext(), "Post deleted successfully", Toast.LENGTH_SHORT).show()
                    sharedMainActivityViewModel.getForums()
                }else{
                    Toast.makeText(requireContext(), "Post deletion failed", Toast.LENGTH_SHORT).show()
                }
            }
            dialog.dismiss()
        }
        btnNo.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }
}