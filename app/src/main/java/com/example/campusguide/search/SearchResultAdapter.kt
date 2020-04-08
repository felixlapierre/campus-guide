package com.example.campusguide.search

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.example.campusguide.R

/**
 * An Android Adapter class that provides views for elements in a list of SearchResults.
 */
class SearchResultAdapter constructor(private val activity: Activity) : BaseAdapter() {
    private val results: ArrayList<SearchResult> = arrayListOf()

    /**
     * Get the view for the list element at position.
     */
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val inflater = LayoutInflater.from(activity)
        val row = inflater.inflate(R.layout.search_result_item, parent, false)

        row.findViewById<TextView>(R.id.firstLine).text = results[position].primaryText

        row.findViewById<TextView>(R.id.secondLine).text = results[position].secondaryText

        return row
    }

    override fun getItem(position: Int): SearchResult {
        return results[position]
    }

    override fun getItemId(position: Int): Long {
        // This method will be called by the list view
        // but is not necessary for the functionality
        // of the list view to be correct.
        return position.toLong()
    }

    override fun getCount(): Int {
        return results.size
    }

    fun clear() {
        results.clear()
    }

    fun add(result: SearchResult) {
        results.add(result)
    }
}
