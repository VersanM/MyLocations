package com.mihaiv.test.mylocations.screen.main

import android.arch.lifecycle.ViewModel
import com.mihaiv.test.mylocations.data.repository.locations.LocationsRepository
import com.mihaiv.test.mylocations.screen.base.BaseViewModelFactory
import javax.inject.Inject

class MainViewModelFactory @Inject constructor(private val locationsRepository: LocationsRepository) : BaseViewModelFactory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>) = MainViewModel(locationsRepository) as T
}