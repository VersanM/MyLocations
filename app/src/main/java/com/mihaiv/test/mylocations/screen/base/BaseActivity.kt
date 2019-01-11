package com.mihaiv.test.mylocations.screen.base

import android.annotation.SuppressLint
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentTransaction
import com.mihaiv.test.mylocations.utils.hideKeyboard
import dagger.android.support.DaggerAppCompatActivity
import javax.inject.Inject

abstract class BaseActivity<VM : BaseViewModel, VMF : BaseViewModelFactory> : DaggerAppCompatActivity() {

    @Inject
    lateinit var viewModelFactory: VMF

    protected lateinit var viewModel: VM

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(getViewModelClass())
    }

    protected abstract fun getViewModelClass(): Class<VM>

    @SuppressLint("CommitTransaction")
    fun switchFragment(fragment: Fragment, container: Int, addToBackStack: Boolean) {
        hideKeyboard()
        val currentFragment = supportFragmentManager.findFragmentById(container)
        if (fragment.javaClass.isInstance(currentFragment)) {
            return
        }
        val transaction: FragmentTransaction = if (addToBackStack) {
            supportFragmentManager
                .beginTransaction()
                .replace(container, fragment)
                .addToBackStack(fragment.javaClass.name)
        } else {
            supportFragmentManager
                .beginTransaction()
                .replace(container, fragment)
        }
        transaction.commit()
    }

    protected fun clearStack() {
        for (i in 0 until supportFragmentManager.backStackEntryCount) {
            supportFragmentManager.popBackStack()
        }
    }
}