package com.example.fit5046a4

import android.app.Application
import com.example.fit5046a4.database.Ingredient
import com.example.fit5046a4.database.IngredientDAO
import com.example.fit5046a4.database.IngredientDatabase
import kotlinx.coroutines.flow.Flow

class IngredientRepository(application: Application) {
    private var ingredientDAO: IngredientDAO = IngredientDatabase.getDatabase(application).ingredientDAO()
    val allIngredients: Flow<List<Ingredient>> = ingredientDAO.getAllIngredients()

    suspend fun insert(ingredient: Ingredient){
        ingredientDAO.insertIngredient(ingredient)
    }

    suspend fun delete(ingredient: Ingredient){
        ingredientDAO.deleteIngredient(ingredient)
    }

    suspend fun update(ingredient: Ingredient){
        ingredientDAO.updateIngredient(ingredient)
    }

    suspend fun deleteAllIngredients() {
        ingredientDAO.deleteAllIngredients()
    }

}