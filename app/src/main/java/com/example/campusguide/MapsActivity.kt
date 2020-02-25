package com.example.campusguide

import android.os.Bundle
import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import android.os.Build
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.maps.model.Marker
import android.location.Location
import android.view.View
import android.widget.ToggleButton
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.location.LocationListener
import com.google.android.gms.location.LocationRequest
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.AutocompleteActivity
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.activity_maps.*

class MapsActivity() : FragmentActivity(), OnMapReadyCallback, LocationListener,
    GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private lateinit var mMap: GoogleMap
    private lateinit var mLastLocation: Location
    private var mCurrLocationMarker: Marker? = null
    private var mGoogleApiClient: GoogleApiClient? = null
    private lateinit var mLocationRequest: LocationRequest
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    companion object {
        private const val LOCATION_PERMISSION_ACCESS_CODE = 1
        private const val AUTOCOMPLETE_REQUEST_CODE = 2
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        val currentLocationButton: FloatingActionButton = findViewById(R.id.currentLocationButton)
        currentLocationButton.setOnClickListener {
            //Check if location permission has been granted
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                goToCurrentLocation()
            } else {
                //Request location permission
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    LOCATION_PERMISSION_ACCESS_CODE
                )
            }
        }

        if(!Places.isInitialized())
            Places.initialize(applicationContext, getString(R.string.google_maps_key))

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
        mMap.uiSettings.isMyLocationButtonEnabled = false

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

        // Update switch campus button listener
        val switchCampusToggle: ToggleButton = findViewById(R.id.switchCampusButton)
        SwitchCampus(switchCampusToggle, mMap, campus_name)

    }

    @Synchronized
    protected fun buildGoogleApiClient() {
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

    }

    override fun onLocationChanged(location: Location) {

        mLastLocation = location
        if (mCurrLocationMarker != null) {
            mCurrLocationMarker!!.remove()
        }
        //Place current location marker
        val latLng = LatLng(location.latitude, location.longitude)
        val markerOptions = MarkerOptions()
        markerOptions.position(latLng)
        markerOptions.title("Current Position")
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
        mCurrLocationMarker = mMap!!.addMarker(markerOptions)

        //move map camera
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng))
        mMap.animateCamera(CameraUpdateFactory.zoomTo(11f))

        //stop location updates
        if (mGoogleApiClient != null) {
            LocationServices.getFusedLocationProviderClient(this)
        }

    }

    override fun onConnectionFailed(connectionResult: ConnectionResult) {

    }

    /**
     * Centers the map on the user's current location and places a marker.
     */
    private fun goToCurrentLocation() {
        fusedLocationClient.lastLocation.addOnSuccessListener(this) { location ->
            if (location != null) {
                val currentLatLng = LatLng(location.latitude, location.longitude)
                mMap.addMarker(
                    MarkerOptions()
                        .position(currentLatLng)
                        .title("You are here.")
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                )
                mMap.animateCamera(
                    CameraUpdateFactory.newLatLngZoom(
                        currentLatLng,
                        Constants.ZOOM_STREET_LVL
                    )
                )
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            LOCATION_PERMISSION_ACCESS_CODE -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
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

    fun onSearchCalled(view :View){
        val fields = arrayListOf(Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS, Place.Field.LAT_LNG)
        val intent : Intent =
                Autocomplete.IntentBuilder(
                AutocompleteActivityMode.FULLSCREEN, fields
                )
                .build(this)
        startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE)
    }

   override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
       super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                val place: Place = Autocomplete.getPlaceFromIntent(data!!)
                mMap!!.addMarker(MarkerOptions()
                    .position(LatLng(place.latLng!!.latitude, place.latLng!!.longitude)))
                mMap!!.animateCamera(CameraUpdateFactory.newLatLng(place.latLng))
                campus_name.text = place.name

            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                print("The Request has run into an error")
                print(data)
            } else if (resultCode == RESULT_CANCELED) {
                print("The Request was cancelled")
                print(data)
            }
        }
    }

    fun onOpenMenu(view : View){
        print("Menu was opened")
    }
}