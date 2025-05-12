package com.example.fit5046a4

import android.util.Log
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

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