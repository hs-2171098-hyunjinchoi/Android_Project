package com.example.android_project.navigation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.android_project.R
import com.google.firebase.firestore.firestore
import com.google.firebase.Firebase
import com.google.firebase.auth.auth

data class Message(val sender:String?=null,val receiver: String?= null, val body:String? =null, val timestamp: Long = 0)
data class MyUser(val email:String?=null, val name:String?=null,val birth: String?=null)
class ChatFragment : Fragment() {
    private val firestore = Firebase.firestore
    private val auth = Firebase.auth
    private val messageList = mutableListOf<Message>()
    private lateinit var messageadapter : MessageAdapter
    private lateinit var messageRecyclerView : RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_chat, container, false)
        var email : String? = ""
        messageadapter = MessageAdapter(messageList)
        messageRecyclerView = view.findViewById(R.id.list)
        messageRecyclerView.layoutManager = LinearLayoutManager(context)
        messageRecyclerView.adapter = messageadapter
        firestore.collection("users").document(auth.currentUser!!.uid).get().addOnCompleteListener {task->
            if(task.isSuccessful){
                var user = task.result?.toObject(MyUser::class.java)
                firestore.collection("chat")
                    .orderBy("timestamp")
                    .addSnapshotListener { snapshot, error ->
                        if (error != null) {
                            // Handle error
                            return@addSnapshotListener
                        }
                        if(snapshot != null){
                            messageList.clear()
                            for (doc in snapshot!!.documents) {
                                val body = doc.getString("body")
                                val receiver = doc.getString("receiver")
                                val sender = doc.getString("sender")
                                val timestamp = doc.getLong("timestamp")
                                if(user?.email == receiver) {
                                    val message = Message(sender, receiver, body, timestamp!!)
                                    messageList.add(message)
                                }
                            }
                            messageadapter.notifyDataSetChanged()
                        }
                    }
            }

        }
        return view
    }
}

class MessageAdapter(private val messageList: List<Message>) : RecyclerView.Adapter<MessageAdapter.MessageViewHolder>() {
    inner class MessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var senderText = itemView.findViewById<TextView>(R.id.Sender)
        var messageText = itemView.findViewById<TextView>(R.id.Body)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        var itemView = LayoutInflater.from(parent.context).inflate(R.layout.list_chat, parent, false)
        return MessageViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        val message = messageList[position]
        Firebase.firestore.collection("users").document(message.sender.toString()).get().addOnCompleteListener {
            if(it.isSuccessful){
                var user1 = it.result?.toObject(MyUser::class.java)
                holder.senderText.text = user1?.name
                holder.messageText.text = message.body
            }
        }
    }

    override fun getItemCount(): Int {
        return messageList.size
    }
}