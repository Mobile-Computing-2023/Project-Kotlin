package com.example.mobile_computing_project

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import com.example.mobile_computing_project.databinding.ActivityMainBinding
import com.example.mobile_computing_project.fragments.*
import com.example.mobile_computing_project.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

private const val TAG = "MainActivity"
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var signedInUser: User? = null
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val colorDrawable = ColorDrawable(Color.parseColor("#FFFFFF"))
        supportActionBar?.setBackgroundDrawable(colorDrawable)

        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        replaceFragment(MenuFragment())

        auth = Firebase.auth
        val db = Firebase.firestore

        db.collection("Users").document(auth.currentUser?.uid as String).get().addOnSuccessListener { it ->
            signedInUser = it.toObject(User::class.java)!!
            Log.i(TAG, "Signed In User: $signedInUser")
            if(signedInUser?.canteen == true){
                binding.bottomNavCanteen.visibility = View.VISIBLE
            }
            else{
                binding.bottomNavUser.visibility = View.VISIBLE
            }
        }.addOnFailureListener {error ->
            Log.i(TAG, "Failure in fetching current user", error)
        }

        binding.bottomNavCanteen.setOnItemSelectedListener {
            when(it.itemId){
                R.id.menu -> replaceFragment(CanteenMenuFragment())
                R.id.orders -> replaceFragment(OrdersFragment())
                R.id.complaint -> replaceFragment(CanteenComplaintFragment())
                R.id.specials -> replaceFragment(SpecialsFragment())
                else -> {}
            }
            true
        }

        binding.bottomNavUser.setOnItemSelectedListener {
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