package com.example.campusguide

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.ToggleButton
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.campusguide.directions.ChooseDirectionOptions
import com.example.campusguide.directions.Route
import com.example.campusguide.utils.BuildingHighlights
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.AutocompleteActivity
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import com.google.android.material.floatingactionbutton.FloatingActionButton
import database.ObjectBox
import kotlinx.android.synthetic.main.activity_maps.*

class MapsActivity : AppCompatActivity(), OnMapReadyCallback,
    GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private val map = GoogleMapAdapter()
    private lateinit var mMap: GoogleMap
    private var mGoogleApiClient: GoogleApiClient? = null
    private lateinit var mLocationRequest: LocationRequest
    private lateinit var route: Route
    private lateinit var buildingHighlights: BuildingHighlights
    private val permissions = Permissions(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        val currentLocationButton: FloatingActionButton = findViewById(R.id.currentLocationButton)
        currentLocationButton.setOnClickListener(CenterLocationListener(this, map, permissions))

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

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                buildGoogleApiClient()
            }
        } else {
            buildGoogleApiClient()
        }

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

    @Synchronized
    private fun buildGoogleApiClient() {
        mGoogleApiClient = GoogleApiClient.Builder(this)
            .addConnectionCallbacks(this)
            .addOnConnectionFailedListener(this)
            .addApi(LocationServices.API).build()
        mGoogleApiClient!!.connect()
    }

    override fun onConnected(bundle: Bundle?) {
        mLocationRequest = LocationRequest()
        mLocationRequest.interval = 1000
        mLocationRequest.fastestInterval = 1000
        mLocationRequest.priority = LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            LocationServices.getFusedLocationProviderClient(this)
        }
    }

    override fun onConnectionSuspended(i: Int) {
        print("Connection is SUSPENDED")
    }

    override fun onConnectionFailed(connectionResult: ConnectionResult) {
        print("Connection has FAILED")
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        requestedPermissions: Array<out String>,
        grantResults: IntArray
    ) {
        permissions.onRequestPermissionsResult(requestCode, requestedPermissions, grantResults)
    }

    fun onSearchCalled(view: View) {
        val fields =
            arrayListOf(
                Place.Field.ID,
                Place.Field.NAME,
                Place.Field.ADDRESS,
                Place.Field.LAT_LNG
            )
        val intent: Intent =
            Autocomplete.IntentBuilder(
                AutocompleteActivityMode.FULLSCREEN, fields
            )
                .build(this)
        startActivityForResult(intent, Constants.AUTOCOMPLETE_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Constants.AUTOCOMPLETE_REQUEST_CODE) {
            when (resultCode) {
                RESULT_OK -> {
                    val place: Place = Autocomplete.getPlaceFromIntent(data!!)
                    mMap.addMarker(
                        MarkerOptions()
                            .position(LatLng(place.latLng!!.latitude, place.latLng!!.longitude))
                    )
                    mMap.animateCamera(CameraUpdateFactory.newLatLng(place.latLng))
                    campus_name.text = place.name
                }
                AutocompleteActivity.RESULT_ERROR -> {
                    print("The Request has run into an error")
                    print(data)
                }
                RESULT_CANCELED -> {
                    print("The Request was cancelled")
                    print(data)
                }
            }
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