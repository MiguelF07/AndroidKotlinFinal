package com.example.androidkotlinfinal

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class ProfileActivity : AppCompatActivity() {
    val db = Firebase.firestore
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

        val kilometersData = findViewById<View>(R.id.kilometersDailyData) as TextView
        val stepsData = findViewById<View>(R.id.stepsDailyData) as TextView
        val timeData = findViewById<View>(R.id.totalDailyTimeData) as TextView

        getFromFirebase(object : OnDataReceiveCallback {
            override fun onDataReceived(kilometers: Double, steps: Double, time: Double) {
                kilometersData.text = kilometers.toString()
                stepsData.text = steps.toString()
                timeData.text = time.toString()
            }
        })
    }
    private fun getFromFirebase(callback: OnDataReceiveCallback) {
        println("Aqui")
        val database = FirebaseDatabase.getInstance()
        println("Aqui2")
        val ref = database.getReference()
        println("Aqui3")
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                println("Aqui4")
                println(dataSnapshot.child("/dailyData/2022-06-21/kilometersDaily").getValue())

                var kilometers = dataSnapshot.child("/dailyData/2022-06-21/kilometersDaily").getValue(Double::class.java)
                var steps = dataSnapshot.child("/dailyData/2022-06-21/stepsDaily").getValue(Double::class.java)
                var time = dataSnapshot.child("/dailyData/2022-06-21/time").getValue(Double::class.java)
                if (kilometers != null) {
                    println("aqui5")
                    if (steps != null) {
                        if (time != null) {
                            callback.onDataReceived(kilometers,steps,time)
                        }
                    }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }

    interface OnDataReceiveCallback {
        fun onDataReceived(kilometers: Double, steps: Double, time:Double)
    }

}