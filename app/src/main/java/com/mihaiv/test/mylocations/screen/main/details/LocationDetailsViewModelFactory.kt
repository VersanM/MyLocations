package com.mihaiv.test.mylocations.screen.main.details

import android.arch.lifecycle.ViewModel
import com.mihaiv.test.mylocations.screen.base.BaseViewModelFactory

class LocationDetailsViewModelFactory : BaseViewModelFactory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>) = LocationDetailsViewModel() as T
}