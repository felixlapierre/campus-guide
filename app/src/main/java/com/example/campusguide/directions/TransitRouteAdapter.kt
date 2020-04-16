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

class TransitRouteAdapter constructor(private val activity: Activity) : BaseAdapter() {

    private val results: ArrayList<TransitRoute> = arrayListOf()

    /**
     * Get a View that displays the data at the specified position in the data set.
     */
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

        val inflater = LayoutInflater.from(activity)
        val row = inflater.inflate(R.layout.transit_route_item, parent, false)

        row.findViewById<TextView>(R.id.routeTitle).text = results[position].title
        row.findViewById<TextView>(R.id.routeDuration).text = results[position].duration

        val steps = results[position].steps

        var n = 3
        if(steps.size < 4) {
            n = steps.size-1
        }

        for (i in 0..n) {
            val iv = ImageView(activity)
            val tv = TextView(activity)
            when(steps[i].travelMode) {
                "DRIVING" -> {
                    iv.setImageResource(R.drawable.ic_directions_car)
                    row.findViewById<LinearLayout>(R.id.routeSteps).addView(iv)
                }
                "WALKING" -> {
                    iv.setImageResource(R.drawable.ic_directions_walk)
                    row.findViewById<LinearLayout>(R.id.routeSteps).addView(iv)
                }
                "TRANSIT" -> {
                    if(steps[i].transitDetails.line.vehicle.name == "Subway") {
                        iv.setImageResource(R.drawable.ic_subway)
                    } else {
                        iv.setImageResource(R.drawable.ic_directions_transit)
                    }
                    tv.text = steps[i].transitDetails.line.shortName
                    if(steps[i].transitDetails.line.color != "") {
                        tv.setBackgroundColor(Color.parseColor(steps[i].transitDetails.line.color))
                    }
                    styleTransitLine(tv)
                    row.findViewById<LinearLayout>(R.id.routeSteps).addView(iv)
                    row.findViewById<LinearLayout>(R.id.routeSteps).addView(tv)
                }
            }
            val tv2 = TextView(activity)
            if(i != n) {
                tv2.text = ">"
                row.findViewById<LinearLayout>(R.id.routeSteps).addView(tv2)
            } else if(steps.size > i+1) {
                tv2.text = "..."
                row.findViewById<LinearLayout>(R.id.routeSteps).addView(tv2)
            }
        }

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