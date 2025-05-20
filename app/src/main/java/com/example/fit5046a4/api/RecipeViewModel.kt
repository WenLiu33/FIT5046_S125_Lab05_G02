package com.example.fit5046a4.api

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

/**
 * ViewModel for managing and providing data related to food categories for different meal types (breakfast, lunch, dinner).
 *
 * This ViewModel fetches food category data using a Retrofit service and filters the categories based on keywords for each meal type.
 * The filtered data is then exposed through LiveData for observation by the UI.
 *
 * @property breakfastCategory LiveData containing a list of food categories related to breakfast.
 * @property lunchCategory LiveData containing a list of food categories related to lunch.
 * @property dinnerCategory LiveData containing a list of food categories related to dinner.
 *
 * @author Sylvia
 * @version 2.0
 */
class RecipeViewModel : ViewModel() {
    // private mutable livedata to store each list
    private val _breakfastCategories = MutableLiveData<List<Category>>()
    private val _lunchCategories = MutableLiveData<List<Category>>()
    private val _dinnerCategories = MutableLiveData<List<Category>>()

    //public immutable livedata for UI to observe
    val breakfastCategory: LiveData<List<Category>> get() = _breakfastCategories
    val lunchCategory: LiveData<List<Category>> get() = _lunchCategories
    val dinnerCategory: LiveData<List<Category>> get() = _dinnerCategories

    /**
     * Fetches the categories of food from the Retrofit service and filters them based on meal types (breakfast, lunch, dinner).
     *
     * The method performs an API call to retrieve the food categories and categorizes them into breakfast, lunch, and dinner
     * based on keywords in the category names. These filtered lists are then posted to the corresponding LiveData properties.
     */
    fun fetchCategories() {
        viewModelScope.launch {
            try {
                val response = RetrofitObject.retrofitService.getCategories()
                if (response.isSuccessful) {
                    val categories = response.body()?.categories


                    //AI-generated: filter based on keywords for each meal type
                    val breakfast = categories?.filter { it.strCategory.contains("Chicken", ignoreCase = true) ||
                            it.strCategory.contains("Breakfast", ignoreCase = true)||
                            it.strCategory.contains("Vegetarian", ignoreCase = true)}
                    val lunch = categories?.filter { it.strCategory.contains("Lunch", ignoreCase = true) ||
                            it.strCategory.contains("Lamb", ignoreCase = true) ||
                            it.strCategory.contains("Starter", ignoreCase = true) }
                    val dinner = categories?.filter { it.strCategory.contains("Dinner", ignoreCase = true) ||
                            it.strCategory.contains("Pasta", ignoreCase = true)||
                            it.strCategory.contains("Seafood", ignoreCase = true)}

                    // Post filtered results to LiveData
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