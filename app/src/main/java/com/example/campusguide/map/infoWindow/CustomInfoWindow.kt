import android.app.Activity
import android.content.Context
import android.view.View
import android.widget.TextView

import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Marker

import com.example.campusguide.R
import com.example.campusguide.map.infoWindow.InfoWindowData


class CustomInfoWindow(private val context: Context) : GoogleMap.InfoWindowAdapter{

    override fun getInfoWindow(marker: Marker): View? {
        return null
    }

    override fun getInfoContents(marker: Marker): View {
        val view = (context as Activity).layoutInflater
            .inflate(R.layout.custom_info_marker, null)

        val symbol = view.findViewById<TextView>(R.id.symbol)
        val fullName = view.findViewById<TextView>(R.id.fullName)
        val address = view.findViewById<TextView>(R.id.address)
        val services = view.findViewById<TextView>(R.id.services)
        val events = view.findViewById<TextView>(R.id.events)

        val infoWindowData = marker.tag as InfoWindowData?

        symbol.text = infoWindowData?.symbol
        fullName.text = infoWindowData?.fullName
        address.text = infoWindowData?.address
        services.text = infoWindowData?.services
        events.text = infoWindowData?.events

        return view
    }
}