package com.example.android_project

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val textView = findViewById<TextView>(R.id.textView)
        Firebase.auth.signInWithEmailAndPassword("1111@gmail.com", "111111")
            .addOnCompleteListener(this) { // it: Task<AuthResult!>
                if (it.isSuccessful) {
                    textView.text = "sign-in success ${Firebase.auth.currentUser?.uid}"

                } else {
                    textView.text = "sign-in failed"
                }
            }
    }
}