package com.example.campusguide.permissions

interface PermissionGrantedObserver {
    fun onPermissionGranted(permission: String)
}