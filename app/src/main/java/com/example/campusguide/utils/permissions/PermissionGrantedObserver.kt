package com.example.campusguide.utils.permissions

interface PermissionGrantedObserver {
    fun onPermissionGranted(permission: String)
}