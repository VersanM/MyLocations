package com.mihaiv.test.mylocations.di

import com.mihaiv.test.mylocations.screen.main.MainActivity
import com.mihaiv.test.mylocations.screen.main.MainFragmentsProvider
import com.mihaiv.test.mylocations.screen.main.MainModule
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBuilder {

    @ActivityScope
    @ContributesAndroidInjector(modules = [MainModule::class, MainFragmentsProvider::class])
    abstract fun provideMainActivityInjector(): MainActivity
}