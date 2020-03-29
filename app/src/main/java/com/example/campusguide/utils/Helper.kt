package com.example.campusguide.utils

class Helper {

    companion object {
        fun capitalizeWords(location: String) : String {
            val words = location.split(" ").toMutableList()
            var output = ""
            for (word in words) {
                output += word.capitalize() + " "
            }
            output = output.trim()
            return output
        }
    }
}