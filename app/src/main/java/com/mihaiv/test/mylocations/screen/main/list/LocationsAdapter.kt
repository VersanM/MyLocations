package com.mihaiv.test.mylocations.screen.main.list

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mihaiv.test.mylocations.R
import com.mihaiv.test.mylocations.data.local.db.locations.Location
import com.mihaiv.test.mylocations.utils.calculateDistanceBetweenCoordinates
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_location.*

class LocationsAdapter(
    var locations: List<Location>,
    var isLocationAvailable: Boolean = false,
    private val onLocationClicked: (Location) -> Unit
) : RecyclerView.Adapter<LocationsAdapter.ViewHolder>() {

    private var currentLatitude: Double? = null
    private var currentLongitude: Double? = null

    fun setCurrentUserCoordinates(latitude: Double, longitude: Double) =
        if (currentLatitude == null || currentLatitude != latitude) {
            currentLatitude = latitude
            currentLongitude = longitude
            notifyDataSetChanged()
        } else Unit

    override fun onCreateViewHolder(parent: ViewGroup, position: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_location, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount() = locations.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindData(locations[position], onLocationClicked)
    }

    inner class ViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView), LayoutContainer {
        fun bindData(location: Location, onLocationClicked: (Location) -> Unit) {
            locationTagTextView.text = location.tag
            if (currentLatitude == null || currentLongitude == null) {
                if (isLocationAvailable) {
                    locationDistanceTextView.text =
                            containerView.context.getString(R.string.calculating_distance_message)
                } else {
                    locationDistanceTextView.text =
                            containerView.context.getString(R.string.not_available_distance_message)
                }
            } else {
                val distanceInKm = calculateDistanceBetweenCoordinates(
                    currentLatitude!!, currentLongitude!!,
                    location.latitude, location.longitude
                )
                val formattedDistance = String.format("%.5f km", distanceInKm)
                locationDistanceTextView.text = formattedDistance
            }
            locationItemLayout.setOnClickListener {
                onLocationClicked(location)
            }
        }
    }
}