package com.example.campusguide.search

import android.app.Activity
import com.example.campusguide.search.indoor.BuildingNotFoundException
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.verifyZeroInteractions
import org.junit.Rule
import org.junit.Test
import org.junit.rules.ExpectedException
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import java.lang.IndexOutOfBoundsException

@RunWith(JUnit4::class)
class SearchResultAdapterTest {

    @Test
    fun testAddClearGetItemGetCount() {
        val one : SearchResult = SearchResult("primary", "secondary", "one")
        val two : SearchResult = SearchResult("primary", "secondary", "two")
        val three : SearchResult = SearchResult("primary", "secondary", "three")

        val listener = SearchResultAdapter(mock())

        listener.add(one)
        listener.add(two)
        listener.add(three)

        assert(listener.count == 3)
        assert(listener.getItem(0) == one)
        assert(listener.getItem(1) == two)
        assert(listener.getItem(2) == three)

        listener.clear()
        assert(listener.count == 0)
    }

    @Test(expected = IndexOutOfBoundsException::class)
    fun testGetItemOutOfBounds() {
        val listener = SearchResultAdapter(mock())
        listener.getItem(0)
    }

    @Test
    fun testGetItemId() {
        val listener = SearchResultAdapter(mock())
        val num : Int = 5
        val test : Long = listener.getItemId(5)
        assert(test == num.toLong())
    }
}
