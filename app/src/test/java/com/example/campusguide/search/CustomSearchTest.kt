package com.example.campusguide.search

import com.example.campusguide.MapsActivity
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.FetchPlaceRequest
import com.nhaarman.mockitokotlin2.*
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class CustomSearchTest {

    val mockMap : Map = mock()
    val mockActivity : MapsActivity = mock()

    @Test
    fun testOnClick() {

    }

    @Test
    fun testGetPlaces() {

        val mockFields : List<Place.Field> = mock()
        val mockFetchPlaceRequest : FetchPlaceRequest = mock()
        val mockPlaces : Places = spy()

        Places.createClient(mockActivity).fetchPlace()



    }


}