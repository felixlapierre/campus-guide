package com.example.campusguide.search

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.campusguide.R
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class CustomSearchActivity : AppCompatActivity(), SearchView.OnQueryTextListener, AdapterView.OnItemClickListener {
    private lateinit var searchView: SearchView
    private lateinit var listView: ListView
    private lateinit var adapter: ArrayAdapter<String>
    private lateinit var searchResultProvider: PlacesApiSearchResultProvider
    private val searchResults = arrayListOf<String>()
    private val resultIds = arrayListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_custom_search)

        searchResultProvider = PlacesApiSearchResultProvider(this)
        searchView = findViewById(R.id.searchView)
        listView = findViewById(R.id.searchResults)
        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, searchResults)

        listView.adapter = adapter

        searchView.setOnQueryTextListener(this)

        listView.onItemClickListener = this
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
                resultIds.add(it.placeId)
            }
            runOnUiThread{ adapter.notifyDataSetChanged() }
        }
    }

    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        val selectedResult = resultIds[position]
        val result = Intent()
        result.data = Uri.parse(selectedResult)
        setResult(RESULT_OK, result)
        finish()
    }
}