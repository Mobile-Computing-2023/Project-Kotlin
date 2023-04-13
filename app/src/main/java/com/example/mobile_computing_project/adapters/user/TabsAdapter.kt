package com.example.mobile_computing_project.adapters.user

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.mobile_computing_project.fragments.ProfileComplaintsFragment
import com.example.mobile_computing_project.fragments.ProfileOrdersFragment

class TabsAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        // Return a NEW fragment instance in createFragment(int)
        return if (position == 0) {
            ProfileOrdersFragment()
        } else {
            ProfileComplaintsFragment()
        }
    }
}