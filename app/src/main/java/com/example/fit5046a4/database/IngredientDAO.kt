package com.example.fit5046a4.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface IngredientDAO {
    @Query("select * from Ingredient")
    fun getAllIngredients():Flow<List<Ingredient>>

    @Insert
    suspend fun insertIngredient(ingredient: Ingredient)

    @Insert
    suspend fun insertIngredients(ingredients: List<Ingredient>)

    @Update
    suspend fun updateIngredient(ingredient: Ingredient)

    @Delete
    suspend fun deleteIngredient(ingredient: Ingredient)

    @Query("SELECT name, AVG(expiryDate - insertDate) AS averageLife FROM Ingredient Group by name" )
    fun averageIngredientLife():Flow<List<IngredientLife>>

    @Query("SELECT * FROM ingredient")
    suspend fun getAllIngredientsOnce(): List<Ingredient>

    @Query("DELETE FROM ingredient")
    suspend fun deleteAllIngredients()
    
}