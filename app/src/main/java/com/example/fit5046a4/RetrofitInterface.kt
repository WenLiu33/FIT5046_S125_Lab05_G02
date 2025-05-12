package com.example.fit5046a4

import retrofit2.Response
import retrofit2.http.GET




interface RetrofitInterface {
    @GET("categories.php")
    suspend fun getCategories(): Response<CategoryResponse>

}