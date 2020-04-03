package com.example.campusguide.calendar

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.example.campusguide.ActivityResultListener
import com.example.campusguide.MapsActivity
import com.example.campusguide.R
import com.example.campusguide.utils.CalendarUtils
import com.example.campusguide.utils.permissions.PermissionGrantedObserver
import com.example.campusguide.utils.permissions.PermissionsSubject
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.android.material.navigation.NavigationView

/**
 * Class for handling login.
 * Asks for calendar permissions when first logging in.
 */

class Login constructor(
    private val activity: MapsActivity,
    private val permissions: PermissionsSubject
) : ActivityResultListener, PermissionGrantedObserver {
    init {
        permissions.addObserver(this)
    }

    private val calendarPermission = Manifest.permission.READ_CALENDAR

    private val TAG = "MapsActivity"
    private val RC_SIGN_IN = 1

    private lateinit var mGoogleSignInClient: GoogleSignInClient
    private var userEmail: String? = null

    private fun getCalendarPermissions() {
        if (ActivityCompat.checkSelfPermission(
                activity,
                Manifest.permission.READ_CALENDAR
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            permissions.requestPermission(calendarPermission)
        }
    }

    override fun onPermissionGranted(permissions: Array<out String>) {
        if(permissions.contains(calendarPermission)) {
            updateUI()
        }
    }

    fun onCreate() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()
        mGoogleSignInClient = GoogleSignIn.getClient(activity, gso)
    }

    fun onStart() {
        val account = GoogleSignIn.getLastSignedInAccount(activity)
        // Signed in already, show authenticated UI.
        if (account != null) {
            userEmail = account.email
            updateUI()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }
    }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.getResult(ApiException::class.java)
            // Signed in successfully, show authenticated UI.
            if (account != null) {
                userEmail = account.email
                updateUI()
            }
        } catch (e: ApiException) {
            Log.w(TAG, "signInResult:failed code=" + e.statusCode)
        }
    }

    fun signIn() {
        val signInIntent: Intent = mGoogleSignInClient.signInIntent
        activity.startActivityForResult(signInIntent, RC_SIGN_IN)
        // Ask for calendar permissions
        getCalendarPermissions()
    }

    fun signOut() {
        mGoogleSignInClient.signOut()
        userEmail = null
        Toast.makeText(activity, "Logged Out", Toast.LENGTH_LONG).show()
        // Change menu item title for Login asking to login to an account
        getNavView().menu.findItem(R.id.login_button).title = "Login to an Account"
        // Disable view for Calendar menu item
        getNavView().menu.findItem(R.id.calendar).isVisible = false
    }

    private fun updateUI() {
        Toast.makeText(activity, "Logged into $userEmail", Toast.LENGTH_LONG).show()
        // Change menu item title for Login to include logged in email
        getNavView().menu.findItem(R.id.login_button).title = "Log Out of $userEmail"
        // Enable view for Calendar menu item
        getNavView().menu.findItem(R.id.calendar).isVisible = true
        // Set Calendar menu item title to pre-selected Calendar if found in DB
        val calendarUtils = CalendarUtils(activity)
        val calName = calendarUtils.getCalendarNameFromDB()
        if (calName != ""){
            calendarUtils.setCalendarMenuItemName(calName)
        }
    }

    fun getUserEmail(): String {
        return userEmail.toString()
    }

    private fun getNavView(): NavigationView {
        return activity.findViewById(R.id.nav_view)
    }
}
