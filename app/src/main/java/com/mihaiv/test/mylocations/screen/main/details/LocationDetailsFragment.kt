package com.mihaiv.test.mylocations.screen.main.details

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.mihaiv.test.mylocations.R
import com.mihaiv.test.mylocations.data.local.db.locations.Location
import com.mihaiv.test.mylocations.screen.base.BaseFragment
import com.mihaiv.test.mylocations.screen.main.MainController
import com.mihaiv.test.mylocations.screen.main.MainViewModel
import com.mihaiv.test.mylocations.utils.Outcome
import com.mihaiv.test.mylocations.utils.observeNonNull
import kotlinx.android.synthetic.main.fragment_location_details.*
import javax.inject.Inject


class LocationDetailsFragment : BaseFragment<LocationDetailsViewModel, LocationDetailsViewModelFactory>(),
    OnMapReadyCallback {

    @Inject
    lateinit var mainViewController: MainController.View

    private var googleMap: GoogleMap? = null

    override fun getViewModelClass() = LocationDetailsViewModel::class.java

    companion object {
        private const val LOCATION_PARAMETER_KEY = "LOCATION_PARAMETER_KEY"
        fun createFragment(location: Location) = LocationDetailsFragment().apply {
            arguments = Bundle().apply { putParcelable(LOCATION_PARAMETER_KEY, location) }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_location_details, container, false)
        val mapFragment = childFragmentManager.findFragmentById(R.id.locationDetailsMap) as SupportMapFragment
        mapFragment.getMapAsync(this)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val location = arguments?.getParcelable<Location>(LOCATION_PARAMETER_KEY)
        viewModel.onLocationSelected(location)

        observeSelectedLocationLiveData()
        observeAddMarkerLiveData()
        observeCenterMapLocationLiveData()
    }

    private fun observeLocationsLiveData() {
        ViewModelProviders.of(activity!!).get(MainViewModel::class.java).getLocationsLiveData().observeNonNull(this) {
            when (it) {
                is Outcome.Success -> if (!it.data.isNullOrEmpty()) {
                    viewModel.onLocationsReceived(it.data)
                }
            }
        }
    }

    private fun observeSelectedLocationLiveData() {
        viewModel.getSelectedLocationLiveData().observeNonNull(this) { location ->
            detailsTagTextView.text = location.tag
            latitudeTextView.text = String.format("%.5f", location.latitude)
            longitudeTextView.text = String.format("%.5f", location.longitude)
            addressTextView.text = location.address
            ViewModelProviders.of(activity!!).get(MainViewModel::class.java).onSelectedLocation(location)
        }
    }

    private fun observeAddMarkerLiveData() {
        viewModel.getAddMarkerLiveData().observeNonNull(this) { location ->
            addMarkerToMap(location)
        }
    }

    private fun observeCenterMapLocationLiveData() {
        viewModel.getCenterMapLiveData().observeNonNull(this) { location ->
            centerMapToLocation(location)
        }
    }

    override fun onResume() {
        super.onResume()
        mainViewController.setToolbarTitle(R.string.location_details_title)
        mainViewController.hideAddMenuIcon()
        mainViewController.showDeleteMenuIcon()
        mainViewController.showEditMenuIcon()
        mainViewController.showBackMenuIcon()
    }

    override fun onMapReady(googleMap: GoogleMap?) {
        this.googleMap = googleMap
        this.googleMap?.uiSettings?.isMapToolbarEnabled = false
        this.googleMap?.setOnMarkerClickListener { marker ->
            viewModel.onLocationSelected(marker.position.latitude, marker.position.longitude)
            false
        }
        observeLocationsLiveData()
        viewModel.onMapLoaded()
    }

    private fun addMarkerToMap(location: Location) {
        val coordinates = LatLng(location.latitude, location.longitude)
        val markerOptions = MarkerOptions()
        markerOptions.position(coordinates).title(location.tag).snippet(location.address)
        googleMap?.addMarker(markerOptions)
    }

    private fun centerMapToLocation(location: Location) {
        // For zooming automatically to the location of the marker
        val coordinates = LatLng(location.latitude, location.longitude)
        val cameraPosition = CameraPosition.Builder().target(coordinates).zoom(16f).build()
        googleMap?.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
    }
}