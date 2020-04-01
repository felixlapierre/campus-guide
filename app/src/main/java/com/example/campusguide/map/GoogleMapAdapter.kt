package com.example.campusguide.map

import com.example.campusguide.directions.PathPolyline
import com.google.android.gms.maps.CameraUpdate
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Polyline
import com.google.android.gms.maps.model.PolylineOptions
import com.google.android.gms.maps.model.MarkerOptions

class GoogleMapAdapter : Map {
    lateinit var adapted: GoogleMap

    override fun addMarker(position: LatLng, title: String): Marker? {
        val opts = MarkerOptions()
            .position(position)
            .title(title)
            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))

        return GoogleMapMarker(adapted.addMarker(opts))
    }

    override fun addMarker(opts: MarkerOptions): Marker? {
        return GoogleMapMarker(adapted.addMarker(opts))
    }

    override fun animateCamera(position: LatLng, zoom: Float) {
        adapted.animateCamera(
            CameraUpdateFactory.newLatLngZoom(
                position,
                zoom
            )
        )
    }

    override fun moveCamera(update: CameraUpdate?) {
        return adapted.moveCamera(update)
    }

    override fun addPolyline(polyOptions: PolylineOptions?): Polyline? {
        return adapted.addPolyline(polyOptions)
    }

    override fun setInfoWindowAdapter(infoWindowAdapter: GoogleMap.InfoWindowAdapter) {
        adapted.setInfoWindowAdapter(infoWindowAdapter)
    }

    override fun setOnInfoWindowClickListener(infoWindowClickListener: GoogleMap.OnInfoWindowClickListener) {
        adapted.setOnInfoWindowClickListener(infoWindowClickListener)
    }

    override fun addPath(path: PathPolyline) {
        addPolyline(path.polyline)
        addMarker(path.startMarker)
        addMarker(path.endMarker)
    }
}