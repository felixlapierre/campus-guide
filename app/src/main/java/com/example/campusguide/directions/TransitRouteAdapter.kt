package com.example.campusguide.directions

import android.app.Activity
import android.graphics.Color
import android.graphics.Typeface
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.example.campusguide.R
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class TransitRouteAdapter constructor(private val activity: Activity) : BaseAdapter() {

    private val results: ArrayList<TransitRoute> = arrayListOf()

    /**
     * Get a View that displays the data at the specified position in the data set.
     */
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

        val inflater = LayoutInflater.from(activity)
        val row = inflater.inflate(R.layout.transit_route_item, parent, false)

        // Set the route title
        row.findViewById<TextView>(R.id.routeTitle).text = results[position].title

        // Set the route travel time
        val travelTime = "${results[position].duration / 60} min"
        row.findViewById<TextView>(R.id.routeDuration).text = travelTime

        // Display the first four steps
        val steps = results[position].steps
        var n = 3
        if(steps.size < 4) {
            n = steps.size-1
        }
        for (i in 0..n) {
            val stepIcon = ImageView(activity)
            val stepTransitLine = TextView(activity)
            when(steps[i].travelMode) {
                "DRIVING" -> {
                    stepIcon.setImageResource(R.drawable.ic_directions_car)
                    row.findViewById<LinearLayout>(R.id.routeSteps).addView(stepIcon)
                }
                "WALKING" -> {
                    stepIcon.setImageResource(R.drawable.ic_directions_walk)
                    row.findViewById<LinearLayout>(R.id.routeSteps).addView(stepIcon)
                }
                "TRANSIT" -> {
                    if(steps[i].transitDetails.line.vehicle.name == "Subway") {
                        stepIcon.setImageResource(R.drawable.ic_subway)
                    } else {
                        stepIcon.setImageResource(R.drawable.ic_directions_transit)
                    }
                    stepTransitLine.text = steps[i].transitDetails.line.shortName
                    if(steps[i].transitDetails.line.color != "") {
                        stepTransitLine.setBackgroundColor(
                            Color.parseColor(steps[i].transitDetails.line.color)
                        )
                    }
                    styleTransitLine(stepTransitLine)
                    row.findViewById<LinearLayout>(R.id.routeSteps).addView(stepIcon)
                    row.findViewById<LinearLayout>(R.id.routeSteps).addView(stepTransitLine)
                }
            }
            val separator = TextView(activity)
            separator.setPadding(20, 0, 20, 0)
            if(i != n) {
                separator.text = ">"
                row.findViewById<LinearLayout>(R.id.routeSteps).addView(separator)
            } else if(steps.size > i+1) {
                separator.text = "..."
                row.findViewById<LinearLayout>(R.id.routeSteps).addView(separator)
            }
        }

        // Display the departure and arrival times
        val df = SimpleDateFormat("h:mm aa", Locale.CANADA)
        val time = Calendar.getInstance()
        val departureTime = df.format(time.time)
        time.add(Calendar.SECOND, results[position].duration)
        val arrivalTime = df.format(time.time)
        val tv = TextView(activity)
        val text = "$departureTime \u2014 $arrivalTime"
        tv.text = text
        row.findViewById<LinearLayout>(R.id.transitRouteItemLayout).addView(tv)

        return row
    }

    /**
     * Get the data item associated with the specified position in the data set.
     */
    override fun getItem(position: Int): TransitRoute {
        return results[position]
    }

    /**
     * Get the row id associated with the specified position in the list.
     */
    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    /**
     * How many items are in the data set represented by this Adapter.
     */
    override fun getCount(): Int {
        return results.size
    }

    /**
     * Add a data item to the data set.
     */
    fun add(result: TransitRoute) {
        results.add(result)
    }

    private fun styleTransitLine(tv: TextView) {
        tv.minWidth = 100
        tv.minHeight = 65
        tv.gravity = Gravity.CENTER
        tv.typeface = Typeface.DEFAULT_BOLD
        tv.setTextColor(Color.BLACK)
    }
}