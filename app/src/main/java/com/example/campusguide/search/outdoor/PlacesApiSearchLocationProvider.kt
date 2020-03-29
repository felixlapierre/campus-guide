package com.example.campusguide.search.outdoor

import androidx.fragment.app.FragmentActivity
import com.example.campusguide.search.SearchLocation
import com.example.campusguide.search.SearchLocationProvider
import com.example.campusguide.utils.DisplayMessageErrorListener
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.FetchPlaceRequest
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class PlacesApiSearchLocationProvider constructor(private val activity: FragmentActivity):
    SearchLocationProvider {
    override suspend fun getLocation(id: String): SearchLocation = suspendCoroutine { cont ->
        val fields: List<Place.Field> =
            listOf(Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS, Place.Field.LAT_LNG)

        val request = FetchPlaceRequest.newInstance(id, fields)

        Places.createClient(activity).fetchPlace(request).addOnSuccessListener { response ->
            val place = response.place
            val name = place.name
            val latlng = place.latLng
            if(latlng != null && name != null) {
                val location = SearchLocation(
                    name,
                    place.address!!,
                    id,
                    latlng.latitude,
                    latlng.longitude
                )
                cont.resume(location)
            }
        }.addOnFailureListener { exception ->
            val message = exception.message
            if(message != null) {
                DisplayMessageErrorListener(activity).onError(message)
            }
        }
    }

}