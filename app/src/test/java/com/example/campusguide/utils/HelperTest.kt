package com.example.campusguide.utils

import org.junit.Test

class HelperTest {

    @Test
    fun testCapitalizeWords() {

        val lowerCaseString = "capitalize the words within this sentence"
        val originalStrings = lowerCaseString.split(" ").toMutableList()
        var output = ""
        assert(originalStrings.size == 6)

        for (word in originalStrings) {
            assert(word.capitalize() != word)
            val upperCaseWord = word.capitalize()
            assert(upperCaseWord[0].isUpperCase())
            output += ("$upperCaseWord ")

        }
        assert(output.substring(0, output.length - 1) == output.trim())
    }
}