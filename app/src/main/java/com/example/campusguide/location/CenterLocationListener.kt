package com.example.campusguide.location

import android.Manifest
import android.location.Location
import android.view.View
import com.example.campusguide.Constants
import com.example.campusguide.Map
import com.example.campusguide.utils.permissions.PermissionGrantedObserver
import com.example.campusguide.utils.permissions.PermissionsSubject
import com.google.android.gms.maps.model.LatLng

class CenterLocationListener constructor(
    private val map: Map,
    private val permissions: PermissionsSubject,
    private val locationProvider: LocationProvider
) : View.OnClickListener,
    PermissionGrantedObserver {
    init {
        permissions.addObserver(this)
    }

    private val locationPermission = Manifest.permission.ACCESS_FINE_LOCATION

    override fun onClick(v: View?) {
        // Center only if location permissions have been granted
        if (permissions.havePermission(locationPermission)) {
            goToCurrentLocation()
        } else {
            //Request location permission
            permissions.requestPermission(locationPermission)
        }
    }

    override fun onPermissionGranted(permission: String) {
        if(permission == locationPermission)
            goToCurrentLocation()
    }

    /**
     * Centers the map on the user's current location and places a marker.
     */
    private fun goToCurrentLocation() {
        locationProvider.getLocation {
            animateCurrentLocation(it)
        }
    }

    private fun animateCurrentLocation(location: Location) {
        val currentLatLng = LatLng(location.latitude, location.longitude)
        map.addMarker(currentLatLng, Constants.LOCATION_MARKER_TITLE)
        map.animateCamera(currentLatLng,
            Constants.ZOOM_STREET_LVL
        )
    }

}