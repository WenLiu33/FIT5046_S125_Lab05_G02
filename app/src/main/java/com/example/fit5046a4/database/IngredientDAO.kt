package com.example.fit5046a4.database

import android.adservices.adid.AdId
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface IngredientDAO {
//    @Query("select * from Ingredient")
//    fun getAllIngredients():Flow<List<Ingredient>>

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

    @Query("SELECT * FROM Ingredient WHERE isDeleted = 0")
    fun getAllIngredients(): Flow<List<Ingredient>>

    @Query("SELECT * FROM Ingredient")
    fun getAllIngredientsIncludingDeleted(): Flow<List<Ingredient>> // for bar chart display only

    @Query("UPDATE Ingredient SET isDeleted = 1 WHERE id = :ingredientId")
    suspend fun markIngredientAsDeleted(ingredientId: Int) // for edit ingredient removal

    @Query("UPDATE Ingredient SET isDeleted = 1")
    suspend fun markAllAsDeleted() // for clear fridge function
}