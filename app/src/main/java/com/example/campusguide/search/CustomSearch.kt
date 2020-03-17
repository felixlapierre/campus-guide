package com.example.campusguide.search

import android.content.Intent
import android.view.View
import com.example.campusguide.ActivityResultListener
import com.example.campusguide.MapsActivity

const val AUTOCOMPLETE_REQUEST_CODE = 69; //nice

class CustomSearch constructor(private val activity: MapsActivity) : View.OnClickListener,  ActivityResultListener{
    override fun onClick(v: View?) {
        val searchIntent = Intent(activity, CustomAutocompleteActivity::class.java)
        activity.startActivityForResult(searchIntent, AUTOCOMPLETE_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(requestCode != AUTOCOMPLETE_REQUEST_CODE) { return }


    }
}