package com.mihaiv.test.mylocations.di

import android.app.Application
import com.mihaiv.test.mylocations.MyLocationsApp
import com.mihaiv.test.mylocations.data.local.prefs.AppPrefsModule
import com.mihaiv.test.mylocations.data.remote.ApiModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(modules = [AndroidInjectionModule::class,
    AndroidSupportInjectionModule::class,
    ApplicationModule::class,
    ActivityBuilder::class,
    ApiModule::class,
    AppPrefsModule::class
])
interface AppComponent : AndroidInjector<DaggerApplication> {

    fun inject(myLocationsApp: MyLocationsApp)

    override fun inject(instance: DaggerApplication)

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun application(application: Application): Builder

        fun build(): AppComponent
    }
}