package com.example.campusguide.search

import android.content.Intent
import android.view.Gravity
import android.view.View
import android.widget.PopupWindow
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.campusguide.R
import com.google.android.libraries.places.api.model.Place
import kotlinx.android.synthetic.main.confirm_window.view.*

class CurrentTravelPopup {
    private var view : View
    private var activity: AppCompatActivity
    private var window: PopupWindow

    constructor(activity: AppCompatActivity, place: Place){
        this.activity = activity
        window = PopupWindow(activity)
        view = activity.layoutInflater.inflate(R.layout.confirm_window, null)

        window.contentView = view
        window.showAtLocation(
            activity.findViewById(android.R.id.content),
            Gravity.CENTER_VERTICAL,
            20,
            20)

        view.locationSearched.text = place.name!!

        view.confirm_button.setOnClickListener{

        }

        view.cancel_button.setOnClickListener{
            window.dismiss()
        }
    }
}