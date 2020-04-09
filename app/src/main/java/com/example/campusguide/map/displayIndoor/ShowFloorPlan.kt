package com.example.campusguide.map.displayIndoor

import android.view.View
import com.example.campusguide.map.GoogleMapAdapter
import com.google.android.gms.maps.model.LatLng
import com.google.android.material.floatingactionbutton.FloatingActionButton

class ShowFloorPlan constructor(private val map: GoogleMapAdapter)
    : View.OnClickListener {

    private var changeFloor = ChangeFloor(map)
    lateinit var floorUpButton: FloatingActionButton
    lateinit var floorDownButton: FloatingActionButton
    private var isClicked = false

    fun setUp(upButtonId: Int,downButtonId :Int){
        changeFloor.setIds(upButtonId, downButtonId)
    }

    override fun onClick(v: View?) {
        isClicked = !isClicked

        if (isClicked){

            changeFloor.displayCurrentFloor()

           // val building = map.getCameraTarget()
            if (floorUpButton != null) {
                floorUpButton.show()
                floorUpButton.setOnClickListener(changeFloor);

            }
            if (floorDownButton != null) {
                floorDownButton.show()
                floorDownButton.setOnClickListener(changeFloor)
            }
        }
        else{
            changeFloor.hideCurrentFloor()
            if (floorUpButton != null) {
                floorUpButton.hide()
            }
            if (floorDownButton != null) {
                floorDownButton.hide()
            }
        }

    }
    private fun getBuilding(coordinates:LatLng){
        //TODO: get building at point

    }

}