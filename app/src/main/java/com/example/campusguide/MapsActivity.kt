package com.example.campusguide

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ToggleButton
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.gms.maps.*

import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Add a marker on Hall Building and move the camera
        val hall = LatLng(45.497290, -73.578824)
        mMap.addMarker(MarkerOptions().position(hall).title("Hall Building"))
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(hall, 17.0f))

        // Update switch campus button listener
        val switchCampusToggle: ToggleButton = findViewById(R.id.switchCampusButton)
        switchCampusToggle.setOnCheckedChangeListener{ _, isChecked ->
            switchCampus(isChecked)
        }
    }
    
    private fun switchCampus(isChecked: Boolean) {
        if(isChecked){
            // isChecked = true = on then we are at sgw campus, switch to loyola campus
            val loyCoord = LatLng(45.458153, -73.640490)
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(loyCoord, 17.0f))

        } else {
            val sgwCoord = LatLng(45.495792, -73.578096)
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(sgwCoord, 17.0f))
        }
    }
}
