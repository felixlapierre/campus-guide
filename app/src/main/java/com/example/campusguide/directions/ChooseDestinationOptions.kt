package com.example.campusguide.directions

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.DialogFragment
import com.example.campusguide.Constants
import com.example.campusguide.MapsActivity
import com.example.campusguide.R
import com.example.campusguide.calendar.Events
import com.example.campusguide.calendar.FindEventLocation
import com.example.campusguide.location.Location
import com.example.campusguide.search.CustomSearch
import com.example.campusguide.search.amenities.AmenitiesLocationProvider
import com.example.campusguide.search.indoor.BuildingIndexSingleton
import com.example.campusguide.search.indoor.IndoorLocationProvider
import com.example.campusguide.search.outdoor.PlacesApiSearchLocationProvider
import database.ObjectBox
import database.entity.Calendar
import io.objectbox.Box
import io.objectbox.kotlin.boxFor
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class ChooseDestinationOptions(private val locationSelectedListener: (location: Location) -> Unit): DialogFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val view = inflater.inflate(R.layout.choose_destination_options, container, false)

        view.findViewById<Button>(R.id.calendar).setOnClickListener {
            useNextEvent()
        }
        view.findViewById<Button>(R.id.fromMap).setOnClickListener {
            chooseFromMap()
        }
        view.findViewById<Button>(R.id.search).setOnClickListener {
            searchForLocation()
        }

        return view
    }

    private fun useNextEvent(){
        dismiss()
        val act = activity as MapsActivity
        val calendarBox: Box<Calendar> = ObjectBox.boxStore.boxFor()
        val calendar = Pair(calendarBox.all[0].id, calendarBox.all[0].name)

        val nextLocation = Events(act, calendar).getNextEventLocation()
        GlobalScope.launch {
            FindEventLocation(act, locationSelectedListener).getLocationOfEvent(nextLocation)
        }
    }

    private fun chooseFromMap(){
        //TODO fill this method once we have the logic to select from the map
        dismiss()
        val location = Location("Montreal", 45.5017, -73.5673)
        locationSelectedListener(location)
    }

    private fun searchForLocation(){
        dismiss()
        // Build the CustomSearch
        val act = activity as MapsActivity
        val provider = IndoorLocationProvider(
            BuildingIndexSingleton.getInstance(act.assets),
            AmenitiesLocationProvider(
                PlacesApiSearchLocationProvider(activity!!)
            )
        )
        val search = CustomSearch(act, provider, Constants.DESTINATION_SEARCH_REQUEST_CODE)

        search.setLocationListener {searchLocation ->
            if(searchLocation != null) {
                locationSelectedListener(searchLocation)
            }
            act.removeActivityResultListener(search)
        }

        act.addActivityResultListener(search)
        search.openCustomSearchActivity()
    }
}