package com.example.mobile_computing_project.adapters.user

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.mobile_computing_project.fragments.CartFragment
import com.example.mobile_computing_project.fragments.MenuFragment

class TabsAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        // Return a NEW fragment instance in createFragment(int)
        if (position == 0) {
            return MenuFragment()
        }
        else {
            return CartFragment()
        }
    }
}