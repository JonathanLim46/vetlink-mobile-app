package com.example.vetlink.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.vetlink.R
import org.w3c.dom.Text

class ForumPostListAdapter(private val forumPostList: List<ForumPostList>): RecyclerView.Adapter<ForumPostListAdapter.ForumPostViewHolder>(){

    private lateinit var listener: RecyclerViewClickListener<ForumPostList>

    class ForumPostViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val postMenu: ImageView = itemView.findViewById(R.id.menuOptionalPost)
        val postImageProfile: ImageView = itemView.findViewById(R.id.ivPostImageProfile)
        val postImagePets: ImageView = itemView.findViewById(R.id.ivPostImagePets)
        val postUsername: TextView = itemView.findViewById(R.id.tvPostUsername)
        val postLocation: TextView = itemView.findViewById(R.id.tvPostLocation)
        val postStatus: TextView = itemView.findViewById(R.id.tvPostStatus)
        val postHeader: TextView = itemView.findViewById(R.id.tvPostHeader)
        val postDescription: TextView = itemView.findViewById(R.id.tvPostDescription)
        val postLastSeen: TextView = itemView.findViewById(R.id.tvLastSeenData)
        val postCharacteristics: TextView = itemView.findViewById(R.id.tvCharacteristicsData)
        val postComment: ImageView = itemView.findViewById(R.id.ivCommentPost)
    }

    fun setClickListener(clickListener: RecyclerViewClickListener<ForumPostList>){
        this.listener = clickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ForumPostViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.each_item_forum_post, parent, false)

        // HIDE, JIKA BUKAN PUNYA USER....
//        val Image = view.findViewById<ImageView>(R.id.menuOptionalPost)
//        Image.visibility = View.GONE

        return ForumPostViewHolder(view)
    }

    override fun getItemCount(): Int {
        return forumPostList.size
    }

    override fun onBindViewHolder(holder: ForumPostViewHolder, position: Int) {
        val forumPostList = forumPostList[position]
        holder.postImageProfile.setImageResource(forumPostList.postImageProfile)
        holder.postImagePets.setImageResource(forumPostList.postImagePets)
        holder.postUsername.text = forumPostList.postUsername
        holder.postLocation.text = forumPostList.postLocation
        holder.postStatus.text = forumPostList.postStatus
        holder.postHeader.text = forumPostList.postHeader
        holder.postDescription.text = forumPostList.postDescription
        holder.postLastSeen.text = forumPostList.postLastSeen
        holder.postCharacteristics.text = forumPostList.postCharacteristics

        holder.postMenu.tag = "postMenu"
        holder.postComment.tag = "postComment"

        holder.postMenu.setOnClickListener{
            listener?.onItemClicke(it, forumPostList)
        }

        holder.postComment.setOnClickListener{
            listener?.onItemClicke(it, forumPostList)
        }

    }
}