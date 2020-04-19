package com.example.campusguide

/**
 * Class handles accessibility requests such as adjustments
 * to directions and/or colors of the application.
 */
object Accessibility {

    var forbiddenRooms: MutableList<String> = emptyList<String>().toMutableList()

    // public methods to be called on click
    fun setEscalators(bool: Boolean) {
        if (bool) forbidEscalators()
        else allowEscalators()
    }

    fun setElevators(bool: Boolean) {
        if (bool) forbidElevators()
        else allowElevators()
    }

    fun setStairs(bool: Boolean) {
        if (bool) forbidStairs()
        else allowStairs()
    }

    // private methods that change pathfinding
    private fun forbidEscalators() {
        forbiddenRooms.add("escalators")
    }

    private fun forbidElevators() {
        forbiddenRooms.add("elevators")
    }

    private fun forbidStairs() {
        forbiddenRooms.add("stairs")
    }

    private fun allowEscalators() {
        forbiddenRooms.remove("escalators")
    }
    private fun allowElevators() {
        forbiddenRooms.remove("elevators")
    }
    private fun allowStairs() {
        forbiddenRooms.remove("stairs")
    }
}
