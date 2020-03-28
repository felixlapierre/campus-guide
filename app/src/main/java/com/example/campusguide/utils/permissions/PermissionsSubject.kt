package com.example.campusguide.utils.permissions

interface PermissionsSubject {
    fun addObserver(observer: PermissionGrantedObserver)
    fun havePermission(permission: String): Boolean
    fun requestPermission(permission: String)
}