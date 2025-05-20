package com.example.fit5046a4.api

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
/**
 * Interface for making Retrofit API calls related to food categories.
 *
 * This interface defines the method to fetch food categories from the remote API.
 *
 * @author Sylvia
 * @version 1.0
 */
interface RetrofitInterface {
    @GET("categories.php")
    suspend fun getCategories(): Response<CategoryResponse>

}