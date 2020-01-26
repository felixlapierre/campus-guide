package com.example.campusguide

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
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

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
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

        // Update switch campus button text value now that map loads
        val switchCampusButton = findViewById<Button>(R.id.switchCampusButton)
        switchCampusButton.text = "loy"
        val switchCampusFAB: FloatingActionButton = findViewById(R.id.switchCampusButton)
        switchCampusFAB.setOnClickListener(switchCampus())
    }

    /**
     * Switch campus method.
     * Check what campus to switch to, recenter, and change the value of the campus name string
     * in values/strings.
     */
    fun switchCampus() {
        // get button so we can see it's current text
        val switchCampusButton = findViewById<Button>(R.id.switchCampusButton)

        fusedLocationClient.lastLocation.addOnSuccessListener(this) {location ->
            if(switchCampusButton.text.equals("loy")){
                switchCampusButton.text = "sgw"
                val loyCoord = LatLng(45.458153, -73.640490)
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(loyCoord, 17.0f))

            } else if(switchCampusButton.text.equals("sgw")){
                switchCampusButton.text = "loy"
                val sgwCoord = LatLng(45.495792, -73.578096)
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(sgwCoord, 17.0f))
            }
            // don't do anything if values are off
        }



    }
}
