package com.mihaiv.test.mylocations.data.local.prefs

import android.content.Context
import android.content.SharedPreferences
import com.mihaiv.test.mylocations.MyLocationsApp
import com.mihaiv.test.mylocations.R
import dagger.Module
import dagger.Provides
import javax.inject.Singleton


@Module
class AppPrefsModule {

    @Provides
    @Singleton
    internal fun provideSharedPreferences(application: MyLocationsApp): SharedPreferences =
        application.getSharedPreferences(application.getString(R.string.app_name), Context.MODE_PRIVATE)
}