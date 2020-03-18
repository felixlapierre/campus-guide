package com.example.campusguide.search

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListAdapter
import android.widget.ListView
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import com.example.campusguide.R
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class CustomSearchActivity : AppCompatActivity(), SearchView.OnQueryTextListener {
    private lateinit var searchView: SearchView
    private lateinit var listView: ListView
    private lateinit var adapter: ArrayAdapter<String>
    private lateinit var searchResultProvider: PlacesApiSearchResultProvider
    private val searchResults = arrayListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_custom_search)

        searchResultProvider = PlacesApiSearchResultProvider(this)
        searchView = findViewById(R.id.searchView)
        listView = findViewById(R.id.searchResults)
        adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, searchResults)

        listView.adapter = adapter

        searchView.setOnQueryTextListener(this)
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        return queryIfNotNull(query)
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        return queryIfNotNull(newText)
    }

    private fun queryIfNotNull(query: String?): Boolean {
        return if(query  == null) {
            false
        } else {
            doQuery(query)
            true
        }
    }

    private fun doQuery(query: String) {
        GlobalScope.launch {
            val response = searchResultProvider.search(query)
            searchResults.clear()
            response.autocompletePredictions.forEach { it ->
                searchResults.add(it.getPrimaryText(null).toString())
            }
            runOnUiThread{ adapter.notifyDataSetChanged() }
        }
    }
}