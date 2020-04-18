package com.example.campusguide.directions

import com.google.android.gms.maps.model.LatLng
import java.io.Serializable

open class Test : Serializable{
    private lateinit var steps : List<GoogleDirectionsAPIStep>
    private lateinit var path : List<LatLng>
    private lateinit var pathPolyline: PathPolyline
    private lateinit var start : String
    private lateinit var end : String

    fun setStart(s:String){
        start = s
    }

    fun getStart() : String{
        return start
    }

    fun setEnd(s:String){
        end = s
    }

    fun getEnd() : String{
        return end
    }

    fun setSteps(set: List<GoogleDirectionsAPIStep>) {
        steps = set
    }

    fun getSteps() : List<GoogleDirectionsAPIStep>{
        return steps
    }

    fun setPath(pathPolyline: List<LatLng>) {
        path = pathPolyline
    }

    fun getPath() : List<LatLng> {
        return path
    }

    fun setPathPolyline(newSegment: PathPolyline) {
        pathPolyline = newSegment
    }

    fun getPathPolyline() : PathPolyline {
        return pathPolyline
    }
}