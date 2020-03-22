package com.example.campusguide.search.indoor

import android.app.Activity
import com.beust.klaxon.Klaxon
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class BuildingIndexSingleton constructor(activity: Activity){
    companion object {
        @Volatile
        private var INSTANCE: BuildingIndexSingleton? = null
        fun getInstance(activity: Activity) =
            INSTANCE?: synchronized(this) {
                    INSTANCE?: BuildingIndexSingleton(
                            activity
                        ).also {
                            INSTANCE = it
                        }
                }
    }

    private var buildings: List<Building>? = null

    init {
        GlobalScope.launch {
            val path = "index"
            val index: MutableList<Building> = mutableListOf()
            activity.assets.list(path)?.forEach {it ->
                val contents = activity.assets.open("$path/$it").bufferedReader().use {it.readText()}
                val building = Klaxon().parse<Building>(contents)
                if(building != null)
                    index.add(building)
            }

            buildings = index
        }
    }

    fun getBuildings(): List<Building>? {
        return buildings
    }
}