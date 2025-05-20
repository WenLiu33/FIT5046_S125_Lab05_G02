package com.example.fit5046a4.api

import android.util.Log
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * This object initializes the Retrofit instance, sets the base URL, and adds the Gson converter factory.
 * It exposes the [retrofitService] to interact with the API by calling methods defined in the [RetrofitInterface].
 *
 * @author Sylvia
 * @version 1.0
 */
object RetrofitObject {
    private val BASE_URL = "https://www.themealdb.com/api/json/v1/1/"
    val retrofitService: RetrofitInterface by lazy{
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        Log.d("Retrofit", "Base URL: $BASE_URL")
        retrofit.create(RetrofitInterface::class.java)
    }
}