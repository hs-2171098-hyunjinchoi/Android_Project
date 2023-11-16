package com.example.android_project

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.android_project.databinding.ActivityMainBinding
import com.example.android_project.navigation.ChatFragment
import com.example.android_project.navigation.HomeFragment
import com.example.android_project.navigation.MyFragment
import com.google.android.material.bottomnavigation.BottomNavigationMenuView
import com.google.android.material.bottomnavigation.BottomNavigationView

class ListActivity : AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val bottomNavigationView: BottomNavigationView = findViewById(R.id.bottomNavigationView)

        supportFragmentManager.beginTransaction().replace(R.id.fragmentContainer, HomeFragment()).commit()

        bottomNavigationView.setOnItemSelectedListener { item ->
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