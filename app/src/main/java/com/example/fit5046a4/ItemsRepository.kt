package com.example.fit5046a4

import retrofit2.Response

class ItemsRepository {
    private val searchService = RetrofitObject.retrofitService

    // get api request
    suspend fun getCategories(): Response<CategoryResponse> {
        return searchService.getCategories()
    }
}
