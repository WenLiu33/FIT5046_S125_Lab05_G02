package com.example.fit5046a4.api

import retrofit2.Response

class CategoryRepository {
    private val searchService = RetrofitObject.retrofitService

    // get api request
    suspend fun getCategories(): Response<CategoryResponse> {
        return searchService.getCategories()
    }
}
