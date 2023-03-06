package com.example.mobile_computing_project

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mobile_computing_project.adapters.MenuItemAdapter
import com.example.mobile_computing_project.databinding.ActivityHomeBinding
import com.example.mobile_computing_project.models.MenuItem
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

private const val TAG = "HomeActivity"
class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    private lateinit var menuItemAdapter: MenuItemAdapter
    private var menuItems: MutableList<MenuItem> = mutableListOf()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        menuItemAdapter = MenuItemAdapter(this, menuItems)
        binding.rvItems.adapter = menuItemAdapter
        binding.rvItems.layoutManager = LinearLayoutManager(this)

        val db = Firebase.firestore
        val menuReference = db.collection("Menu")
        menuReference.addSnapshotListener { snapshot, error ->
            if(error != null || snapshot == null){
                Log.i(TAG, "Error when querying items", error)
            }
            if (snapshot != null) {
                val menuList = snapshot.toObjects(MenuItem::class.java)
                menuItems.clear()
                menuItems.addAll(menuList)
                menuItemAdapter.notifyDataSetChanged()
                for (item in menuList){
                    Log.i(TAG, "Item $item")
                }
            }
        }
    }
}