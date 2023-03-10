package com.example.mobile_computing_project

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Window
import android.widget.Toast
import com.example.mobile_computing_project.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

private const val TAG = "LoginActivity"
class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth
    private var isCanteen: Boolean = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Code to hide the action bar and title
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        supportActionBar?.hide()

        binding = ActivityLoginBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        isCanteen = intent.getBooleanExtra("isCanteen", false)
        if(isCanteen){
            Toast.makeText(this, "Welcome Canteen Vendor!", Toast.LENGTH_SHORT).show()
        }

        auth = Firebase.auth

//        if (auth.currentUser != null) {
////            goToHomeActivity()
//            goToMainActivity()
//        }

        binding.btnSignup.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }

        binding.btnLogin.setOnClickListener {
            binding.btnLogin.isEnabled = false
            val email = binding.etEmail.text.toString()
            val password = binding.etPassword.text.toString()
            if (email.isBlank() || password.isBlank()) {
                binding.btnLogin.isEnabled = true
                Toast.makeText(this, "Please fill the details!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            loginCallback(email, password)
        }
    }

    private fun loginCallback(email: String, password: String){
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                binding.btnLogin.isEnabled = true
                Toast.makeText(this, "Success!", Toast.LENGTH_SHORT).show()
//                goToHomeActivity()
                goToMainActivity()
            }
            else {
                binding.btnLogin.isEnabled = true
                Log.i(TAG, "Sign In Failed", task.exception)
                Toast.makeText(this, "Authentication Failed!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun goToHomeActivity(){
        Log.i(TAG, "goToHomeActivity")
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun goToMainActivity(){
        Log.i(TAG, "goToHomeActivity")
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}