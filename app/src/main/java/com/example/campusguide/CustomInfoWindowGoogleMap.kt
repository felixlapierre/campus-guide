import android.app.Activity
import android.content.Context
import android.view.View
import android.widget.ImageView
import android.widget.TextView

import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Marker

import com.example.campusguide.R
import com.example.campusguide.utils.InfoWindowData


class CustomInfoWindowGoogleMap(private val context: Context) : GoogleMap.InfoWindowAdapter {

    override fun getInfoWindow(marker: Marker): View? {
        return null
    }

    override fun getInfoContents(marker: Marker): View {
        val view = (context as Activity).layoutInflater
            .inflate(R.layout.custom_info_marker, null)

        val name_tv = view.findViewById<TextView>(R.id.name)
        val details_tv = view.findViewById<TextView>(R.id.details)
        val img = view.findViewById<ImageView>(R.id.pic)

        val hotel_tv = view.findViewById<TextView>(R.id.hotels)
        val food_tv = view.findViewById<TextView>(R.id.food)
        val transport_tv = view.findViewById<TextView>(R.id.transport)

        name_tv.setText(marker.title)
        details_tv.setText(marker.snippet)

        val infoWindowData = marker.tag as InfoWindowData?

        hotel_tv.setText(infoWindowData?.hotel)
        food_tv.setText(infoWindowData?.food)
        transport_tv.setText(infoWindowData?.transport)

        return view
    }
}