package com.mihaiv.test.mylocations.data.local.db

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import com.mihaiv.test.mylocations.data.local.db.locations.Location
import com.mihaiv.test.mylocations.data.local.db.locations.LocationDao

@Database(entities = [Location::class], version = 1, exportSchema = false)
abstract class MyLocationsDatabase : RoomDatabase() {

    abstract fun locationDao(): LocationDao
}