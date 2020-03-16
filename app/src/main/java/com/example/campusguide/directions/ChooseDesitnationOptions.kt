package com.example.campusguide.directions

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.DialogFragment
import com.example.campusguide.R
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.maps.model.LatLng

class ChooseDestinationOptions(private val locationSelectedListener: (location: LatLng) -> Unit): DialogFragment() {

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
        dismiss()
        locationSelectedListener(LatLng(0.0, 0.0))
    }

    private fun chooseFromMap(){
        dismiss()
        locationSelectedListener(LatLng(0.0, 0.0))
    }

    private fun searchForLocation(){
        dismiss()
        locationSelectedListener(LatLng(0.0, 0.0))
    }

}