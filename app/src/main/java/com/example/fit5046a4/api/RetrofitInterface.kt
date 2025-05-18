package com.example.fit5046a4.api

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query




interface RetrofitInterface {
    @GET("categories.php")
    suspend fun getCategories(): Response<CategoryResponse>

}