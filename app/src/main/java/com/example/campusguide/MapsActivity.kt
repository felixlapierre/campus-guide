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
    private lateinit var route: Route

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
        addBuildingHighlights(googleMap)
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
                LatLng(45.495166, -73.579171),
                LatLng(45.495222, -73.579115),
                LatLng(45.495358, -73.579367),
                LatLng(45.495519, -73.579200),
                LatLng(45.495440, -73.578962)
            )

        val muAnnex = PolygonOptions()
            .add(
                LatLng(45.497963, -73.579537),
                LatLng(45.497785, -73.579704),
                LatLng(45.497748, -73.579629),
                LatLng(45.497920, -73.579460)
            )

        val dAnnex = PolygonOptions()
            .add(
                LatLng(45.497849, -73.579311),
                LatLng(45.497811, -73.579232),
                LatLng(45.497707, -73.579342),
                LatLng(45.497742, -73.579412)
            )

        val miAnnex = PolygonOptions()
            .add(
                LatLng(45.497811, -73.579233),
                LatLng(45.497779, -73.579167),
                LatLng(45.497682, -73.579260),
                LatLng(45.497695, -73.579285),
                LatLng(45.497623, -73.579354),
                LatLng(45.497648, -73.579403)
            )

        val ciAnnex = PolygonOptions()
            .add(
                LatLng(45.497407, -73.580016),
                LatLng(45.497585, -73.579839),
                LatLng(45.497546, -73.579760),
                LatLng(45.497366, -73.579933)
            )

        val sAnnex = PolygonOptions()
            .add(
                LatLng(45.497366, -73.579933),
                LatLng(45.497325, -73.579851),
                LatLng(45.497426, -73.579752),
                LatLng(45.497437, -73.579773),
                LatLng(45.497460, -73.579752),
                LatLng(45.497491, -73.579815)
            )

        val mAnnex = PolygonOptions()
            .add(
                LatLng(45.497325, -73.579851),
                LatLng(45.497290, -73.579781),
                LatLng(45.497390, -73.579681),
                LatLng(45.497426, -73.579752)
            )

        val cbBuilding = PolygonOptions()
            .add(
                LatLng(45.495030, -73.574065),
                LatLng(45.495107, -73.573991),
                LatLng(45.495137, -73.574054),
                LatLng(45.495195, -73.573997),
                LatLng(45.495165, -73.573934),
                LatLng(45.495246, -73.573856),
                LatLng(45.495403, -73.574190),
                LatLng(45.495061, -73.574520),
                LatLng(45.494942, -73.574270),
                LatLng(45.495009, -73.574204),
                LatLng(45.495002, -73.574189),
                LatLng(45.495062, -73.574131)
            )

        val enAnnex = PolygonOptions()
            .add(
                LatLng(45.496932, -73.579553),
                LatLng(45.496892, -73.579470),
                LatLng(45.496788, -73.579574),
                LatLng(45.496803, -73.579607),
                LatLng(45.496676, -73.579734),
                LatLng(45.496700, -73.579784)
            )

        val erBuilding = PolygonOptions()
            .add(
                LatLng(45.496262, -73.580352),
                LatLng(45.496162, -73.580070),
                LatLng(45.496249, -73.579973),
                LatLng(45.496217, -73.579906),
                LatLng(45.496523, -73.579633),
                LatLng(45.496679, -73.579971)
            )

        val fbBuilding = PolygonOptions()
            .add(
                LatLng(45.494696, -73.578039),
                LatLng(45.494913, -73.577786),
                LatLng(45.494870, -73.577713),
                LatLng(45.494877, -73.577705),
                LatLng(45.494836, -73.577633),
                LatLng(45.494843, -73.577626),
                LatLng(45.494799, -73.577550),
                LatLng(45.494807, -73.577541),
                LatLng(45.494764, -73.577465),
                LatLng(45.494775, -73.577453),
                LatLng(45.494692, -73.577309),
                LatLng(45.494701, -73.577299),
                LatLng(45.494655, -73.577219),
                LatLng(45.494397, -73.577521)
            )

        val gsBuilding = PolygonOptions()
            .add(
                LatLng(45.496578, -73.581437),
                LatLng(45.496415, -73.580943),
                LatLng(45.496487, -73.580874),
                LatLng(45.496477, -73.580845),
                LatLng(45.496518, -73.580804),
                LatLng(45.496653, -73.581172),
                LatLng(45.496711, -73.581128),
                LatLng(45.496785, -73.581302),
                LatLng(45.4966265, -73.5814486)
            )

        val kAnnex = PolygonOptions()
            .add(
                LatLng(45.497883, -73.579384),
                LatLng(45.497849, -73.579311),
                LatLng(45.497754, -73.579401),
                LatLng(45.497772, -73.579439),
                LatLng(45.497697, -73.579510),
                LatLng(45.497686, -73.579487),
                LatLng(45.497597, -73.579572),
                LatLng(45.497627, -73.579635)
            )

        val ldBuilding = PolygonOptions()
            .add(
                LatLng(45.496859, -73.577171),
                LatLng(45.496564, -73.577471),
                LatLng(45.496522, -73.577387),
                LatLng(45.496818, -73.577087)
            )

        googleMap.addPolygon(hall)
        googleMap.addPolygon(websterLibrary)
        googleMap.addPolygon(bAnnex)
        googleMap.addPolygon(ev)
        googleMap.addPolygon(gm)
        googleMap.addPolygon(jmsb)
        googleMap.addPolygon(muAnnex)
        googleMap.addPolygon(dAnnex)
        googleMap.addPolygon(miAnnex)
        googleMap.addPolygon(ciAnnex)
        googleMap.addPolygon(sAnnex)
        googleMap.addPolygon(mAnnex)
        googleMap.addPolygon(cbBuilding)
        googleMap.addPolygon(enAnnex)
        googleMap.addPolygon(erBuilding)
        googleMap.addPolygon(fbBuilding)
        googleMap.addPolygon(gsBuilding)
        googleMap.addPolygon(kAnnex)
    }
}
