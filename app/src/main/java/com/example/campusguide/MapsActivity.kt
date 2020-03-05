package com.example.campusguide

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ToggleButton
import androidx.appcompat.app.AppCompatActivity
import com.example.campusguide.directions.ChooseDirectionOptions
import com.example.campusguide.directions.Route
import com.example.campusguide.location.CenterLocationListener
import com.example.campusguide.location.FusedLocationProvider
import com.example.campusguide.search.Search
import com.example.campusguide.utils.permissions.Permissions
import com.example.campusguide.utils.BuildingHighlights
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.libraries.places.api.Places
import com.google.android.material.floatingactionbutton.FloatingActionButton
import database.ObjectBox
import kotlinx.android.synthetic.main.activity_maps.*

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private val map = GoogleMapAdapter()
    private lateinit var mMap: GoogleMap
    private lateinit var route: Route
    private lateinit var buildingHighlights: BuildingHighlights
    private val permissions = Permissions(this)
    private val search = Search(this, map)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        val currentLocationButton: FloatingActionButton = findViewById(R.id.currentLocationButton)
        currentLocationButton.setOnClickListener(
            CenterLocationListener(
                map,
                permissions,
                FusedLocationProvider(this)
            )
        )

        ObjectBox.init(this.applicationContext)
      
        if (!Places.isInitialized())
            Places.initialize(applicationContext, getString(R.string.google_maps_key))
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
        map.adapted = googleMap
        mMap = googleMap
        googleMap.uiSettings.isMyLocationButtonEnabled = false
        route = Route(mMap, this)

        // Add a marker on Hall Building and move the camera
        val hall = LatLng(45.497290, -73.578824)
        mMap.addMarker(MarkerOptions().position(hall).title("Hall Building"))
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(hall, Constants.ZOOM_STREET_LVL))
        buildingHighlights = BuildingHighlights(mMap)
        buildingHighlights.addBuildingHighlights()

        // Update switch campus button listener
        val switchCampusToggle: ToggleButton = findViewById(R.id.switchCampusButton)
        SwitchCampus(switchCampusToggle, mMap, campus_name)

        setButtonListeners()
        mMap.setContentDescription("Google Maps Ready")
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        requestedPermissions: Array<out String>,
        grantResults: IntArray
    ) {
        permissions.onRequestPermissionsResult(requestCode, requestedPermissions, grantResults)
    }

    fun onSearchCalled(view: View) {
        search.onSearchButtonClicked(view)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Constants.AUTOCOMPLETE_REQUEST_CODE) {
            search.onAutocomplete(requestCode, resultCode, data)
        }
    }

    /**
     * Methods for setting button listeners all at once.
     */
    private fun setButtonListeners() {
        setNavButtonListener()
        val switchCampusToggle: ToggleButton = findViewById(R.id.switchCampusButton)
        SwitchCampus(switchCampusToggle, mMap, campus_name)
    }

    fun onOpenMenu(view: View) {
        print("Menu was opened")
    }

    private fun setNavButtonListener() {
        val navigateButton = findViewById<FloatingActionButton>(R.id.navigateButton)
        navigateButton.setOnClickListener {
            val chooseDirectionOptions = ChooseDirectionOptions(route)
            chooseDirectionOptions.show(supportFragmentManager, "directionsOptions")
        }
    }
}