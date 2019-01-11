package com.mihaiv.test.mylocations.screen.main

import com.mihaiv.test.mylocations.data.remote.service.LocationsService
import com.mihaiv.test.mylocations.data.repository.locations.LocationsRepository
import com.mihaiv.test.mylocations.di.ActivityScope
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit

@Module
class MainModule {

    @Provides
    @ActivityScope
    fun provideMainViewModelFactory(locationsRepository: LocationsRepository) =
        MainViewModelFactory(locationsRepository)

    @Provides
    @ActivityScope
    internal fun provideMainControllerNavigation(mainActivity: MainActivity): MainController.Navigation = mainActivity

    @Provides
    @ActivityScope
    internal fun provideMainControllerView(mainActivity: MainActivity): MainController.View = mainActivity

    @Provides
    @ActivityScope
    fun provideLocationsService(retrofit: Retrofit) = retrofit.create(LocationsService::class.java)
}