package com.example.campusguide.map.displayIndoor

object FloorPlans {

    private lateinit var showFloorPlan: ShowFloorPlan

    fun setShowFloorPlan(sfp: ShowFloorPlan){
        showFloorPlan = sfp
    }

    fun show(buildingName: String){
        showFloorPlan.setBuilding(buildingName)
        showFloorPlan.displayButtons()
    }

    fun hide(){
        showFloorPlan.hideButtons()
    }
}