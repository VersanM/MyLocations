package com.mihaiv.test.mylocations.screen.main

import com.mihaiv.test.mylocations.di.FragmentScope
import com.mihaiv.test.mylocations.screen.main.details.LocationDetailsFragment
import com.mihaiv.test.mylocations.screen.main.details.LocationDetailsModule
import com.mihaiv.test.mylocations.screen.main.list.LocationsListFragment
import com.mihaiv.test.mylocations.screen.main.list.LocationsListModule
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Suppress("UNUSED")
@Module
abstract class MainFragmentsProvider {

    @FragmentScope
    @ContributesAndroidInjector(modules = [LocationsListModule::class])
    abstract fun provideLocationsListFragment(): LocationsListFragment

    @FragmentScope
    @ContributesAndroidInjector(modules = [LocationDetailsModule::class])
    abstract fun provideLocationDetailsFragment(): LocationDetailsFragment
}