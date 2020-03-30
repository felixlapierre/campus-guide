package com.example.campusguide.utils.permissions

interface PermissionGrantedObserver {
    fun onPermissionGranted(permissions: Array<out String>)
}