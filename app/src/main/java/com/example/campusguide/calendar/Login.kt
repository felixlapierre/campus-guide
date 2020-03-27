package com.example.campusguide.calendar

import android.content.Intent
import android.util.Log
import android.widget.Toast
import com.example.campusguide.ActivityResultListener
import com.example.campusguide.MapsActivity
import com.example.campusguide.R
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.android.material.navigation.NavigationView

class Login constructor(private val activity: MapsActivity) : ActivityResultListener {

    private val TAG = "MapsActivity"
    private val RC_SIGN_IN = 1

    private lateinit var mGoogleSignInClient: GoogleSignInClient
    private var userEmail: String? = null

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
            updateUI(account)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }
    }

    fun signIn() {
        val signInIntent: Intent = mGoogleSignInClient.signInIntent
        activity.startActivityForResult(signInIntent, RC_SIGN_IN)
    }

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

    fun signOut() {
        mGoogleSignInClient.signOut()
        userEmail = null
        val navView = activity.findViewById<NavigationView>(R.id.nav_view)
        val loginMenuItem = navView.menu.findItem(R.id.login_button)
        loginMenuItem.title = "Login to an Account"
    }

    private fun updateUI(account: GoogleSignInAccount) {
        userEmail = account.email
        Toast.makeText(activity, "Logged into $userEmail", Toast.LENGTH_LONG).show()
        val navView = activity.findViewById<NavigationView>(R.id.nav_view)
        val loginMenuItem = navView.menu.findItem(R.id.login_button)
        loginMenuItem.title = "Log Out of $userEmail"
    }

    fun getUserEmail(): String {
        return userEmail.toString()
    }
}