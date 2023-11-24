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

class MyFragment : Fragment(){
    lateinit var binding: FragmentMyBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ) : View? {
        binding = FragmentMyBinding.inflate(inflater, container, false)
        val rootView = binding.root

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
}