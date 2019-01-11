package com.mihaiv.test.mylocations.data.remote.response.locations

data class LocationRemoteModel(
    val lat: Double,
    val long: Double,
    val tag: String,
    val address: String
)