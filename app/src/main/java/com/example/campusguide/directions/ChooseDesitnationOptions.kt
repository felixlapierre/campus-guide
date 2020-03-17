package com.example.campusguide.directions

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.DialogFragment
import com.example.campusguide.R

class ChooseDestinationOptions(private val locationSelectedListener: (location: Location) -> Unit): DialogFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val view = inflater.inflate(R.layout.choose_destination_options, container, false)

        view.findViewById<Button>(R.id.calendar).setOnClickListener {
            useLastEvent()
        }
        view.findViewById<Button>(R.id.fromMap).setOnClickListener {
            chooseFromMap()
        }
        view.findViewById<Button>(R.id.search).setOnClickListener {
            searchForLocation()
        }

        return view
    }

    private fun useLastEvent(){
        //TODO fill this method once we have the logic to use a calendar Event
        dismiss()
        locationSelectedListener(Location("Montreal"))
    }

    private fun chooseFromMap(){
        //TODO fill this method once we have the logic to select from the map
        dismiss()
        locationSelectedListener(Location("Montreal"))
    }

    private fun searchForLocation(){
        //TODO fill this method once we have the logic to search for a location
        dismiss()
        locationSelectedListener(Location("Montreal"))
    }

}