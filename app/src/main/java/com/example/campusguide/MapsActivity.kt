package com.example.campusguide

import android.Manifest
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ToggleButton
import androidx.core.app.ActivityCompat
import com.example.campusguide.directions.CallbackDirectionsConfirmListener
import com.example.campusguide.directions.EmptyDirectionsGuard
import com.example.campusguide.directions.GetDirectionsDialogFragment
import com.example.campusguide.directions.Route
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
import com.google.android.gms.maps.model.Marker
import android.text.method.TextKeyListener.clear
import com.google.android.gms.maps.GoogleMap.OnMapClickListener
import androidx.core.app.ComponentActivity.ExtraData
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import CustomInfoWindowGoogleMap
import com.example.campusguide.utils.InfoWindowData
import database.ObjectBox

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var route: Route
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
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker on the Hall Building.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        route = Route(mMap, this)

        val hall = LatLng(45.497290, -73.578824)

        val info = InfoWindowData()
        info.symbol = "H"
        info.full_name = "Hall Building"
        info.address = "1234 Rue Maisonneuve"
        info.services = "ECA, CSU, dance floor"
        info.events = "Events"

        val customInfoWindow = CustomInfoWindowGoogleMap(this)
        mMap.setInfoWindowAdapter(customInfoWindow)

        //mMap.addMarker(MarkerOptions().position(hall).title("Hall Building").snippet("this is a string"))
        val markerOptions = MarkerOptions()
        markerOptions.position(hall)

        val m = mMap.addMarker(markerOptions)
        m.tag = info
        m.showInfoWindow()

        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(hall, Constants.ZOOM_STREET_LVL))
        buildingHighlights = BuildingHighlights(mMap)
        buildingHighlights.addBuildingHighlights()


        // Update switch campus button listener
        val switchCampusToggle: ToggleButton = findViewById(R.id.switchCampusButton)
        SwitchCampus(switchCampusToggle, mMap)

        onclickMarker(googleMap)
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

    fun onclickMarker(googleMap: GoogleMap) {

        val info = InfoWindowData()
        info.symbol = "H"
        info.full_name = "Hall Building"
        info.address = "1234 Rue Maisonneuve"
        info.services = "ECA, CSU, dance floor"
        info.events = "Events"

        val customInfoWindow = CustomInfoWindowGoogleMap(this)
        mMap.setInfoWindowAdapter(customInfoWindow)

        // Adding and showing marker while touching the GoogleMap
        googleMap.setOnMapClickListener { arg0 ->
            // Clears any existing markers from the GoogleMap
            googleMap.clear()

            // Creating an instance of MarkerOptions to set position
            val markerOptions = MarkerOptions()

            // Setting position on the MarkerOptions
            markerOptions.position(arg0)

            info.symbol = "B"
            info.full_name = "Building"
            info.address = "location" + arg0.latitude + ", " + arg0.longitude
            info.services = "some"
            info.events = "Events"

            //Set Custom InfoWindow Adapter
            mMap.setInfoWindowAdapter(customInfoWindow)

            // Animating to the currently touched position
            googleMap.animateCamera(CameraUpdateFactory.newLatLng(arg0))

            val marker = mMap.addMarker(markerOptions)
            marker.tag = info
            marker.showInfoWindow()
        }
    }
}
