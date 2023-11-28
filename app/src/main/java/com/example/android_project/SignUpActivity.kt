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
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore

class SignUpActivity : AppCompatActivity() {
    private val db: FirebaseFirestore = Firebase.firestore
    private val usersCollectionRef = db.collection("users")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnCreate.setOnClickListener {
            val userEmail = binding.edtEmail.text.toString()
            val userPassword = binding.edtPassword.text.toString()
            val userName = binding.edtName.text.toString()
            val userDateOfBirth = binding.edtDateOfBirth.text.toString()

            // If any of edt items are empty, you can't proceed
            if (userEmail.isEmpty() || userPassword.isEmpty() || userName.isEmpty() || userDateOfBirth.isEmpty()) {
                Toast.makeText(this, "모든 항목을 입력하세요", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            // 생년월일 8자리 강제
            if (userDateOfBirth.length != 8) {
                binding.edtDateOfBirth.backgroundTintList =
                    ColorStateList.valueOf(ContextCompat.getColor(this, R.color.red))
                Toast.makeText(this, "올바른 생년월일 8자리를 입력하세요", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            } else {
                binding.edtDateOfBirth.backgroundTintList = null
            }
            // 패스워드 6자리 이상
            if (userPassword.length < 6) {
                binding.edtPassword.backgroundTintList =
                    ColorStateList.valueOf(ContextCompat.getColor(this, R.color.red))
                Toast.makeText(this, "비밀번호를 6자리 이상 입력하세요", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            } else {
                binding.edtPassword.backgroundTintList = null
            }
            doSignUp(userEmail, userPassword, userName, userDateOfBirth)
        }


    }

    private fun doSignUp(
        userEmail: String,
        password: String,
        userName: String,
        userDateOfBirth: String
    ) {

        Firebase.auth.createUserWithEmailAndPassword(userEmail, password)
            .addOnCompleteListener(this) {
                // When sign up is successful
                if (it.isSuccessful) {
                    val currentUser = Firebase.auth.currentUser
                    val uid = currentUser?.uid
                    if(uid!=null){
                        addAccountInfo(userEmail, userName, userDateOfBirth, uid)
                    }
                    startActivity(
                        Intent(this, MainActivity::class.java)
                    )
                    finish()
                }
                // When sign up fails
                else { // Password should be at least 6 characters
                    Log.w("SignUpActivity", "signUpWithEmail", it.exception)
                    Toast.makeText(this, "중복된 이메일 계정입니다.", Toast.LENGTH_SHORT).show()
                }
            }

    }

    private fun addAccountInfo(
        userEmail: String,
        userName: String,
        userDateOfBirth: String,
        uid: String?
    ) {
        if (uid != null) {

            // 파이어스토어에 계정정보 추가
            val userInfo = hashMapOf(
                "email" to userEmail,
                "name" to userName,
                "birth" to userDateOfBirth
            )
            usersCollectionRef.document(uid).set(userInfo)
                .addOnSuccessListener {
                }
                .addOnFailureListener {
                    Log.e("컬렉션", "저장 실패", it)
                }
        }
    }
}
