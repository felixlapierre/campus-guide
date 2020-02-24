package com.example.campusguide

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
<<<<<<< HEAD
=======
import android.widget.ToggleButton
import androidx.core.app.ActivityCompat
import com.example.campusguide.directions.CallbackDirectionsConfirmListener
import com.example.campusguide.directions.EmptyDirectionsGuard
import com.example.campusguide.directions.GetDirectionsDialogFragment
import com.example.campusguide.directions.Route
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
>>>>>>> master

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import database.ObjectBox

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
<<<<<<< HEAD
    private lateinit var  buildingHighlights: BuildingHighlights
=======
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var route: Route
>>>>>>> master

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
<<<<<<< HEAD
        ObjectBox.init(this.applicationContext)
=======

        val currentLocationButton: FloatingActionButton = findViewById(R.id.currentLocationButton)
        currentLocationButton.setOnClickListener {
            //Check if location permission has been granted
            if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                goToCurrentLocation()
            } else {
                //Request location permission
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_PERMISSION_ACCESS_CODE)
            }
        }

        val navigateButton = findViewById<FloatingActionButton>(R.id.navigateButton)
        navigateButton.setOnClickListener {
            val getDirectionsDialogFragment =
                GetDirectionsDialogFragment(
                    GetDirectionsDialogFragment.DirectionsDialogOptions(
                        null, null,
                        EmptyDirectionsGuard(this,
                            CallbackDirectionsConfirmListener { start, end ->
                                //Display the directions time
                                route.set(start, end)
                            })
                    )
                )
            getDirectionsDialogFragment.show(supportFragmentManager, "directionsDialog")
        }

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
>>>>>>> master
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
        route = Route(mMap, this)

        // Add a marker on Hall Building and move the camera
        val hall = LatLng(45.497290, -73.578824)
        mMap.addMarker(MarkerOptions().position(hall).title("Hall Building"))
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(hall, 17.0f))
        buildingHighlights = BuildingHighlights(mMap)
        buildingHighlights.addBuildingHighlights()
    }
}
