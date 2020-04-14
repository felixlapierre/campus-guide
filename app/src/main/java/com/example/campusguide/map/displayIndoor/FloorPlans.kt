package com.example.campusguide.map.displayIndoor

import com.example.campusguide.map.GoogleMapAdapter
import com.example.campusguide.map.displ.ChangeFloor
import com.google.android.material.floatingactionbutton.FloatingActionButton

object FloorPlans {

    private var currentBuilding = ""
    private lateinit var changeFloor : ChangeFloor
    lateinit var floorUpButton: FloatingActionButton
    lateinit var floorDownButton: FloatingActionButton

    fun setUpChangeFloor(map: GoogleMapAdapter){
        changeFloor = ChangeFloor(map)
    }
    fun show(buildingName: String){
        if (currentBuilding != buildingName){

            currentBuilding = buildingName
            changeFloor.setBuilding(buildingName)
            displayButtons()
        }
    }
    private fun displayButtons(){
            floorUpButton.show()
            floorUpButton.setOnClickListener(changeFloor);

            floorDownButton.show()
            floorDownButton.setOnClickListener(changeFloor)
    }

    fun hide(){
        changeFloor.unsetBuilding()
        currentBuilding = ""
        hideButtons()
    }
    private fun hideButtons(){
            floorUpButton.hide()
            floorDownButton.hide()
    }
}