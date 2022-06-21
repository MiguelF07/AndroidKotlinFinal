package com.example.androidkotlinfinal.ui.map

import android.Manifest
import android.annotation.SuppressLint
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng

class PermissionsManager(
    activity: AppCompatActivity,
    private val locationProvider: LocationProvider) {

    //1
    private val locationPermissionProvider = activity.registerForActivityResult(
        ActivityResultContracts.RequestPermission()) { granted ->
        if (granted) {
            locationProvider.getUserLocation()
        }
    }

    //2
    fun requestUserLocation() {
        locationPermissionProvider.launch(Manifest.permission.ACCESS_FINE_LOCATION)
    }
}
