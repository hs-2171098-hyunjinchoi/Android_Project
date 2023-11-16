package com.example.android_project

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.android_project.databinding.ActivityLoginBinding
import com.example.android_project.databinding.ActivitySignUpBinding

class SignUpActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)


    }
}