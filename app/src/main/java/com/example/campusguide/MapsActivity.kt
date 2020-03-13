package com.example.campusguide

<<<<<<< Updated upstream
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.CompoundButton
import android.widget.TextView
import android.widget.ToggleButton
import androidx.appcompat.app.AppCompatActivity
import com.example.campusguide.utils.permissions.Permissions
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.activity_maps.*

class MapsActivity : AppCompatActivity() {
    private val permissions = Permissions(this)
    private lateinit var onSearchListener: View.OnClickListener
    private val activityResultListeners: MutableList<ActivityResultListener> = mutableListOf()
=======
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
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.floatingactionbutton.FloatingActionButton
import database.ObjectBox

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var  buildingHighlights: BuildingHighlights
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var route: Route

>>>>>>> Stashed changes

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
<<<<<<< Updated upstream
=======
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        ObjectBox.init(this.applicationContext)
>>>>>>> Stashed changes

        Bootstrapper(this)
    }

    fun getCampusNameTextView(): TextView {
        return campus_name
    }

    fun setSwitchCampusButtonListener(listener: CompoundButton.OnCheckedChangeListener) {
        val toggleButton: ToggleButton = findViewById(R.id.switchCampusButton)
        toggleButton.setOnCheckedChangeListener(listener)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        requestedPermissions: Array<out String>,
        grantResults: IntArray
    ) {
        permissions.onRequestPermissionsResult(requestCode, requestedPermissions, grantResults)
    }

    fun setOnSearchClickedListener(listener: View.OnClickListener) {
        onSearchListener = listener
    }

    fun onSearchCalled(view: View) {
        onSearchListener.onClick(view)
    }

    fun setOnCenterLocationListener(listener: View.OnClickListener) {
        val currentLocationButton: FloatingActionButton = findViewById(R.id.currentLocationButton)
        currentLocationButton.setOnClickListener(listener)
    }

    fun addActivityResultListener(listener: ActivityResultListener) {
        activityResultListeners.add(listener)
    }

<<<<<<< Updated upstream
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        activityResultListeners.forEach { listener ->
            listener.onActivityResult(requestCode, resultCode, data)
        }
=======
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
>>>>>>> Stashed changes
    }

    fun onOpenMenu(view: View) { }

    fun setOnNavigateListener(listener: View.OnClickListener) {
        val navigateButton: FloatingActionButton = findViewById(R.id.navigateButton)
        navigateButton.setOnClickListener(listener)
    }
}