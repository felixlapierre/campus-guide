package com.example.campusguide.map

import androidx.fragment.app.FragmentActivity
import com.example.campusguide.Constants
import com.example.campusguide.map.displayIndoor.OnZoomListener
import com.example.campusguide.utils.BuildingHighlights
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment

class GoogleMapInitializer constructor(
    private val activity: FragmentActivity,
    private val wrapper: GoogleMapAdapter,
    private val mapId: String,
    private val onPolygonClickListener: GoogleMap.OnPolygonClickListener? = null
) : OnMapReadyCallback {
    private var onMapReadyListener: (() -> Unit)? = null
    private var googleMap: GoogleMap? = null

    init {
        // Obtain the SupportMapFragment and get notified when the map is ready to be used
        val id = activity.resources.getIdentifier(mapId, "id", activity.packageName)
        val mapFragment = activity.supportFragmentManager.findFragmentById(id) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(map: GoogleMap?) {
        if (map != null) {
            googleMap = map
            wrapper.adapted = map
            map.uiSettings.isMyLocationButtonEnabled = false
            map.uiSettings.isIndoorLevelPickerEnabled = false
            map.isIndoorEnabled = false
            map.isBuildingsEnabled = false

            // Center the map on SGW Campus
            map.animateCamera(
                CameraUpdateFactory.newLatLngZoom(
                    Constants.SGW_COORDINATES,
                    Constants.ZOOM_STREET_LVL
                )
            )

            val zoomListener = OnZoomListener(wrapper)
            wrapper.setCameraMoveListener(zoomListener)

            BuildingHighlights(map, activity).addBuildingHighlights()

            if (onPolygonClickListener != null) {
                map.setOnPolygonClickListener(onPolygonClickListener)
            }
            map.setContentDescription("$mapId ready")

            onMapReadyListener?.invoke()
        }
    }

    fun setOnMapReadyListener(callback: () -> Unit) {
        if (googleMap != null)
            callback()
        else
            onMapReadyListener = callback
    }
}
