package com.example.campusguide.directions

import java.io.Serializable

open class Test : Serializable{
    private lateinit var steps : List<GoogleDirectionsAPIStep>

    fun setSteps(set: List<GoogleDirectionsAPIStep>) {
        steps = set
    }

    fun getSteps() : List<GoogleDirectionsAPIStep>{
        return steps
    }
}