package com.example.campusguide

import android.content.Intent
import android.os.Bundle
import android.util.Log
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
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.android.material.navigation.NavigationView



class MapsActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private val permissions = Permissions(this)
    private lateinit var onSearchListener: View.OnClickListener
    private val activityResultListeners: MutableList<ActivityResultListener> = mutableListOf()

    private val RC_SIGN_IN = 1
    private val TAG = "MapsActivity"

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

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()
        val mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        val signInButton = findViewById<SignInButton>(R.id.sign_in_button)
        signInButton.setSize(SignInButton.SIZE_STANDARD)
        fun signIn() {
            val signInIntent: Intent = mGoogleSignInClient.signInIntent
            startActivityForResult(signInIntent, RC_SIGN_IN)
        }
        signInButton.setOnClickListener { view ->
            when (view.id) {
                R.id.sign_in_button -> signIn()
            }
        }
    }

    override fun onStart(){
        super.onStart()
        val account = GoogleSignIn.getLastSignedInAccount(this)
        //updateUI(account)
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

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.getResult(ApiException::class.java)
            // Signed in successfully, show authenticated UI.
            //updateUI(account)
        } catch (e: ApiException) {
            Log.w(TAG, "signInResult:failed code=" + e.statusCode)
            //updateUI(null)
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