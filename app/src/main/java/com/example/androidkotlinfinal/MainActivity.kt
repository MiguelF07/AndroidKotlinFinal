package com.example.androidkotlinfinal

import android.Manifest.permission.ACTIVITY_RECOGNITION
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.androidkotlinfinal.data.model.Stopwatch
import com.example.androidkotlinfinal.databinding.ActivityMainBinding
import com.example.androidkotlinfinal.ui.map.LocationProvider
import com.example.androidkotlinfinal.ui.map.PermissionsManager
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.PolylineOptions
import com.google.firebase.auth.FirebaseAuth
import java.util.*

class MainActivity : AppCompatActivity(),OnMapReadyCallback, SensorEventListener{


    private var stepsOnReboot = 0
    private var startMilliseconds = 0L
    private var didGet = false
    private var presenter = MVP.MapPresenter(this)
    private val locationProvider = LocationProvider(this)
    private val permissionManager = PermissionsManager(this, locationProvider)
    private lateinit var mainHandler:Handler
    private var stopwatch = Stopwatch(0)



    private var binding: ActivityMainBinding? = null
    private var mAuth: FirebaseAuth? = null
    private lateinit var map: GoogleMap
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainHandler = Handler(Looper.getMainLooper())
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
        val sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        val stepCounterSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)
        stepCounterSensor?.let {
            sensorManager.registerListener(this@MainActivity, it, SensorManager.SENSOR_DELAY_FASTEST)
        }

        val handler = Handler(Looper.myLooper()!!, )
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        presenter.ui.observe(this) {ui->
            updateUi(ui);
        }

        presenter.onMapLoaded()
        //1
        locationProvider.liveLocation.observe(this) { latLng ->
            map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 18f))
        }

        //2
        permissionManager.requestUserLocation()

        map.uiSettings.isZoomControlsEnabled = true
    }

    private fun updateUi(ui:MVP.Ui){
        if(ui.currentLocation != null && ui.currentLocation != map.cameraPosition.target){
            map.isMyLocationEnabled = true
            map.animateCamera(CameraUpdateFactory.newLatLngZoom(ui.currentLocation, 18f))
        }
        binding?.kilometersNumber?.text = ui.formattedDistance
        binding?.stepsNumber?.text = ui.formattedSteps

        drawRoute(ui.userPath)
    }
    private fun drawRoute(locations : List<LatLng>){
        val polylineOptions = PolylineOptions()
        map.clear()

        val points = polylineOptions.points
        points.addAll(locations)

        map.addPolyline(polylineOptions)

    }


    private fun startTracking(){
        presenter.startTracking()
        startMilliseconds= System.currentTimeMillis()
        map.clear()
        if (ContextCompat.checkSelfPermission(this, ACTIVITY_RECOGNITION)
            != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                arrayOf(ACTIVITY_RECOGNITION),
                99);
        }
        mainHandler.post(object : Runnable {
            override fun run() {
                stopwatch.setNewParameters(System.currentTimeMillis()-startMilliseconds)
                mainHandler.postDelayed(this, 1000)
                binding?.timeValue?.text = String.format("%2d:%2d:%2d", stopwatch.hours, stopwatch.minutes, stopwatch.seconds)
            }
        })

    }


    private fun stopTracking(){
        presenter.stopTracking()
        mainHandler.removeCallbacksAndMessages(null)
        stopwatch.setNewParameters(0)
    }

    override fun onSensorChanged(sensorEvent: SensorEvent?) {
        sensorEvent ?: return
        // Data 1: According to official documentation, the first value of the `SensorEvent` value is the step count
        sensorEvent.values.firstOrNull()?.let {
            if(!didGet){
                stepsOnReboot = it.toInt()
                didGet = true
            }
            presenter.ui.value = presenter.ui.value?.copy(formattedSteps = "${it.toInt()-stepsOnReboot}")
        }

    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {

    }


}