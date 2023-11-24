package com.example.android_project

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.android_project.databinding.ActivityLoginBinding
import com.example.android_project.databinding.ActivitySignUpBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.auth

class SignUpActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnCreate.setOnClickListener{
            val userEmail = binding.edtEmail.text.toString()
            val userPassword = binding.edtPassword.text.toString()
            val userName = binding.edtName.text.toString()
            val userDateOfBirth = binding.edtDateOfBirth.text.toString()

            // If any of edt items are empty, you can't proceed
            if(userEmail.isEmpty()||userPassword.isEmpty()||userName.isEmpty()||userDateOfBirth.isEmpty()){
                Toast.makeText(this, "모든 항목을 입력하세요", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if(userDateOfBirth.length!=8){
                binding.edtDateOfBirth.backgroundTintList =
                    ColorStateList.valueOf(ContextCompat.getColor(this, R.color.red))
                Toast.makeText(this,"올바른 생년월일 8자리를 입력하세요", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            else{
                binding.edtDateOfBirth.backgroundTintList = null
            }
            //TODO: 추후 계정정보 파이어베이스에 저장하는 코드 추가해야 함.

            doSignUp(userEmail, userPassword)
        }


    }
    private fun doSignUp(userEmail: String, password: String){
        Firebase.auth.createUserWithEmailAndPassword(userEmail, password)
            .addOnCompleteListener(this){
                // When sign up is successful
                if(it.isSuccessful){
                    startActivity(
                        Intent(this, ListActivity::class.java)
                    )
                    finish()
                }
                // When sign up fails
                else{ // Password should be at least 6 characters
                    Log.w("SignUpActivity", "signUpWithEmail", it.exception)
                    Toast.makeText(this, "중복된 이메일 계정입니다.", Toast.LENGTH_SHORT).show()
                }
            }
    }
}
