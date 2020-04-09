package com.example.campusguide.map.displayIndoor

import com.example.campusguide.Constants
import com.example.campusguide.map.GoogleMapAdapter
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds

class OnZoomListener constructor(private val map: GoogleMapAdapter): GoogleMap.OnCameraMoveListener {

    private val buildingBounds: Array<LatLngBounds> = getBuildingBounds()

    override fun onCameraMove() {
        if (map.getCameraZoom() >= Constants.ZOOM_STREET_LVL){
            val location = map.getCameraLocation()
            if (cameraFocusedOnBuilding(location) != null){
                ShowFloorPlan(map);
            }
        }
    }

    private fun cameraFocusedOnBuilding (location: LatLng): String? {

            if (buildingBounds[0].contains(location))
                return "hall";
            else if (buildingBounds[0].contains(location))
                return "library"

            return null
    }

    private fun getBuildingBounds(): Array<LatLngBounds> {
        val library: LatLngBounds.Builder = LatLngBounds.builder()

            library.include(LatLng(45.496691, -73.578638))
            library.include(LatLng(45.496253, -73.577702))
            library.include(LatLng(45.496870, -73.577072))
            library.include(LatLng(45.497293, -73.578063))

        val hall : LatLngBounds.Builder = LatLngBounds.builder()

            hall.include(LatLng(45.497735, -73.579038))
            hall.include(LatLng(45.497162, -73.579559))
            hall.include(LatLng(45.496812, -73.578856))
            hall.include(LatLng(45.497383, -73.578292))

        return arrayOf(library.build(), hall.build());

    }
}