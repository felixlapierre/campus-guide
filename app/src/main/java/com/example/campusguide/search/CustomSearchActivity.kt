package com.example.campusguide.search

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ListView
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import com.example.campusguide.R
import com.example.campusguide.search.amenities.AmenitiesSearchResultProvider
import com.example.campusguide.search.indoor.BuildingIndexSingleton
import com.example.campusguide.search.indoor.IndoorSearchResultProvider
import com.example.campusguide.search.outdoor.PlacesApiSearchResultProvider
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class CustomSearchActivity : AppCompatActivity(), SearchView.OnQueryTextListener,
    AdapterView.OnItemClickListener {
    private lateinit var searchView: SearchView
    private lateinit var listView: ListView
    private lateinit var adapter: SearchResultAdapter
    private var searchResultProviders: MutableList<SearchResultProvider> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_custom_search)

        // Enables the "Back" button to cancel search
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val buildingIndex = BuildingIndexSingleton.getInstance(this.assets)
        searchResultProviders.add(
            AmenitiesSearchResultProvider()
        )
        searchResultProviders.add(
            IndoorSearchResultProvider(buildingIndex)
        )
        searchResultProviders.add(
            PlacesApiSearchResultProvider(this)
        )

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
        return if (query == null) {
            false
        } else {
            doQuery(query)
            true
        }
    }

    private fun doQuery(query: String) {
        GlobalScope.launch {
            adapter.clear()
            searchResultProviders.forEach { provider ->
                val results = provider.search(query)
                results.forEach { result ->
                    adapter.add(result)
                }
            }
            runOnUiThread { adapter.notifyDataSetChanged() }
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
