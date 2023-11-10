package com.example.android_project

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.android_project.databinding.ActivityLoginBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.signin.setOnClickListener {
            val userEmail = binding.userEmail.text.toString()
            val userPassword = binding.userPassword.text.toString()
            doSignIn(userEmail, userPassword)
        }
    }
    private fun doSignIn(userEmail: String, userPassword: String){
        Firebase.auth.signInWithEmailAndPassword(userEmail, userPassword)
            .addOnCompleteListener{
                // If you successfully sign-in, you return to MainActivity
                if(it.isSuccessful){
                    startActivity( // 추후에 판매글 보기 액티비티 생성하면 해당 액티비티로 변경할 부분입니다.
                        Intent(this, MainActivity::class.java)
                    )
                    finish()
                }
                else{
                    Log.w("LoginActivity", "signInWithEmail", it.exception)
                    Toast.makeText(this, "로그인 실패! 이메일과 비밀번호를 다시 확인하세요", Toast.LENGTH_SHORT).show()
                }
            }
    }
}