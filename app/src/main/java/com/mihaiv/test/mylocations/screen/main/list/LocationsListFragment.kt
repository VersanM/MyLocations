package com.mihaiv.test.mylocations.screen.main.list

import android.Manifest
import android.annotation.SuppressLint
import android.arch.lifecycle.ViewModelProviders
import android.content.Context.LOCATION_SERVICE
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mihaiv.test.mylocations.BuildConfig
import com.mihaiv.test.mylocations.R
import com.mihaiv.test.mylocations.data.local.db.locations.Location
import com.mihaiv.test.mylocations.screen.base.BaseFragment
import com.mihaiv.test.mylocations.screen.main.MainActivity
import com.mihaiv.test.mylocations.screen.main.MainController
import com.mihaiv.test.mylocations.screen.main.MainViewModel
import com.mihaiv.test.mylocations.utils.*
import kotlinx.android.synthetic.main.empty_layout.*
import kotlinx.android.synthetic.main.fragment_locations_list.*
import javax.inject.Inject

class LocationsListFragment : BaseFragment<LocationsListViewModel, LocationsListViewModelFactory>() {

    @Inject
    lateinit var mainViewController: MainController.View

    @Inject
    lateinit var mainNavigationController: MainController.Navigation

    private lateinit var locationAdapter: LocationsAdapter

    @SuppressLint("LogNotTimber")
    private val onLocationChanged: (android.location.Location?) -> Unit = { location ->
        location?.let {
            Log.i(
                LocationsListFragment::class.java.canonicalName,
                "Location changed -> ${it.latitude} - ${it.longitude}"
            )
            if (::locationAdapter.isInitialized) {
                viewModel.onCurrentCoordinatesChanged(it.latitude, it.longitude)
            }
        }
    }

    companion object {
        private const val LOCATION_REFRESH_TIME = 5000L
        private const val LOCATION_REFRESH_DISTANCE = 20f
    }

    override fun getViewModelClass() = LocationsListViewModel::class.java

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_locations_list, container, false)
    }

    @SuppressLint("MissingPermission")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val itemDecorator = DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
        itemDecorator.setDrawable(ContextCompat.getDrawable(context!!, R.drawable.dotted_line_green)!!)
        locationsRecyclerView.addItemDecoration(itemDecorator)
        locationsRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        ViewModelProviders.of(activity!!).get(MainViewModel::class.java).fetchLocations()
        observeLocationsLiveData()
        observeCurrentCoordinatesLiveData()

        if (isFineLocationPermissionGranted(view.context)) {
            onLocationPermissionGranted()
        } else {
            requestFineLocationPermission()
        }
    }

    private fun observeLocationsLiveData() {
        ViewModelProviders.of(activity!!).get(MainViewModel::class.java).getLocationsLiveData().observeNonNull(this) {
            when (it) {
                is Outcome.Progress -> if (it.loading) {
                    locationsEmptyLayout.showLoading()
                }
                is Outcome.Failure -> showLayoutError(it.error)
                is Outcome.Success -> if (!it.data.isNullOrEmpty()) {
                    locationsEmptyLayout.visibility = View.GONE
                    displayLocations(it.data)
                } else if (!locationsEmptyLayout.isErrorVisible()) {
                    locationsEmptyLayout.showEmpty()
                }
            }
        }
    }

    private fun observeCurrentCoordinatesLiveData() {
        viewModel.getCurrentCoordinatesLiveData().observeNonNull(this) {
            locationAdapter.setCurrentUserCoordinates(it.first, it.second)
        }
    }

    private fun showLayoutError(error: Throwable) =
        locationsEmptyLayout.run {
            // TODO : handle specific errors!
            setError(resources.getString(R.string.error_something_went_wrong))
            emptyLayoutTryAgainButton.setOnClickListener {
                ViewModelProviders.of(activity!!).get(MainViewModel::class.java).fetchFakeData()
            }
            showError()
        }

    private fun requestFineLocationPermission() =
        requestPermissions(
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
            MainActivity.PERMISSIONS_REQUEST_CODE_ACCESS_FINE_LOCATION
        )

    private fun displayLocations(locations: List<Location>) {
        if (!::locationAdapter.isInitialized) {
            val isLocationEnabled =
                context != null && isFineLocationPermissionGranted(context!!) && isLocationEnabled(context!!)
            locationAdapter = LocationsAdapter(locations, isLocationEnabled) { location ->
                mainNavigationController.goToLocationDetails(location)
            }
        } else {
            locationAdapter.locations = locations
            locationAdapter.notifyDataSetChanged()
        }
        locationsRecyclerView.adapter = locationAdapter
    }

    override fun onResume() {
        super.onResume()
        mainViewController.setToolbarTitle(R.string.location_list_title)
        mainViewController.showAddMenuIcon()
        mainViewController.hideBackMenuIcon()
        mainViewController.hideEditMenuIcon()
        mainViewController.hideDeleteMenuIcon()

        if (context != null && isFineLocationPermissionGranted(context!!) && isLocationEnabled(context!!)) {
            onLocationEnabled()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == MainActivity.PERMISSIONS_REQUEST_CODE_ACCESS_FINE_LOCATION && !grantResults.isEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            onLocationPermissionGranted()
        } else if (!shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION) && context != null) {
            AlertDialog.Builder(context!!)
                .setMessage(R.string.location_permission_message)
                .setPositiveButton(R.string.allow_permission) { _, _ ->
                    startActivity(
                        Intent(
                            Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                            Uri.parse("package:" + BuildConfig.APPLICATION_ID)
                        )
                    )
                }
                .setNegativeButton(R.string.cancel, null)
                .show()
        }
    }

    private fun onLocationPermissionGranted() {
        if (activity != null && context != null && !isLocationEnabled(context!!)) {
            requestLocationAccess(activity!!)
        } else {
            onLocationEnabled()
        }
    }

    private fun onLocationEnabled() {
        val locationManager = activity?.getSystemService(LOCATION_SERVICE) as LocationManager
        locationManager.requestLocationUpdates(
            LocationManager.GPS_PROVIDER, LOCATION_REFRESH_TIME,
            LOCATION_REFRESH_DISTANCE, onLocationChanged
        )
        locationManager.requestLocationUpdates(
            LocationManager.NETWORK_PROVIDER, LOCATION_REFRESH_TIME,
            LOCATION_REFRESH_DISTANCE, onLocationChanged
        )
        if (::locationAdapter.isInitialized) {
            locationAdapter.isLocationAvailable = true
            locationAdapter.notifyDataSetChanged()
        }
    }

}