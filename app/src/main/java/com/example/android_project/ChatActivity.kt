package com.example.android_project

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.android_project.navigation.ChatFragment
import com.example.android_project.navigation.ItemDetailsFragment
import com.example.android_project.navigation.Message
import com.example.android_project.navigation.MessageAdapter
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore

class ChatActivity : AppCompatActivity() {
    private val auth = Firebase.auth
    private val firestore = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)
        val body = findViewById<EditText>(R.id.editTextBody)
        val send = findViewById<Button>(R.id.buttonSend)
        val receiver = intent.getStringExtra("receiver")


        send.setOnClickListener {
            if(receiver!=null) {
                sendMessage(receiver, body.text.toString())
                Toast.makeText(this, "전송이 완료되었습니다.", Toast.LENGTH_SHORT).show()

            }
            else
                Log.d(TAG,"intent 전달 오류")
        }
    }
    private fun sendMessage(receiver: String,message: String) {
        val currentUser = auth.currentUser
        if (currentUser != null) {
            val chatMessage = Message(auth.currentUser?.uid, receiver,message,System.currentTimeMillis())
            firestore.collection("chat").add(chatMessage)
            val intent = Intent(this, ListActivity::class.java)
            startActivity(intent)
        }
    }
    companion object {
        const val TAG = "Chat"
    }
}