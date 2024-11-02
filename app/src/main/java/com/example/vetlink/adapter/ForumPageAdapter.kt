package com.example.vetlink.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.vetlink.fragment.ForumPrivateFragment
import com.example.vetlink.fragment.ForumPublicFragment

class ForumPageAdapter(fragment: FragmentActivity, val totalTabs : Int): FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int {
        return totalTabs
    }

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0 -> {
                ForumPublicFragment()
            }
            1 -> {
                ForumPrivateFragment()
            }
            else->{
                ForumPublicFragment()
            }
        }
    }


}