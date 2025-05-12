package com.example.fit5046a4

class ItemsRepository {
    private val searchService = RetrofitObject.retrofitService
    private val API_KEY = "AIzaSyBCcZuquWf8PHvJ-aI_lbwsqb8j1Abdfk8"
    private val SEARCH_ID_cx = "f388f2bb09b914cf8"
    suspend fun getResponse(keyword: String): SearchResponse {
        return searchService.customSearch(
            API_KEY,
            SEARCH_ID_cx,
            keyword
        ) }
}