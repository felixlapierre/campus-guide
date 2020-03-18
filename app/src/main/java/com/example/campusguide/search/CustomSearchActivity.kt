package com.example.campusguide.search

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.campusguide.R
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class CustomSearchActivity : AppCompatActivity(), SearchView.OnQueryTextListener, AdapterView.OnItemClickListener {
    private lateinit var searchView: SearchView
    private lateinit var listView: ListView
    private lateinit var adapter: SearchResultAdapter
    private lateinit var searchResultProvider: PlacesApiSearchResultProvider

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_custom_search)
        
        // Enables the "Back" button to cancel search
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        searchResultProvider = PlacesApiSearchResultProvider(this)
        searchView = findViewById(R.id.searchView)
        listView = findViewById(R.id.searchResults)
        adapter = SearchResultAdapter(this)

        listView.adapter = adapter

        searchView.setOnQueryTextListener(this)
        searchView.requestFocus()

        listView.onItemClickListener = this
    }

    /**
     * Called when the back button is pressed; cancels the search.
     */
    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    /**
     * Called when the user clicks the search button
     */
    override fun onQueryTextSubmit(query: String?): Boolean {
        return queryIfNotNull(query)
    }

    /**
     * Called whenever the text in the search box changes
     */
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
            adapter.clear()
            response.autocompletePredictions.forEach { it ->
                val primaryText = it.getPrimaryText(null).toString()
                val secondaryText = it.getSecondaryText(null).toString()
                val id = it.placeId
                val searchResult = SearchResult(primaryText, secondaryText, id)

                adapter.add(searchResult)
            }
            runOnUiThread{ adapter.notifyDataSetChanged() }
        }
    }

    /**
     * Called whenever an item in the search result list is clicked
     */
    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        val selectedResult = adapter.getItem(position)
        val result = Intent()
        result.data = Uri.parse(selectedResult.id)
        setResult(RESULT_OK, result)
        finish()
    }
}