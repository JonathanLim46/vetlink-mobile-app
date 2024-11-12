package com.example.vetlink.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.vetlink.data.model.forums.Forum
import com.example.vetlink.fragment.ForumPrivateFragment
import com.example.vetlink.fragment.ForumPublicFragment

class ForumPageAdapter(
    fragment: FragmentActivity,
    val totalTabs : Int,
    val otherForums: List<Forum>,
    val userForums: List<Forum>
): FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int {
        return totalTabs
    }

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0 -> {
                ForumPublicFragment.newInstance(otherForums)
            }
            1 -> {
                ForumPrivateFragment.newInstance(userForums)
            }
            else->{
                ForumPublicFragment()
            }
        }
    }


}