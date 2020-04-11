package com.example.campusguide.search

/**
 * Interface for a class that can give search results
 * from a query. Searching is a suspend function to
 * allow asynchronous services such as network
 * requests.
 */
interface SearchResultProvider {
    suspend fun search(query: String): List<SearchResult>
}
