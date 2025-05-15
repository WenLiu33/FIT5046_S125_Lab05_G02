package com.example.fit5046a4.api

import retrofit2.Response

class ItemsRepository {
    private val searchService = RetrofitObject.retrofitService

    // get api request
    suspend fun getCategories(): Response<CategoryResponse> {
        return searchService.getCategories()
    }

    // Add this method for keyword search
    suspend fun getResponse(keyword: String): SearchResponse {
        return searchService.getSearchResults(keyword)
    }
}
