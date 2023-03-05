package com.example.mobile_computing_project

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.mobile_computing_project.databinding.ActivityMainBinding
import com.example.mobile_computing_project.databinding.ActivitySignUpBinding

class SignUpActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
    }
}