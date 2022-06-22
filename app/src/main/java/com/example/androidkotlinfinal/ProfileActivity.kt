package com.example.androidkotlinfinal

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class ProfileActivity : AppCompatActivity() {
    val db = Firebase.firestore
    private var mAuth: FirebaseAuth? = null
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

        mAuth = FirebaseAuth.getInstance()
        val routesButton = findViewById<View>(R.id.routeHistory) as Button
        routesButton.setOnClickListener { view: View? ->
            val intent = Intent(this, HistoryRoutesActivity::class.java)
            startActivity(intent)
        }

        val kilometersData = findViewById<View>(R.id.kilometersDailyData) as TextView
        val stepsData = findViewById<View>(R.id.stepsDailyData) as TextView
        val timeData = findViewById<View>(R.id.totalTimeData) as TextView


        var firestore: FirebaseFirestore
        firestore = FirebaseFirestore.getInstance()
        val docIdRef = mAuth!!.uid?.let { db.collection("users").document(it) }
        docIdRef!!.get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val document = task.result
                println("PRINTT")
                println(document)
                document.data?.get("kilometersDaily")
                kilometersData.text = document.data?.get("kilometersDaily").toString()
                stepsData.text = document.data?.get("stepsDaily").toString()
                timeData.text = document.data?.get("timeDaily").toString()


            } else {
                Log.d(mAuth!!.uid, "Failed with: ", task.exception)
            }
        }

    }



    interface OnDataReceiveCallback {
        fun onDataReceived(kilometers: Double, steps: Double, time:Double)
    }




}