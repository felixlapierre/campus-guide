import android.app.Activity
import android.content.Context
import android.view.View
import com.example.campusguide.map.GoogleMapAdapter
import com.example.campusguide.map.infoWindow.MarkerTag
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Marker

class CustomInfoWindow(private val context: Context, private val  map: GoogleMapAdapter) : GoogleMap.InfoWindowAdapter {

    override fun getInfoWindow(marker: Marker): View? {
        return null
    }

    override fun getInfoContents(marker: Marker): View {
        val markerTag = marker.tag as MarkerTag
        val view = (context as Activity).layoutInflater.inflate(markerTag.layout, null)

        map.setOnInfoWindowClickListener(markerTag.onInfoWindowClick())
        map.setOnInfoWindowCloseListener(markerTag.onInfoWindowClose())
        return markerTag.fillOutInfor(view)
    }
}
