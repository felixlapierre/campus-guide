package com.example.campusguide.search.indoor

import android.content.res.AssetManager
import com.beust.klaxon.Klaxon
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

/**
 * Singleton class to hold the index of indoor buildings and their rooms.
 * Useful, since loading the index of indoor rooms is a long operation -
 * otherwise the app would have to wait 2-3 seconds whenever it is
 * used.
 */
class BuildingIndexSingleton constructor(assets: AssetManager): BuildingIndex{
    /**
     * Companion object that performs the singleton logic, ensuring
     * the instance is unique and can be retrieved statically.
     */
    companion object {
        @Volatile
        private var INSTANCE: BuildingIndexSingleton? = null
        fun getInstance(assets: AssetManager) =
            INSTANCE?: synchronized(this) {
                    INSTANCE?: BuildingIndexSingleton(
                            assets
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
            assets.list(path)?.forEach {it ->
                val contents = assets.open("$path/$it").bufferedReader().use {it.readText()}
                val building = Klaxon().parse<Building>(contents)
                if(building != null)
                    index.add(building)
            }

            buildings = index
        }
    }

    /**
     * Return the index of buildings. Will return null if the
     * buildings haven't been loaded yet.
     */
    override fun getBuildings(): List<Building>? {
        return buildings
    }
}