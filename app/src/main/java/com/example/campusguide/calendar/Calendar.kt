package com.example.campusguide.calendar

import com.example.campusguide.Constants
import com.example.campusguide.directions.GoogleDirectionsAPIResponse
import com.example.campusguide.directions.GoogleDirectionsAPIResponseParser
import com.google.api.services.calendar.Calendar
import com.google.api.services.calendar.model.CalendarList
import com.google.api.services.calendar.model.Event
import com.example.campusguide.utils.ErrorListener
import com.example.campusguide.utils.request.RequestDispatcher
import com.example.campusguide.utils.request.VolleyRequestDispatcher

class Calendar {
     private lateinit var requestDispatcher: VolleyRequestDispatcher

     // TODO: review the types for below
     private lateinit var current_calenar: Calendar
     private lateinit var calendar_list: Array<String>

     // todo: ask jon for help :fukt:

     /**
      * Save Calendar IDs locally for UI display.
      */
     private suspend fun getCalendarList() {
          // build http request
          val url = Constants.CALENDAR_LIST_API_URL
          // get jSON object
          val response = requestDispatcher.sendRequest(url)

     }

     /**
      * Save selected calendar for UI display and next/last event.
      */
     private fun calendarInit() {
          // build http request

          // get jSON factory

          // get credentials


//          val service = Calendar.Builder(httpTransport, jsonFactory, credentials)
//               .setApplicationName("applicationName").build()
     }

     // TODO: decide whether to handle location parsing here or somewhere else.
     // TODO: make sure "use last event" or "use next event" are the buttons to call these functions.
     fun getNextEventLocation(){

     }
     fun getPrevEventLocation(){

     }
}