import android.app.Activity
import android.content.Context
import android.view.View
import android.widget.TextView
import com.example.campusguide.R
import com.example.campusguide.map.infoWindow.InfoWindowData
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Marker

class CustomInfoWindow(private val context: Context) : GoogleMap.InfoWindowAdapter {

    override fun getInfoWindow(marker: Marker): View? {
        return null
    }

    override fun getInfoContents(marker: Marker): View {
        val view = (context as Activity).layoutInflater
            .inflate(R.layout.custom_info_marker, null)

        val infoWindowData = marker.tag as InfoWindowData?

        fillOutInfo(view, infoWindowData)

        return view
    }

    private fun fillOutInfo(view: View, infoWindowData: InfoWindowData?) {
        view.findViewById<TextView>(R.id.symbol).text = infoWindowData?.symbol
        view.findViewById<TextView>(R.id.fullName).text = infoWindowData?.fullName
        view.findViewById<TextView>(R.id.address).text = infoWindowData?.address
        view.findViewById<TextView>(R.id.departments).text = infoWindowData?.departments
        view.findViewById<TextView>(R.id.departmentsList).text = infoWindowData?.departmentsList
        view.findViewById<TextView>(R.id.services).text = infoWindowData?.services
        view.findViewById<TextView>(R.id.servicesList).text = infoWindowData?.servicesList
    }
}
