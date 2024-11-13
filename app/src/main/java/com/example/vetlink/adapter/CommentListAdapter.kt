package com.example.vetlink.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.vetlink.R
import com.example.vetlink.data.model.comment.Comment

class CommentListAdapter(private val commentList: List<Comment>): RecyclerView.Adapter<CommentListAdapter.CommentViewHolder>(){

    class CommentViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val commentPhotoProfile: ImageView = itemView.findViewById(R.id.ivPhotoComment)
        val commentUsername: TextView = itemView.findViewById(R.id.tvUsernameComment)
        val commentBody: TextView = itemView.findViewById(R.id.tvBodyComment)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.each_item_row_comment, parent, false)
        return CommentViewHolder(view)
    }

    override fun getItemCount(): Int {
        return commentList.size
    }

    override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {
        val commentList = commentList[position]
        holder.commentPhotoProfile.setImageResource(R.drawable.img_default_profile)
        holder.commentUsername.text = commentList.user.username
        holder.commentBody.text = commentList.content
    }
}