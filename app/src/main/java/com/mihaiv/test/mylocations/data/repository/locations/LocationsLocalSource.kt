package com.mihaiv.test.mylocations.data.repository.locations

import com.mihaiv.test.mylocations.data.local.db.MyLocationsDatabase
import com.mihaiv.test.mylocations.data.local.db.locations.Location
import com.mihaiv.test.mylocations.utils.runOnIoThread
import javax.inject.Inject

class LocationsLocalSource @Inject constructor(private val database: MyLocationsDatabase) {

    fun saveLocations(locations: List<Location>) =
        runOnIoThread {
            database.locationDao().insertLocations(locations)
        }

    fun saveLocation(location: Location) =
        runOnIoThread {
            database.locationDao().insertLocation(location)
        }

    fun getLocations() = database.locationDao().getLocations()

    fun deleteLocations() =
        runOnIoThread {
            database.locationDao().deleteLocations()
        }

    fun updateLocation(lat: Double, lng: Double, tag: String, address: String) =
        runOnIoThread {
            database.locationDao().updateLocation(lat, lng, tag, address)
        }

    fun deleteLocation(locationId: Int) =
        runOnIoThread {
            database.locationDao().deleteLocation(locationId)
        }
}