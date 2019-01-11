package com.mihaiv.test.mylocations.screen.main.details

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import com.mihaiv.test.mylocations.data.local.db.locations.Location
import com.mihaiv.test.mylocations.screen.base.BaseViewModel

class LocationDetailsViewModel : BaseViewModel() {

    private var selectedLocation: Location? = null
    private var locations: List<Location>? = null

    private val selectedLocationLiveData = MutableLiveData<Location>()
    private val addMarkerLiveData = MutableLiveData<Location>()
    private val centerMapLiveData = MutableLiveData<Location>()

    fun getSelectedLocationLiveData() = selectedLocationLiveData as LiveData<Location>

    fun getAddMarkerLiveData() = addMarkerLiveData as LiveData<Location>

    fun getCenterMapLiveData() = centerMapLiveData as LiveData<Location>

    fun onLocationSelected(location: Location?) {
        location?.let {
            this.selectedLocation = location
            selectedLocationLiveData.postValue(location)
        }
    }

    fun onLocationSelected(lat: Double, long: Double) {
        locations?.let {
            for (location in it) {
                if (location.latitude == lat && location.longitude == long) {
                    selectedLocationLiveData.postValue(location)
                    break
                }
            }
        }
    }

    fun onMapLoaded() {
        selectedLocation?.let {
            addMarkerLiveData.postValue(it)
            centerMapLiveData.postValue(it)
        }
    }

    fun onLocationsReceived(locations: List<Location>) {
        this.locations = locations
        for (location in locations) {
            addMarkerLiveData.value = location
        }
    }
}