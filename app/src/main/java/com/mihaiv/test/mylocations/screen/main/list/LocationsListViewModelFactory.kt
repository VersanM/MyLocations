package com.mihaiv.test.mylocations.screen.main.list

import android.arch.lifecycle.ViewModel
import com.mihaiv.test.mylocations.screen.base.BaseViewModelFactory

class LocationsListViewModelFactory : BaseViewModelFactory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>) = LocationsListViewModel() as T
}