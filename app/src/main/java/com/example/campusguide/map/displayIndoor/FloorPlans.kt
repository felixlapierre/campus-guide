package com.example.campusguide.map.displayIndoor

import android.content.res.AssetManager
import com.example.campusguide.map.GoogleMapAdapter
import com.example.campusguide.search.indoor.BuildingIndexSingleton
import com.google.android.material.floatingactionbutton.FloatingActionButton

object FloorPlans {
    private var isHidden = true
    private var currentBuilding = ""
    private lateinit var changeFloor: ChangeFloor
    lateinit var floorUpButton: FloatingActionButton
    lateinit var floorDownButton: FloatingActionButton
    lateinit var buildingIndexSingleton: BuildingIndexSingleton

    fun setUpChangeFloor(map: GoogleMapAdapter) {
        changeFloor = ChangeFloor(map, buildingIndexSingleton)
    }

    fun show(buildingName: String) {

        if (currentBuilding != buildingName) {

            currentBuilding = buildingName
            changeFloor.setBuilding(buildingName)
            if (isHidden) {
                displayButtons()
                isHidden = false
            }
        }
    }

    private fun displayButtons() {

        floorUpButton.show()
        floorUpButton.setOnClickListener(changeFloor)

        floorDownButton.show()
        floorDownButton.setOnClickListener(changeFloor)
    }

    fun hide() {
        if (!isHidden) {
            changeFloor.unsetBuilding()
            currentBuilding = ""
            hideButtons()
            isHidden = true
        }
    }

    private fun hideButtons() {
        floorUpButton.hide()
        floorDownButton.hide()
    }
}
