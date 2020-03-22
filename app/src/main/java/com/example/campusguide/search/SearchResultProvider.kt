package com.example.campusguide.search

interface SearchResultProvider {
    suspend fun search(query: String): List<SearchResult>
}