package com.example.campusguide.utils.permissions

import android.app.Activity
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import com.example.campusguide.Constants

class Permissions constructor(private val activity: Activity): PermissionsSubject {

    private val observers: MutableList<PermissionGrantedObserver> = mutableListOf()

    override fun addObserver(observer: PermissionGrantedObserver) {
        observers.add(observer)
    }

    override fun havePermission(permission: String): Boolean {
        return ActivityCompat.checkSelfPermission(
            activity,
            permission
        ) == PackageManager.PERMISSION_GRANTED
    }

    override fun requestPermission(permission: String) {
        ActivityCompat.requestPermissions(
            activity,
            arrayOf(permission),
            Constants.PERMISSION_REQUEST_CODE
        )
    }

    fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if(requestCode != Constants.PERMISSION_REQUEST_CODE)
            return
        for (i in 0..permissions.size) {
            if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                observers.forEach { observer ->
                    observer.onPermissionGranted(permissions[i])
                }
            }
        }
    }
}