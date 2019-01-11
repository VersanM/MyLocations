package com.mihaiv.test.mylocations.di

import android.app.Application
import android.arch.persistence.room.Room
import android.content.Context
import android.net.ConnectivityManager
import com.mihaiv.test.mylocations.MyLocationsApp
import com.mihaiv.test.mylocations.R
import com.mihaiv.test.mylocations.data.local.db.MyLocationsDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class ApplicationModule {

    @Provides
    @Singleton
    internal fun provideContext(application: Application): Context = application

    @Provides
    @Singleton
    internal fun provideApplication(application: Application): MyLocationsApp = application as MyLocationsApp

    @Provides
    @Singleton
    internal fun provideDao(context: Context) = Room.databaseBuilder<MyLocationsDatabase>(
        context.applicationContext,
        MyLocationsDatabase::class.java, "${context.getString(R.string.app_name)}_db"
    )
        .build()

    @Provides
    @Singleton
    internal fun provideConnectivityManager(application: Application): ConnectivityManager {
        return application.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    }

}