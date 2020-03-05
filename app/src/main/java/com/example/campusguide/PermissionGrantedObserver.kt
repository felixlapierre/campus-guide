package com.example.campusguide

interface PermissionGrantedObserver {
    fun onPermissionGranted(permission: String)
}