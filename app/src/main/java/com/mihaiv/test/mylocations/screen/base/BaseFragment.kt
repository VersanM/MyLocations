package com.mihaiv.test.mylocations.screen.base

import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import dagger.android.support.DaggerFragment
import javax.inject.Inject

abstract class BaseFragment<VM : BaseViewModel, VMF : BaseViewModelFactory> : DaggerFragment() {

    @Inject
    protected lateinit var viewModelFactory: VMF

    protected lateinit var viewModel: VM

    override fun onAttach(context: Context) {
        super.onAttach(context)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(getViewModelClass())
    }

    protected abstract fun getViewModelClass(): Class<VM>
}