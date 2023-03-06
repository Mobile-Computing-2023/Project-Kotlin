package com.example.mobile_computing_project

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.mobile_computing_project.databinding.ActivityHomeBinding
import com.example.mobile_computing_project.databinding.ActivityProfileBinding
import com.example.mobile_computing_project.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

private const val TAG = "ProfileActivity"

class ProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfileBinding
    private var signedInUser: User? = null
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        auth = Firebase.auth
        val db = Firebase.firestore

        db.collection("Users").document(auth.currentUser?.uid as String).get().addOnSuccessListener {
            signedInUser = it.toObject(User::class.java)!!
            Log.i(TAG, "Signed In User: $signedInUser")
            binding.tvUserInfo.text = signedInUser?.name
        }.addOnFailureListener {error ->
            Log.i(TAG, "Failure in fetching current user", error)
        }

    }
}