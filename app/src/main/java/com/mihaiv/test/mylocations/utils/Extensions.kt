package com.mihaiv.test.mylocations.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.Observer
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.text.TextWatcher
import android.view.inputmethod.InputMethodManager
import android.widget.EditText

fun AppCompatActivity.hideKeyboard() =
    run { (getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager) }
        .hideSoftInputFromWindow(currentFocus?.windowToken, 0)

fun <T> LiveData<T>.observeNonNull(lifecycleOwner: LifecycleOwner, action: (T) -> Unit) =
    observe(lifecycleOwner, Observer { it?.let(action) })

@SuppressLint("MissingPermission")
fun LocationManager.requestLocationUpdates(
    provider: String, minTime: Long, minDistance: Float,
    onLocationChanged: (location: android.location.Location?) -> Unit
) {
    requestLocationUpdates(provider, minTime, minDistance, object : LocationListener {
        override fun onLocationChanged(location: Location?) {
            onLocationChanged(location)
        }

        override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
        }

        override fun onProviderEnabled(provider: String?) {
        }

        override fun onProviderDisabled(provider: String?) {
        }
    })
}

fun EditText.addTextChangedListener(onTextChanged: (String) -> Unit) {
    addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {
            onTextChanged(text.toString())
        }
    })
}