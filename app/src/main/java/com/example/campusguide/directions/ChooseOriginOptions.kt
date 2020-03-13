package com.example.campusguide.directions

import android.app.AlertDialog
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.fragment.app.DialogFragment
import com.example.campusguide.R
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

class ChooseOriginOptions :DialogFragment(){

    private lateinit var mFusedLocationProviderClient: FusedLocationProviderClient

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        mFusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(this.requireActivity())

        val inflater = requireActivity().layoutInflater
        val view = inflater.inflate(R.layout.choose_origin_direction, null)

        val builder: AlertDialog.Builder = AlertDialog.Builder(this.activity)

        builder.setView(view)
        builder.create().window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        return builder.create()
    }
}