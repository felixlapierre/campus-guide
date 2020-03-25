package com.example.campusguide.map

import androidx.fragment.app.FragmentActivity
import com.example.campusguide.Constants
import com.example.campusguide.map.infoWindow.BuildingClickListener
import com.example.campusguide.utils.BuildingHighlights
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng

class GoogleMapInitializer constructor(private val activity: FragmentActivity,
                                       private val wrapper: GoogleMapAdapter,
                                       private val mapId: String): OnMapReadyCallback {
    init {
        // Obtain the SupportMapFragment and get notified when the map is ready to be used
        val id = activity.resources.getIdentifier(mapId, "id", activity.packageName)
        val mapFragment = activity.supportFragmentManager.findFragmentById(id) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(map: GoogleMap?) {
        if(map != null) {
            wrapper.adapted = map
            map.uiSettings.isMyLocationButtonEnabled = false

            // Center the map on Hall building
            if(mapId == "maps_activity_map") {
                val hall = LatLng(45.497290, -73.578824)
                map.animateCamera(
                    CameraUpdateFactory.newLatLngZoom(
                        hall,
                        Constants.ZOOM_STREET_LVL
                    )
                )
            }
            BuildingHighlights(map, activity).addBuildingHighlights()
            map.setOnPolygonClickListener(BuildingClickListener(activity, map))
            map.setContentDescription("Google Maps Ready")

            val mapClickListener = MapClickListener(map, activity)
            map.setOnMapClickListener(mapClickListener)
        }
    }


}