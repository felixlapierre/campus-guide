package com.example.campusguide

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
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView



class MapsActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private val permissions = Permissions(this)
    private lateinit var onSearchListener: View.OnClickListener
    private val activityResultListeners: MutableList<ActivityResultListener> = mutableListOf()

    private lateinit var toolbar: Toolbar
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navView: NavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)

        Bootstrapper(this)

        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        drawerLayout = findViewById(R.id.drawer_layout)
        navView = findViewById(R.id.nav_view)

        val toggle = ActionBarDrawerToggle(
            this, drawerLayout, toolbar, 0, 0
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        navView.setNavigationItemSelectedListener(this)
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        activityResultListeners.forEach { listener ->
            listener.onActivityResult(requestCode, resultCode, data)
        }
    }

    fun onOpenMenu(view: View) { }

    fun setOnNavigateListener(listener: View.OnClickListener) {
        val navigateButton: FloatingActionButton = findViewById(R.id.navigateButton)
        navigateButton.setOnClickListener(listener)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
//            R.id.nav_profile -> {
//                Toast.makeText(this, "Profile clicked", Toast.LENGTH_SHORT).show()
//            }
//            R.id.nav_messages -> {
//                Toast.makeText(this, "Messages clicked", Toast.LENGTH_SHORT).show()
//            }
//            R.id.nav_friends -> {
//                Toast.makeText(this, "Friends clicked", Toast.LENGTH_SHORT).show()
//            }
//            R.id.nav_update -> {
//                Toast.makeText(this, "Update clicked", Toast.LENGTH_SHORT).show()
//            }
//            R.id.nav_logout -> {
//                Toast.makeText(this, "Sign out clicked", Toast.LENGTH_SHORT).show()
//            }
        }
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }
}