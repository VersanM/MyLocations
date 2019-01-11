package com.mihaiv.test.mylocations.utils

import android.net.ConnectivityManager
import javax.inject.Inject

class SystemUtils @Inject constructor(private val connectivityManager: ConnectivityManager) {

    fun isNetworkUnavailable(): Boolean {
        val activeNetwork = connectivityManager.activeNetworkInfo
        return activeNetwork == null || !activeNetwork.isConnected
    }
}
