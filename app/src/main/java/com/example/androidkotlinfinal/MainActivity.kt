package com.example.androidkotlinfinal

import android.Manifest.permission_group.ACTIVITY_RECOGNITION
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.androidkotlinfinal.databinding.ActivityMainBinding
import com.example.androidkotlinfinal.ui.map.LocationProvider
import com.example.androidkotlinfinal.ui.map.PermissionsManager
import com.google.android.gms.maps.*
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity(),OnMapReadyCallback{

    var mapFragment: MapFragment? = null

    companion object {
        private const val REQUEST_CODE_ACTIVITY_RECOGNITION = 2
    }

    private var presenter = MVP.MapPresenter(this)
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


        binding!!.startButton.setOnClickListener {
            startTracking()
        }
        binding!!.endButton.setOnClickListener {
            stopTracking()
        }
        presenter.onViewCreated()

    }
    

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        presenter.ui.observe(this) {ui->
            updateUi(ui);
        }

        presenter.onMapLoaded()
        //1
        locationProvider.liveLocation.observe(this) { latLng ->
            map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 14f))
        }

        //2
        permissionManager.requestUserLocation()

        map.uiSettings.isZoomControlsEnabled = true
    }

    private fun updateUi(ui:MVP.Ui){
        if(ui.currentLocation != null && ui.currentLocation != map.cameraPosition.target){
            map.isMyLocationEnabled = true
            map.animateCamera(CameraUpdateFactory.newLatLngZoom(ui.currentLocation, 14f))
        }
        binding?.kilometersNumber?.text = ui.formattedDistance
        binding?.stepsNumber?.text = ui.formattedSteps

    }
//    fun setupStepCounterListener() {
//        // 1
//        val sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
//        // 2
//        val stepCounterSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)
//        // 3
//        stepCounterSensor ?: return
//        // 4
//        sensorManager.registerListener(this@MainActivity, stepCounterSensor, SensorManager.SENSOR_DELAY_FASTEST)
//    }

    private fun startTracking(){
        presenter.startTracking()

        if (ContextCompat.checkSelfPermission(this, ACTIVITY_RECOGNITION)
            != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                arrayOf(ACTIVITY_RECOGNITION),
                99);
        }

    }


    private fun stopTracking(){
        presenter.stopTracking()
    }


}