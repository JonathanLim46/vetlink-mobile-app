package com.example.vetlink.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.vetlink.R
import com.squareup.picasso.MemoryPolicy
import com.squareup.picasso.NetworkPolicy
import com.squareup.picasso.Picasso

class ForumPostListAdapter(
    private val context: Context,
    private val forumPostList: List<ForumPostList>,
    private val isPublic: Boolean
): RecyclerView.Adapter<ForumPostListAdapter.ForumPostViewHolder>(){

    private lateinit var listener: RecyclerViewClickListener<ForumPostList>

    class ForumPostViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val postMenu: ImageView = itemView.findViewById(R.id.menuOptionalPost)
        val postImageProfile: ImageView = itemView.findViewById(R.id.ivPostImageProfile)
        val postImagePets: ImageView = itemView.findViewById(R.id.ivPostImagePets)
        val postUsername: TextView = itemView.findViewById(R.id.tvPostUsername)
        val postStatus: TextView = itemView.findViewById(R.id.tvPostStatus)
        val postHeader: TextView = itemView.findViewById(R.id.tvPostHeader)
        val postDescription: TextView = itemView.findViewById(R.id.tvPostDescription)
        val postLastSeen: TextView = itemView.findViewById(R.id.tvLastSeenData)
        val postCharacteristics: TextView = itemView.findViewById(R.id.tvCharacteristicsData)
        val postComment: ImageView = itemView.findViewById(R.id.ivCommentPost)
        val postShare: ImageView = itemView.findViewById(R.id.ivSharePost)
    }

    fun setClickListener(clickListener: RecyclerViewClickListener<ForumPostList>){
        this.listener = clickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ForumPostViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.each_item_forum_post, parent, false)

        // HIDE, JIKA BUKAN PUNYA USER....
        if (isPublic == true){
            val Image = view.findViewById<ImageView>(R.id.menuOptionalPost)
            Image.visibility = View.GONE
        }else{
            val Image = view.findViewById<ImageView>(R.id.menuOptionalPost)
            Image.visibility = View.VISIBLE
        }

        return ForumPostViewHolder(view)
    }

    override fun getItemCount(): Int {
        return forumPostList.size
    }

    override fun onBindViewHolder(holder: ForumPostViewHolder, position: Int) {
        val forumPostList = forumPostList[position]

        if (forumPostList.postImageProfile != null) {
            Picasso.get()
                .load(forumPostList.postImageProfile)
                .into(holder.postImageProfile)
        } else {
            holder.postImageProfile.setImageResource(R.drawable.img_default_profile)
        }
        if (forumPostList.postImagePets != null) {
            Picasso.get().invalidate(forumPostList.postImagePets)
            Picasso.get()
                .load(forumPostList.postImagePets)
                .memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
                .networkPolicy(NetworkPolicy.NO_CACHE, NetworkPolicy.NO_STORE)
                .into(holder.postImagePets)
        } else {
            holder.postImagePets.setImageResource(R.drawable.icon_pets)
        }



        holder.postUsername.text = forumPostList.postUsername
        holder.postStatus.text = forumPostList.postStatus.replaceFirstChar { it.uppercaseChar() }
        when (forumPostList.postStatus.lowercase()) {
            "lost" -> holder.postStatus.setTextColor(ContextCompat.getColor(context, R.color.red))
            "found" -> holder.postStatus.setTextColor(ContextCompat.getColor(context, R.color.green))
        }

        if (forumPostList.postStatus.lowercase() == "found"){
            holder.postShare.visibility = View.GONE
        } else {
            holder.postShare.visibility = View.VISIBLE
        }

        holder.postHeader.text = forumPostList.postHeader
        holder.postDescription.text = forumPostList.postDescription
        holder.postLastSeen.text = forumPostList.postLastSeen
        holder.postCharacteristics.text = forumPostList.postCharacteristics

        holder.postMenu.tag = "postMenu"
        holder.postComment.tag = "postComment"
        holder.postShare.tag = "postShare"

        holder.postMenu.setOnClickListener{
            listener?.onItemClicke(it, forumPostList)
        }

        holder.postComment.setOnClickListener{
            listener?.onItemClicke(it, forumPostList)
        }

        holder.postShare.setOnClickListener{
            listener?.onItemClicke(it, forumPostList)
        }
    }
}