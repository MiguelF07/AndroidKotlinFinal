package com.example.androidkotlinfinal.ui.map

import android.annotation.SuppressLint
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.SphericalUtil
import kotlin.math.roundToInt

@SuppressLint("MissingPermission")

class LocationProvider(private val activity: AppCompatActivity) {

    private val fusedLocation
            by lazy { LocationServices.getFusedLocationProviderClient(activity) }

    private val locations = mutableListOf<LatLng>()

    val liveLocation = MutableLiveData<LatLng>()
    private var distance = 0;
    val liveLocations = MutableLiveData<List<LatLng>>()
    val liveDistance = MutableLiveData<Int>()

    fun getUserLocation() {
        fusedLocation.lastLocation.addOnSuccessListener { location ->
            val locationLatLong = LatLng(location.latitude, location.longitude)
            locations.add(locationLatLong)
            liveLocation.value = locationLatLong
        }
    }

    fun trackUser() {
        //1
        val locationRequest = LocationRequest.create()
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.interval = 5000

        //2
        fusedLocation.requestLocationUpdates(locationRequest, locationCallback,
            Looper.getMainLooper())
    }

    fun stopTracking() {
        fusedLocation.removeLocationUpdates(locationCallback)
        locations.clear()
        distance = 0
    }

    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(result: LocationResult) {
            //1
            val currentLocation = result.lastLocation
            val latLng = currentLocation?.let { LatLng(it.latitude, currentLocation.longitude) }

            //2
            val lastLocation = locations.lastOrNull()

            //3
            if (lastLocation != null) {
                distance +=
                    SphericalUtil.computeDistanceBetween(lastLocation, latLng).roundToInt()
                liveDistance.value = distance
            }

            //4
            if (latLng != null) {
                locations.add(latLng)
            }
            liveLocations.value = locations
        }
    }
}