package com.example.androidkotlinfinal

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.androidkotlinfinal.LoginActivity
import com.example.androidkotlinfinal.R
import com.example.androidkotlinfinal.databinding.FragmentNotificationsBinding
import com.google.firebase.auth.FirebaseAuth

class ProfileActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        val logoutButton = findViewById<View>(R.id.logoutButton) as Button
        logoutButton.setOnClickListener { view: View? ->
            FirebaseAuth.getInstance().signOut()
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

        val routesButton = findViewById<View>(R.id.routeHistory) as Button
        routesButton.setOnClickListener { view: View? ->
            val intent = Intent(this, HistoryRoutesActivity::class.java)
            startActivity(intent)
        }
    }


}