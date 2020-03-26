package com.example.campusguide.map

import android.view.View
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