package com.example.android_project.navigation

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.example.android_project.LoginActivity
import com.example.android_project.MainActivity
import com.example.android_project.databinding.FragmentMyBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore

class MyFragment : Fragment(){
    lateinit var binding: FragmentMyBinding
    private val db : FirebaseFirestore = Firebase.firestore
    private val usersCollectionRef = db.collection("users")
    private var userName: String? = null
    private var userEmail: String? = null
    private var userBirth: String? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ) : View? {
        binding = FragmentMyBinding.inflate(inflater, container, false)
        val rootView = binding.root

        loadUserInfo()

        binding.btnLogout.setOnClickListener{
            logout()
        }
        return rootView
    }
    private fun logout(){

        Firebase.auth.signOut()

        val intent = Intent(getActivity(), LoginActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)

        Log.d("currentuser 확인", Firebase.auth.currentUser?.uid ?: "no user")
    }
    private fun loadUserInfo(){
        val currentUser = Firebase.auth.currentUser
        val uid = currentUser?.uid

        if(uid!=null){
            usersCollectionRef.document(uid).get() // document 식별은 uid로 가능
                .addOnSuccessListener {
                    if(it!=null && it.exists()){
                        // 파이어스토어에서 유저정보 가져오기
                        userName = it.getString("name")
                        userEmail = it.getString("email")
                        userBirth = it.getString("birth")
                        
                        // UI 업데이트
                        binding.userNameLabel.text = "${binding.userNameLabel.text} $userName"
                        binding.userEmailLabel.text = "${binding.userEmailLabel.text} $userEmail"
                        binding.userBirthLabel.text = "${binding.userBirthLabel.text} $userBirth"
                    }else{
                        Log.d("MyFragment", "user의 document가 존재하지 않음")
                    }
                }
                .addOnFailureListener{

                }
        }else{
            Log.d("MyFragment", "로그인 상태가 아님")
        }
    }
}