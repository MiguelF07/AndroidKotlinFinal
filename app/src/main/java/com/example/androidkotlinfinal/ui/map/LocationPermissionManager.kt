package com.example.androidkotlinfinal.ui.map

import android.Manifest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity

class LocationPermissionManager(activity: AppCompatActivity,
                                private val locProvider: Provider) {

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
