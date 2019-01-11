package com.mihaiv.test.mylocations.data.remote.service

import com.mihaiv.test.mylocations.data.remote.response.locations.LocationRemoteModel
import io.reactivex.Flowable
import retrofit2.Response
import retrofit2.http.GET

interface LocationsService {

    @GET("locations")
    fun getLocations(): Flowable<Response<List<LocationRemoteModel>>>

}