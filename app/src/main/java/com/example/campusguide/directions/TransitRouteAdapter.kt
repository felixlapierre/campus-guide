package com.example.campusguide.directions

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
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
        row.findViewById<TextView>(R.id.routeSteps).text = results[position].steps
        row.findViewById<TextView>(R.id.routeDuration).text = results[position].duration

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
}