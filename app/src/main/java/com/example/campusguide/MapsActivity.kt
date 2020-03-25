package com.example.campusguide

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.CompoundButton
import android.widget.TextView
import android.widget.Toast
import android.widget.ToggleButton
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.example.campusguide.utils.permissions.Permissions
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_maps.*


class MapsActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private val permissions = Permissions(this)
    private lateinit var onSearchListener: View.OnClickListener
    private val activityResultListeners: MutableList<ActivityResultListener> = mutableListOf()

    private val RC_SIGN_IN = 1
    private val TAG = "MapsActivity"

    private lateinit var mGoogleSignInClient: GoogleSignInClient
    private var userEmail: String? = null

    private lateinit var toolbar: Toolbar
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navView: NavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)

        Bootstrapper(this)

        setupDrawer()
        setupLogin()
    }

    // TODO: Put into Login class?
    override fun onStart() {
        super.onStart()
        val account = GoogleSignIn.getLastSignedInAccount(this)
        if (account != null) {
            updateUI(account)
        }
    }

    // TODO: Put into Login class?
    fun getUserEmail(): String {
        return userEmail.toString()
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
        // TODO: Connect with future Login class
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }
    }

    fun setOnNavigateListener(listener: View.OnClickListener) {
        val navigateButton: FloatingActionButton = findViewById(R.id.navigateButton)
        navigateButton.setOnClickListener(listener)
    }

    // TODO: Setup Drawer class?
    // drawer menu
    private fun setupDrawer() {
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

    // TODO: Put into Login class
    private fun setupLogin() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)
    }

    // TODO: Put into Login class
    private fun signIn(mGoogleSignInClient: GoogleSignInClient) {
        val signInIntent: Intent = mGoogleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    // TODO: Put into Login class
    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.getResult(ApiException::class.java)
            // Signed in successfully, show authenticated UI.
            if (account != null) {
                updateUI(account)
            }
        } catch (e: ApiException) {
            Log.w(TAG, "signInResult:failed code=" + e.statusCode)
        }
    }

    // TODO: Put into Login class
    private fun signOut() {
        mGoogleSignInClient.signOut()
        userEmail = null
        val loginMenuItem = navView.menu.findItem(R.id.login_button)
        loginMenuItem.title = "Choose Account"
    }

    // TODO: Put into Login class
    private fun updateUI(account: GoogleSignInAccount) {
        userEmail = account.email
        Toast.makeText(this, "Logged into $userEmail", Toast.LENGTH_LONG).show()
        val loginMenuItem = navView.menu.findItem(R.id.login_button)
        loginMenuItem.title = "Log Out of $userEmail"
    }

    // TODO: Put into Drawer class
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.login_button -> {
                if (item.title == "Choose Account"){
                    signIn(mGoogleSignInClient)
                }
                else if (item.title == "Log Out of $userEmail"){
                    Toast.makeText(this, "Logged Out", Toast.LENGTH_LONG).show()
                    signOut()
                }
            }
        }
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }
}