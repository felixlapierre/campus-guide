package com.example.campusguide

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.CompoundButton
import android.widget.TextView
import android.widget.ToggleButton
import androidx.appcompat.app.AppCompatActivity
import com.example.campusguide.map.displ.ChangeFloor
import com.example.campusguide.map.displayIndoor.FloorPlans
import com.example.campusguide.utils.permissions.Permissions
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.activity_maps.*


class MapsActivity : AppCompatActivity() {
    var permissions: Permissions? = null
    private lateinit var onSearchListener: View.OnClickListener
    private val activityResultListeners: MutableList<ActivityResultListener> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)

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
        permissions?.onRequestPermissionsResult(requestCode, requestedPermissions, grantResults)
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

    fun setFloorPlanButtons(){
        ChangeFloor.upButtonId = R.id.upOneFloor
        ChangeFloor.downButtonId = R.id.downOneFloor
        FloorPlans.floorUpButton = findViewById(R.id.upOneFloor)
        FloorPlans.floorDownButton = findViewById(R.id.downOneFloor)
    }

    fun addActivityResultListener(listener: ActivityResultListener) {
        activityResultListeners.add(listener)
    }

    fun removeActivityResultListener(listener: ActivityResultListener) {
        activityResultListeners.remove(listener)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        activityResultListeners.forEach { listener ->
            listener.onActivityResult(requestCode, resultCode, data)
        }
    }
}
