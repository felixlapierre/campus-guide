package com.example.campusguide.search

import android.view.Gravity
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.PopupWindow
import androidx.appcompat.app.AppCompatActivity
import com.example.campusguide.R
import com.example.campusguide.directions.DirectionsFlow
import com.google.maps.model.LatLng
import kotlinx.android.synthetic.main.confirm_window.view.*

class CurrentTravelPopup {
    private var view : View
    private var activity: AppCompatActivity
    private var popup: PopupWindow

    constructor(activity: AppCompatActivity, place: SearchLocation, directions: DirectionsFlow){
        this.activity = activity
        popup = PopupWindow(activity)
        popup.setBackgroundDrawable(activity.resources.getDrawable(R.drawable.custom_triangular_shape))
        popup.height = WindowManager.LayoutParams.WRAP_CONTENT
        popup.width = WindowManager.LayoutParams.WRAP_CONTENT
        popup.isOutsideTouchable = true;
        popup.isFocusable = true;

        view = activity.layoutInflater.inflate(R.layout.confirm_window, null)
        popup.contentView = view

        val confirmButton: Button? = view.findViewById(R.id.goHere)
        confirmButton?.setOnClickListener { _ ->
            popup.dismiss()
            directions.startFlow(null, LatLng(place.lat, place.lon).toString())
        }

        popup.showAtLocation(
            activity.findViewById(android.R.id.content),
            Gravity.CENTER_VERTICAL,
            20,
            20)

        view.primaryText.text = place.name
        view.secondaryText.text = place.secondaryText
    }
}