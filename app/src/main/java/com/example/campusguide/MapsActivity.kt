package com.example.campusguide

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ToggleButton
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
import com.google.android.gms.maps.model.Polygon
import com.google.android.gms.maps.model.PolygonOptions
import com.google.android.material.floatingactionbutton.FloatingActionButton

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
        addBuildingHighlights(googleMap)
    }

    fun addBuildingHighlights(googleMap: GoogleMap){
        val hall = PolygonOptions()
            .add(LatLng(45.497165, -73.579545),
            LatLng(45.497710, -73.579034),
            LatLng(45.497373, -73.578338),
            LatLng(45.496830, -73.578850)
            )

        val websterLibrary = PolygonOptions()
            .add(LatLng(45.496913, -73.578330),
                LatLng(45.496897, -73.578290),
                LatLng(45.496941, -73.578247),
                LatLng(45.496965, -73.578295),
                LatLng(45.497002, -73.578257),
                LatLng(45.497018, -73.578295),
                LatLng(45.497260, -73.578058),
                LatLng(45.497142, -73.577818),
                LatLng(45.497108, -73.577849),
                LatLng(45.497086, -73.577800),
                LatLng(45.497078, -73.577806),
                LatLng(45.497071, -73.577792),
                LatLng(45.497116, -73.577748),
                LatLng(45.497041, -73.577589),
                LatLng(45.496994, -73.577631),
                LatLng(45.496989, -73.577620),
                LatLng(45.496999, -73.577612),
                LatLng(45.496978, -73.577568),
                LatLng(45.497008, -73.577538),
                LatLng(45.496892, -73.577294),
                LatLng(45.496616, -73.577557),
                LatLng(45.496637, -73.577601),
                LatLng(45.496583, -73.577651),
                LatLng(45.496496, -73.577466),
                LatLng(45.496256, -73.577698),
                LatLng(45.496669, -73.578567),
                LatLng(45.496706, -73.578531),
                LatLng(45.496729, -73.578579),
                LatLng(45.496889, -73.578415),
                LatLng(45.496869, -73.578372)
            )

        val bAnnex = PolygonOptions()
            .add(
                LatLng(45.497883, -73.579384),
                LatLng(45.497917, -73.579453),
                LatLng(45.497741, -73.579636),
                LatLng(45.497705, -73.579560)
            )

        val ev = PolygonOptions()
            .add(
                LatLng(45.4952462, -73.5780194),
                LatLng(45.4952631, -73.5779996),
                LatLng(45.4952465, -73.5779128),
                LatLng(45.4952328, -73.5779141),
                LatLng(45.4952375, -73.5778752),
                LatLng(45.4952965, -73.5777948),
                LatLng(45.4953531, -73.5777415),
                LatLng(45.4953541, -73.5776955),
                LatLng(45.4954455, -73.5776097),
                LatLng(45.4958638, -73.5784952),
                LatLng(45.4955938, -73.5787597)
                //LatLng(45.4952453, -73.5780184)
        )

        val gm = PolygonOptions()
            .add(
                LatLng(45.4957625, -73.5791047),
                LatLng(45.4957797, -73.5791463),
                LatLng(45.4961278, -73.5788070),
                LatLng(45.4959454, -73.5784318),
                LatLng(45.4958591, -73.5785072),
                LatLng(45.4956175, -73.5787449),
                LatLng(45.4957781, -73.5790909)
            )

        val jmsb = PolygonOptions()
            .add(
                LatLng(45.495187, -73.578526),
                LatLng(45.495006, -73.578737),
                LatLng(45.495038, -73.578791),
                LatLng(45.495007, -73.578824),
                LatLng(45.495166, -73.579171)
            )

        googleMap.addPolygon(hall)
        googleMap.addPolygon(websterLibrary)
        googleMap.addPolygon(bAnnex)
        googleMap.addPolygon(ev)
        googleMap.addPolygon(gm)
        googleMap.addPolygon(jmsb)
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(hall, Constants.ZOOM_STREET_LVL))

        // Update switch campus button listener
        val switchCampusToggle: ToggleButton = findViewById(R.id.switchCampusButton)
        SwitchCampus(switchCampusToggle, mMap)
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
