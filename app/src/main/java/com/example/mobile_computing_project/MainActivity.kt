package com.example.mobile_computing_project

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.mobile_computing_project.databinding.ActivityMainBinding
import com.example.mobile_computing_project.fragments.*
import com.example.mobile_computing_project.models.CartItem
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
    var listInMainActivity: MutableList<CartItem> = mutableListOf()
    private var menuItem: MenuItem? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val colorDrawable = ColorDrawable(Color.parseColor("#FFFFFF"))
        supportActionBar?.setBackgroundDrawable(colorDrawable)

        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        auth = Firebase.auth
        val db = Firebase.firestore

        db.collection("Users").document(auth.currentUser?.uid as String).get().addOnSuccessListener { it ->
            signedInUser = it.toObject(User::class.java)!!
            Log.i(TAG, "Signed In User: $signedInUser")
            if(signedInUser?.canteen == true){
                binding.bottomNavCanteen.visibility = View.VISIBLE
                replaceFragment(OrdersFragment())
            }
            else{
                binding.bottomNavUser.visibility = View.VISIBLE
                replaceFragment(MenuFragment())
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
                R.id.profile -> {
                    replaceFragment(ProfileFragment())
                }
                else -> {}
            }
            true
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.logout, menu)
        menuItem = menu?.findItem(R.id.btn_logout)!!
        menuItem?.isVisible = false
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == R.id.btn_logout) {
            auth.signOut()
            goToLandingActivity()
            Toast.makeText(this, "Logged Out", Toast.LENGTH_SHORT).show()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun replaceFragment(fragment: Fragment){
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frame_layout, fragment)
        fragmentTransaction.commit()
    }

    fun showButtonInActionBar(show: Boolean) {
        menuItem?.isVisible = show
    }

    private fun goToLandingActivity(){
        Log.i(TAG, "goToLandingActivity")
        val intent = Intent(this, LandingActivity::class.java)
        startActivity(intent)
        finish()
    }
}