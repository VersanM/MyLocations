package com.mihaiv.test.mylocations.screen.main.details

import com.mihaiv.test.mylocations.di.FragmentScope
import dagger.Module
import dagger.Provides

@Module
class LocationDetailsModule {

    @Provides
    @FragmentScope
    fun provideLocationDetailsViewModelFactory() = LocationDetailsViewModelFactory()
}