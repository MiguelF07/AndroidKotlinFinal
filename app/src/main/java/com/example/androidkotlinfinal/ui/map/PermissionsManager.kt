package com.example.androidkotlinfinal.ui.map

import android.Manifest
import android.annotation.SuppressLint
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng

class PermissionsManager(activity: AppCompatActivity,
    private val locProvider: LocationProvider) {

    private val locPermissionProvider = activity.registerForActivityResult(
        ActivityResultContracts.RequestPermission()) { success ->
        if (success) {
            locProvider.getUserLocation()
        }
    }

    fun requestUserLocation() {
        locPermissionProvider.launch(Manifest.permission.ACCESS_FINE_LOCATION)
    }
}
