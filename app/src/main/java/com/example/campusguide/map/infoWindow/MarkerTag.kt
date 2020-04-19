package com.example.campusguide.map.infoWindow

import android.text.Layout
import android.view.View
import com.google.android.gms.maps.GoogleMap

abstract class MarkerTag(val layout: Int){
    abstract fun onInfoWindowClick(): GoogleMap.OnInfoWindowClickListener
    abstract fun onInfoWindowClose(): GoogleMap.OnInfoWindowCloseListener
    abstract fun fillOutInfor(view: View): View
}