package com.example.fit5046a4

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch




class RecipeViewModel : ViewModel() {
    private val _breakfastCategories = MutableLiveData<List<Category>>()
    private val _lunchCategories = MutableLiveData<List<Category>>()
    private val _dinnerCategories = MutableLiveData<List<Category>>()

    val breakfastCategory: LiveData<List<Category>> get() = _breakfastCategories
    val lunchCategory: LiveData<List<Category>> get() = _lunchCategories
    val dinnerCategory: LiveData<List<Category>> get() = _dinnerCategories

    fun fetchCategories() {
        viewModelScope.launch {
            try {
                val response = RetrofitObject.retrofitService.getCategories()
                if (response.isSuccessful) {
                    val categories = response.body()?.categories


                    val breakfast = categories?.filter { it.strCategory.contains("Breakfast", ignoreCase = true) ||
                            it.strCategory.contains("Chicken", ignoreCase = true)||
                            it.strCategory.contains("Vegetarian", ignoreCase = true)}
                    val lunch = categories?.filter { it.strCategory.contains("Lunch", ignoreCase = true) ||
                            it.strCategory.contains("Lamb", ignoreCase = true) ||
                            it.strCategory.contains("Starter", ignoreCase = true) }
                    val dinner = categories?.filter { it.strCategory.contains("Dinner", ignoreCase = true) ||
                            it.strCategory.contains("Pasta", ignoreCase = true)||
                            it.strCategory.contains("Seafood", ignoreCase = true)}

                    _breakfastCategories.value = breakfast ?: emptyList()
                    _lunchCategories.value = lunch ?: emptyList()
                    _dinnerCategories.value = dinner ?: emptyList()
                } else {
                    Log.e( "RecipeViewModel",  "Failed to load categories")
                }
            } catch (e: Exception) {
                Log.e("RecipeViewModel", "Error: ${e.message}")
            }
        }
    }


}