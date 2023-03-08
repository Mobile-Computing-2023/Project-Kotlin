package com.example.mobile_computing_project

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.mobile_computing_project.databinding.ActivityMainBinding
import com.example.mobile_computing_project.fragments.CartFragment
import com.example.mobile_computing_project.fragments.ComplaintFragment
import com.example.mobile_computing_project.fragments.MenuFragment
import com.example.mobile_computing_project.fragments.ProfileFragment

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        replaceFragment(MenuFragment())

        binding.bottomNavView.setOnItemSelectedListener {
            when(it.itemId){
                R.id.menu -> replaceFragment(MenuFragment())
                R.id.cart -> replaceFragment(CartFragment())
                R.id.complaint -> replaceFragment(ComplaintFragment())
                R.id.profile -> replaceFragment(ProfileFragment())
                else -> {}
            }
            true
        }
    }

    private fun replaceFragment(fragment: Fragment){
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frame_layout, fragment)
        fragmentTransaction.commit()
    }
}