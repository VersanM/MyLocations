package com.mihaiv.test.mylocations.screen.main.list

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import com.mihaiv.test.mylocations.screen.base.BaseViewModel

class LocationsListViewModel :
    BaseViewModel() {

    private val currentCoordinatesLiveData = MutableLiveData<Pair<Double, Double>>()

    private var latitude: Double? = null
    private var longitude: Double? = null

    fun getCurrentCoordinatesLiveData() = currentCoordinatesLiveData as LiveData<Pair<Double, Double>>

    fun onCurrentCoordinatesChanged(latitude: Double, longitude: Double) =
        if (this.latitude != latitude || this.longitude != longitude) {
            this.latitude = latitude
            this.longitude = longitude
            currentCoordinatesLiveData.postValue(Pair(latitude, longitude))
        } else Unit
}