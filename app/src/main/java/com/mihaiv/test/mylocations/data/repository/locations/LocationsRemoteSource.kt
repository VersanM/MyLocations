package com.mihaiv.test.mylocations.data.repository.locations

import com.mihaiv.test.mylocations.data.remote.response.locations.LocationRemoteModel
import com.mihaiv.test.mylocations.data.remote.service.LocationsService
import io.reactivex.Flowable
import retrofit2.Response
import javax.inject.Inject

class LocationsRemoteSource @Inject constructor(private val locationsService: LocationsService) {

    fun fetchLocations(): Flowable<Response<List<LocationRemoteModel>>> = locationsService.getLocations()
}