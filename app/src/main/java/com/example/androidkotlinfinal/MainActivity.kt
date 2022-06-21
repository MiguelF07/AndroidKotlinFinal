package com.example.androidkotlinfinal

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentTransaction
import androidx.navigation.Navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI.setupActionBarWithNavController
import androidx.navigation.ui.NavigationUI.setupWithNavController
import com.example.androidkotlinfinal.R
import com.example.androidkotlinfinal.databinding.ActivityMainBinding
import com.example.androidkotlinfinal.ui.map.LocationProvider
import com.example.androidkotlinfinal.ui.map.PermissionsManager
import com.google.android.gms.maps.*
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity(),OnMapReadyCallback {

    var mapFragment: MapFragment? = null
    val db = Firebase.firestore

    private val locationProvider = LocationProvider(this)
    private val permissionManager = PermissionsManager(this, locationProvider)



    private var binding: ActivityMainBinding? = null
    private var mAuth: FirebaseAuth? = null
    private lateinit var map: GoogleMap
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mAuth = FirebaseAuth.getInstance()
        if (mAuth!!.currentUser == null) {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        } else {
            Toast.makeText(this, "Already logged in", Toast.LENGTH_LONG).show()
        }
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding!!.root)

        val profileButton = findViewById<View>(R.id.profileBtn) as Button
        profileButton.setOnClickListener { view: View? ->
            val intent = Intent(this, ProfileActivity::class.java)
            startActivity(intent)
        }

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap

        //1
        locationProvider.liveLocation.observe(this) { latLng ->
            map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 14f))
        }

        //2
        permissionManager.requestUserLocation()

        map.uiSettings.isZoomControlsEnabled = true
    }
}