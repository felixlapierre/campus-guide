package com.example.campusguide.search

import com.example.campusguide.search.indoor.BuildingNotFoundException
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import org.junit.Rule
import org.junit.Test
import org.junit.rules.ExpectedException
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import java.lang.IndexOutOfBoundsException

@RunWith(JUnit4::class)
class SearchResultAdapterTest {

    @Test
    fun testGetItem() {
        val searchResults : ArrayList<SearchResult> = ArrayList<SearchResult>()
        val one : SearchResult = SearchResult("primary", "secondary", "one")
        val two : SearchResult = SearchResult("primary", "secondary", "two")
        searchResults.add(one)
        searchResults.add(two)

        assert(searchResults[0] == one)
        assert(searchResults[1] == two)

    }
    @Test(expected = IndexOutOfBoundsException::class)
    fun testGetItemOutOfBounds() {
        val searchResults : ArrayList<SearchResult> = ArrayList<SearchResult>()
        val one : SearchResult = SearchResult("primary", "secondary", "one")
        val two : SearchResult = SearchResult("primary", "secondary", "two")
        searchResults.add(one)

        assert(searchResults[1] == one)
    }

    @Test
    fun testGetItemId() {
        var position : Int = 5

        assert(position.toLong().javaClass == Long::class.java)
    }

    @Test
    fun testClear() {
        val searchResults : ArrayList<SearchResult> = mock()

        val one : SearchResult = SearchResult("primary", "secondary", "one")
        val two : SearchResult = SearchResult("primary", "secondary", "two")
        val three : SearchResult = SearchResult("primary", "secondary", "three")

        searchResults.add(one)
        searchResults.add(one)
        searchResults.add(one)
        searchResults.add(two)
        searchResults.add(three)

        verify(searchResults, times(3)).add(one)
        verify(searchResults, times(1)).add(two)
        verify(searchResults, times(1)).add(three)

        searchResults.clear()

        verify(searchResults).clear()
    }

    @Test
    fun testAdd() {
        val searchResults : ArrayList<SearchResult> = mock()

        val one : SearchResult = SearchResult("primary", "secondary", "one")
        val two : SearchResult = SearchResult("primary", "secondary", "two")
        val three : SearchResult = SearchResult("primary", "secondary", "three")

        searchResults.add(one)
        searchResults.add(one)
        searchResults.add(one)
        searchResults.add(two)
        searchResults.add(three)

        verify(searchResults, times(3)).add(one)
        verify(searchResults, times(1)).add(two)
        verify(searchResults, times(1)).add(three)
    }
}