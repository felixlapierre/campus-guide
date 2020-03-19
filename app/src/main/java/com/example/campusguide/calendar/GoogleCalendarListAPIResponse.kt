package com.example.campusguide.calendar

import com.beust.klaxon.*

data class GoogleCalendarListAPIResponse (

    @Json(name = "kind")
    val kind: String,

    @Json(name = "etag")
    val etag: String,

    @Json(name = "nextPageToken")
    val nextPageToken: String,

    @Json(name = "nextSyncToken")
    val nextSyncToken: String,

    @Json(name = "items")
    val items: List<Calendar>
)