package com.mihaiv.test.mylocations.data.mapper

import com.mihaiv.test.mylocations.data.local.db.locations.Location
import com.mihaiv.test.mylocations.data.remote.response.locations.LocationRemoteModel

fun map(remoteLocation: LocationRemoteModel) =
    Location(
        latitude = remoteLocation.lat, longitude = remoteLocation.long,
        tag = remoteLocation.tag, address = remoteLocation.address
    )

fun map(remoteLocationList: List<LocationRemoteModel>): List<Location> {
    val locations = mutableListOf<Location>()
    for (remoteLocation in remoteLocationList) {
        locations.add(map(remoteLocation))
    }
    return locations
}