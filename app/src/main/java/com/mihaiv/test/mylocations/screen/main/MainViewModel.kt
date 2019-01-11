package com.mihaiv.test.mylocations.screen.main

import com.mihaiv.test.mylocations.data.local.db.locations.Location
import com.mihaiv.test.mylocations.data.repository.locations.LocationsRepository
import com.mihaiv.test.mylocations.screen.base.BaseViewModel
import javax.inject.Inject

class MainViewModel @Inject constructor(private val locationsRepository: LocationsRepository) : BaseViewModel() {

    private var selectedLocation: Location? = null

    fun getLocationsLiveData() = locationsRepository.getLocationsLiveData()

    fun fetchLocations() = locationsRepository.triggerLocationsFetch()

    fun addNewLocation(location: Location) =
        locationsRepository.addNewLocation(location)

    fun onSelectedLocation(location: Location) {
        this.selectedLocation = location
    }

    fun deleteSelectedLocation() {
        selectedLocation?.let {
            locationsRepository.deleteLocation(it.id)
        }
    }

    fun fetchFakeData() = locationsRepository.fetchFakeData()
}