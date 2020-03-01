package com.example.campusguide

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ToggleButton
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.fragment.app.FragmentActivity
import com.example.campusguide.directions.*
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.example.campusguide.utils.BuildingHighlights

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.maps.GeoApiContext
import database.ObjectBox

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var route: Route
    private lateinit var mGeoApiContext: GeoApiContext
    private lateinit var  buildingHighlights: BuildingHighlights

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        ObjectBox.init(this.applicationContext)

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
//        navigateButton.setOnClickListener {
//            val getDirectionsDialogFragment =
//                GetDirectionsDialogFragment(
//                    GetDirectionsDialogFragment.DirectionsDialogOptions(
//                        null, null,
//                        EmptyDirectionsGuard(this,
//                            CallbackDirectionsConfirmListener { start, end ->
//                                //Display the directions time
//                                route.set(start, end)
//                            })
//                    )
//                )
//            getDirectionsDialogFragment.show(supportFragmentManager, "directionsDialog")
//        }

        navigateButton.setOnClickListener {
            val chooseDirectionOptions = ChooseDirectionOptions(route)
            chooseDirectionOptions.show(supportFragmentManager, "directionsOptions")
        }

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
        route = Route(mMap, this)

        // Add a marker on Hall Building and move the camera
        val hall = LatLng(45.497290, -73.578824)
        mMap.addMarker(MarkerOptions().position(hall).title("Hall Building"))
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(hall, Constants.ZOOM_STREET_LVL))
        buildingHighlights = BuildingHighlights(mMap)
        buildingHighlights.addBuildingHighlights()

        // Update switch campus button listener
        val switchCampusToggle: ToggleButton = findViewById(R.id.switchCampusButton)
        SwitchCampus(switchCampusToggle, mMap)

        mGeoApiContext = GeoApiContext.Builder()
            .apiKey(getString(R.string.google_maps_key))
            .build()

    }

    companion object {
        private const val LOCATION_PERMISSION_ACCESS_CODE = 1
    }

    /**
     * Centers the map on the user's current location and places a marker.
     */
    private fun goToCurrentLocation() {
        fusedLocationClient.lastLocation.addOnSuccessListener(this) { location ->
            if(location != null) {
                val currentLatLng = LatLng(location.latitude, location.longitude)
                mMap.addMarker(MarkerOptions()
                    .position(currentLatLng)
                    .title("You are here.")
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)))
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, Constants.ZOOM_STREET_LVL))
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when(requestCode) {
            LOCATION_PERMISSION_ACCESS_CODE -> {
                if((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    goToCurrentLocation()
                }
                return
            }
            // Add switch case statements for other permissions (e.g. contacts or calendar) here
            else -> {
                // Ignore all other requests
            }
        }
    }
}
