package com.example.fit5046a4

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.fit5046a4.database.Ingredient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class IngredientViewModel(application: Application) : AndroidViewModel(application){
    private val cRepository: IngredientRepository

    init{
        cRepository = IngredientRepository(application)
    }

    val allIngredients: Flow<List<Ingredient>> = cRepository.allIngredients

    fun insertIngredient(ingredient: Ingredient) = viewModelScope.launch(Dispatchers.IO){
        cRepository.insert(ingredient)
    }

    fun updateIngredient(ingredient: Ingredient) = viewModelScope.launch(Dispatchers.IO){
        cRepository.update(ingredient)
    }
    fun deleteIngredient(ingredient: Ingredient) = viewModelScope.launch(Dispatchers.IO){
        cRepository.delete(ingredient)
    }
}