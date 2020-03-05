package com.example.campusguide

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat

class Permissions constructor(private val activity: Activity) {
    fun havePermission(permission: String): Boolean {
        return ActivityCompat.checkSelfPermission(
            activity,
            permission
        ) == PackageManager.PERMISSION_GRANTED
    }

    fun requestPermission(permission: String) {
        ActivityCompat.requestPermissions(
            activity,
            arrayOf(permission),
            Constants.LOCATION_PERMISSION_ACCESS_CODE
        )
    }

    fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {

        when (requestCode) {
            Constants.LOCATION_PERMISSION_ACCESS_CODE -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    //goToCurrentLocation()
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