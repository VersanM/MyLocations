package com.mihaiv.test.mylocations.screen.main.list

import com.mihaiv.test.mylocations.di.FragmentScope
import dagger.Module
import dagger.Provides

@Module
class LocationsListModule {

    @Provides
    @FragmentScope
    fun provideLocationsListViewModelFactory() = LocationsListViewModelFactory()

}