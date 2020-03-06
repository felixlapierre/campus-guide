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
import com.google.android.gms.maps.GoogleMap
import com.google.android.libraries.places.api.Places
import com.google.android.material.floatingactionbutton.FloatingActionButton
import database.ObjectBox
import kotlinx.android.synthetic.main.activity_maps.*

class MapsActivity : AppCompatActivity() {

    private val map = GoogleMapAdapter()
    private lateinit var mMap: GoogleMap
    private lateinit var route: Route
    private lateinit var buildingHighlights: BuildingHighlights
    private val permissions = Permissions(this)
    private lateinit var search: Search

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)

        GoogleMapInitializer(this, map)
        search = Search(this, map)

        val switchCampusToggle: ToggleButton = findViewById(R.id.switchCampusButton)
        SwitchCampus(switchCampusToggle, map, campus_name)

        val currentLocationButton: FloatingActionButton = findViewById(R.id.currentLocationButton)
        currentLocationButton.setOnClickListener(
            CenterLocationListener(
                map,
                permissions,
                FusedLocationProvider(this)
            )
        )

        setButtonListeners()

        route = Route(map, this)

        ObjectBox.init(this.applicationContext)
      
        if (!Places.isInitialized())
            Places.initialize(applicationContext, getString(R.string.google_maps_key))
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
        SwitchCampus(switchCampusToggle, map, campus_name)
    }

    fun onOpenMenu(view: View) { }

    private fun setNavButtonListener() {
        val navigateButton = findViewById<FloatingActionButton>(R.id.navigateButton)
        navigateButton.setOnClickListener {
            val chooseDirectionOptions = ChooseDirectionOptions(route)
            chooseDirectionOptions.show(supportFragmentManager, "directionsOptions")
        }
    }
}