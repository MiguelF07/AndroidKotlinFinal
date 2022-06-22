package com.example.androidkotlinfinal

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import com.example.androidkotlinfinal.ui.map.Provider
import com.example.androidkotlinfinal.ui.map.LocationPermissionManager
import com.google.android.gms.maps.model.LatLng

class MapUi {

    data class Ui(
        val uiSteps : String,
        val uiDistance: String,
        val currentLocation : LatLng?,
        val  pathing : List<LatLng>
        ) {
        companion object {
            val EMPTY = Ui(
                uiSteps = "0",
                uiDistance = "0",
                currentLocation = null,
                pathing = emptyList()
            )
        }
    }

    class MapUi (private val activity:AppCompatActivity){
        val ui = MutableLiveData(Ui.EMPTY)
        private val locationProvider = Provider(activity)
        private val permissionsManager = LocationPermissionManager(activity, locationProvider)
        fun onViewCreated(){
            locationProvider.liveLocations.observe(activity) { locations ->
                val current = ui.value
                ui.value = current?.copy(pathing = locations)
            }
            locationProvider.liveLocation.observe(activity) { newLocation ->
                val current = ui.value
                ui.value = current?.copy(currentLocation = newLocation)
            }
            locationProvider.liveDistance.observe(activity) { distance ->
                val current = ui.value
                val formattedDistance = activity.getString(R.string.distance_value, distance)
                ui.value = current?.copy(uiDistance=formattedDistance)
            }
        }

        fun onMapLoaded(){
            permissionsManager.requestUserLocation()
        }
        fun startTracking(){
            locationProvider.startTracking()
        }
        fun stopTracking(){
            locationProvider.stopTracking()
            ui.value = Ui.EMPTY
        }
    }




}