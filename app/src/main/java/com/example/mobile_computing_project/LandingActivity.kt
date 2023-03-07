package com.example.mobile_computing_project

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Window
import android.widget.LinearLayout
import com.example.mobile_computing_project.databinding.ActivityLandingBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

private const val TAG = "LandingActivity"

class LandingActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLandingBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var logoLinearLayout: LinearLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Code to hide the action bar and title
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        supportActionBar?.hide()

        auth = Firebase.auth

        if (auth.currentUser != null) {
            goToHomeActivity()
        }

        binding = ActivityLandingBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.btnLandingSigninUser.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            val isCanteen = false
            intent.putExtra("isCanteen", isCanteen)
            startActivity(intent)
        }

//        binding.btnLandingSigninCanteen.setOnClickListener {  }

        binding.btnLandingSignup.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }

//        This needs to change for canteen thing
        binding.btnLandingSigninCanteen.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            val isCanteen = true
            intent.putExtra("isCanteen", isCanteen)
            startActivity(intent)
        }
    }
    private fun goToHomeActivity(){
        Log.i(TAG, "goToHomeActivity")
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
        finish()
    }
}