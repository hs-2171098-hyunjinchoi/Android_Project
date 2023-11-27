package com.example.android_project

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.android_project.databinding.ActivityMainBinding
import com.example.android_project.navigation.ChatFragment
import com.example.android_project.navigation.HomeFragment
import com.example.android_project.navigation.MyFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // When you first run the app, there is no user,
        // so you switch to LoginActivity immediately.
        if (Firebase.auth.currentUser == null) {
            startActivity(
                Intent(this, LoginActivity::class.java)
            )
            finish()
        }
        val bottomNavigationView: BottomNavigationView = findViewById(R.id.bottomNavigationView)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction().replace(R.id.fragmentContainer, HomeFragment()).commit()
            bottomNavigationView.menu.findItem(R.id.action_home).isChecked = true
        }

        bottomNavigationView.setOnItemSelectedListener {item ->
            var selectedFragment: Fragment? = null

            when (item.itemId) {
                R.id.action_home -> selectedFragment = HomeFragment()
                R.id.action_chat -> selectedFragment = ChatFragment()
                R.id.action_my -> selectedFragment = MyFragment()
            }

            selectedFragment?.let {
                supportFragmentManager.beginTransaction().replace(R.id.fragmentContainer, it).commit()
            }

            true
        }
    }
}