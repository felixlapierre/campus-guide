package com.example.campusguide.map

import android.graphics.Bitmap
import android.graphics.Picture
import androidx.fragment.app.FragmentActivity
import com.example.campusguide.Constants
import com.example.campusguide.R
import com.example.campusguide.utils.BuildingHighlights
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import kotlinx.android.synthetic.main.activity_maps.*

class GoogleMapInitializer constructor(private val activity: FragmentActivity, private val wrapper: GoogleMapAdapter): OnMapReadyCallback {
    init {
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = activity.supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(map: GoogleMap?) {
        if(map != null) {
            wrapper.adapted = map
            map.uiSettings.isMyLocationButtonEnabled = false
            map.uiSettings.isIndoorLevelPickerEnabled = false
            map.isIndoorEnabled = false;
            map.isBuildingsEnabled = false


            // Center the map on Hall building
            val hall = LatLng(45.497290, -73.578824)
            map.animateCamera(CameraUpdateFactory.newLatLngZoom(hall,
                Constants.ZOOM_STREET_LVL
            ))


            val h = LatLng(45.4972695, -73.57894175)

            map.addGroundOverlay(
                GroundOverlayOptions()
                    .image(BitmapDescriptorFactory.fromAsset("Hall7.bmp"))
                    .position(h, 68F, 63F).bearing(124F)
                    .zIndex(5F)
                    .visible(true)
                    )

            BuildingHighlights(map).addBuildingHighlights()

            map.setContentDescription("Google Maps Ready")
        }
    }
}