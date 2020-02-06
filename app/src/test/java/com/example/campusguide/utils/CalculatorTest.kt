package com.example.campusguide.utils

import org.junit.Test

import org.junit.Assert.*

class CalculatorTest {

    @Test
    fun add() {
        var first = 5
        var second = 10
        var expected = 15
        assertEquals(expected, Calculator().add(first, second))
    }
}