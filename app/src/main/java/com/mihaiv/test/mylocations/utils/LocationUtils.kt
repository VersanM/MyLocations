package com.mihaiv.test.mylocations.utils

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.location.LocationManager
import android.os.Build
import android.provider.Settings
import android.support.v4.app.ActivityCompat
import android.support.v4.content.PermissionChecker
import com.mihaiv.test.mylocations.R

// Calculates distance between 2 points (latitude, longitude) in km
fun calculateDistanceBetweenCoordinates(
    currentLat: Double, currentLong: Double,
    targetLat: Double, targetLong: Double
): Double {
    val R = 6371 // Radius of the earth

    val latDistance = Math.toRadians(targetLat - currentLat)
    val lonDistance = Math.toRadians(targetLong - currentLong)
    val a = Math.sin(latDistance / 2) *
            Math.sin(latDistance / 2) + (Math.cos(Math.toRadians(currentLat)) *
            Math.cos(Math.toRadians(targetLat)) * Math.sin(lonDistance / 2) *
            Math.sin(lonDistance / 2))
    val c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a))
    var distance = R.toDouble() * c * 1000.0 // convert to meters

    distance = Math.pow(distance, 2.0)

    return Math.sqrt(distance) / 1000
} //= Math.sqrt(Math.pow(targetLat - currentLat, 2.0) + Math.pow(targetLong - currentLong, 2.0))

fun isLocationEnabled(context: Context): Boolean {
    val manager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    val isGpsProviderEnabled = manager.isProviderEnabled(LocationManager.GPS_PROVIDER)
    val isNetworkProviderEnabled = manager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    return isGpsProviderEnabled || isNetworkProviderEnabled
}

fun requestLocationAccess(activity: Activity) {
    buildLocationAccessDialog(activity, DialogInterface.OnClickListener { dialog, which ->
        val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
        activity.startActivity(intent)
    }).show()
}

fun buildLocationAccessDialog(
    activity: Activity,
    onOkClickListener: DialogInterface.OnClickListener
): android.app.AlertDialog.Builder {
    val resources = activity.resources
    val title = resources.getString(R.string.requesting_location_access)
    val message = resources.getString(R.string.do_you_want_to_open_location_settings)
    return buildLocationAccessDialog(activity, onOkClickListener, title, message)
}

fun buildLocationAccessDialog(
    activity: Activity,
    onOkClickListener: DialogInterface.OnClickListener,
    title: String,
    message: String
): AlertDialog.Builder {
    val builder = AlertDialog.Builder(activity)
    builder.setTitle(title)
    builder.setMessage(message)
    builder.setPositiveButton(android.R.string.ok, onOkClickListener)
    builder.setNegativeButton(android.R.string.no, null)
    builder.setCancelable(true)
    return builder
}

fun isFineLocationPermissionGranted(context: Context): Boolean {
    val isAndroidMOrHigher = Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
    val isFineLocationPermissionGranted = isGranted(context, Manifest.permission.ACCESS_FINE_LOCATION)

    return isAndroidMOrHigher && isFineLocationPermissionGranted
}

fun isGranted(context: Context, permission: String) =
    ActivityCompat.checkSelfPermission(context, permission) == PermissionChecker.PERMISSION_GRANTED