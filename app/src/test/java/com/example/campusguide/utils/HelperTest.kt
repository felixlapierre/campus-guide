package com.example.campusguide.utils

import org.junit.Test

class HelperTest {

    @Test
    fun testCapitalizeWords() {

        val lowerCaseString = "capitalize the words within this sentence"
        val capitalized = Helper.capitalizeWords(lowerCaseString)
        assert(capitalized == "Capitalize The Words Within This Sentence")
    }
}
