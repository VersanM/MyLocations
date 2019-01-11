package com.mihaiv.test.mylocations.data.repository.locations

import android.annotation.SuppressLint
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MediatorLiveData
import com.mihaiv.test.mylocations.data.local.db.locations.Location
import com.mihaiv.test.mylocations.data.mapper.map
import com.mihaiv.test.mylocations.data.remote.response.locations.LocationRemoteModel
import com.mihaiv.test.mylocations.utils.Outcome
import com.mihaiv.test.mylocations.utils.SystemUtils
import com.mihaiv.test.mylocations.utils.mockData.remoteLocations
import io.reactivex.android.schedulers.AndroidSchedulers
import retrofit2.Response
import javax.inject.Inject

class LocationsRepository @Inject constructor(
    private val locationsLocalSource: LocationsLocalSource,
    private val locationsRemoteSource: LocationsRemoteSource,
    private val systemUtils: SystemUtils
) {
    private var shouldFetch =
        true // This variable should be changed according to our needs (when should we fetch data?!) - for now is faked

    private val locationsResultLiveData = MediatorLiveData<Outcome<List<Location>>>().apply {
        addSource(locationsLocalSource.getLocations()) { locations ->
            locations?.let {
                value = Outcome.loading(false)
                value = Outcome.success(it)
                shouldFetch = it.isEmpty()
            }
        }
    }

    fun getLocationsLiveData(): LiveData<Outcome<List<Location>>> = locationsResultLiveData

    fun triggerLocationsFetch() {
        if (shouldFetch) {
            locationsResultLiveData.value = Outcome.loading(true)
            fetchLocations()
        }
    }

    fun fetchFakeData() {
        val localLocationList = map(remoteLocations)
        locationsLocalSource.saveLocations(localLocationList)
    }

    @SuppressLint("CheckResult")
    private fun fetchLocations() {
        locationsRemoteSource.fetchLocations() // by default executed on IO thread - setup in ApiModule
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(this::handleServerResponse, this::handleServerError)
    }

    private fun handleServerResponse(response: Response<List<LocationRemoteModel>>) =
    // TODO : This a bad idea! made only for a fast solution. We should update the local database not delete it after receiving data from server (this way we may lose local updates)
        if (response.isSuccessful) {
            locationsLocalSource.deleteLocations()
            val localLocationList = map(response.body()!!)
            locationsLocalSource.saveLocations(localLocationList)
            shouldFetch = false
        } else {
            // TODO: handle error properly
            locationsResultLiveData.value = Outcome.failure(Throwable(response.errorBody().toString()))
        }



    private fun handleServerError(throwable: Throwable) {
        if (systemUtils.isNetworkUnavailable()) {
            locationsResultLiveData.value = Outcome.failure(Throwable("No Internet Connection"))
        } else {
            locationsResultLiveData.value = Outcome.failure(throwable)
        }
    }

    fun addNewLocation(location: Location) {
        locationsLocalSource.saveLocation(location)
        // TODO: Trigger a JobService to save the Location on server
        // TODO: use JobService in case there is no internet connection at this moment, so this way it will be added to the server when there is internet connection, even if the app is in background
        // TODO: cancel job (if there is one) when the location is deleted (no internet connection case)
    }

    fun deleteLocation(locationId: Int) {
        locationsLocalSource.deleteLocation(locationId)
        // TODO : delete the location on server (probably other parameters may be needed)
    }
}