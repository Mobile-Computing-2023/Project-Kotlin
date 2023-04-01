package com.example.mobile_computing_project

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Window
import android.widget.Toast
import com.example.mobile_computing_project.databinding.ActivitySignUpBinding
import com.example.mobile_computing_project.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

private const val TAG = "SignUpActivity"
class SignUpActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpBinding
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Code to hide the action bar and title
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        supportActionBar?.hide()

        binding = ActivitySignUpBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        auth = Firebase.auth

        if (auth.currentUser != null) {
            goToMainActivity()
        }

        binding.btnSignup.setOnClickListener {
            binding.btnSignup.isEnabled = false
            val name = binding.etName.text.toString()
            val email = binding.etEmail.text.toString()
            val password = binding.etPassword.text.toString()
            if (email.isBlank() || password.isBlank() || name.isBlank()) {
                binding.btnSignup.isEnabled = true
                Toast.makeText(this, "Please fill the details!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            signUpCallback(name,email,password)
        }
    }

    private fun signUpCallback(name:String, email: String, password: String){
        val db = Firebase.firestore
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "createUserWithEmail:success")
                    val user = auth.currentUser
                    var uid = "";
                    user.let {
                        uid = it?.uid.toString()
                    }
                    val userModel  = User(uid, name, email)
                    db.collection("Users").document(uid).set(userModel)
                    goToMainActivity()
                }
                else {
                    binding.btnSignup.isEnabled = true
                    Log.i(TAG, "Sign Up Failed", task.exception)
                    Toast.makeText(this, "Authentication Failed!", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun goToMainActivity(){
        Log.i(TAG, "goToMainActivity")
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}