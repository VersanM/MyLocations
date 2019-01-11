package com.mihaiv.test.mylocations.data.local.db.locations

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query

@Dao
interface LocationDao {

    @Query("SELECT * FROM locations")
    fun getLocations(): LiveData<List<Location>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertLocations(locations: List<Location>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertLocation(location: Location)

    @Query("DELETE FROM locations")
    fun deleteLocations()

    @Query("DELETE FROM locations WHERE id=:locationId")
    fun deleteLocation(locationId: Int)

    @Query("UPDATE locations SET tag=:tag, address=:address WHERE latitude=:lat AND longitude=:lng")
    fun updateLocation(lat: Double, lng: Double, tag: String, address: String)
}