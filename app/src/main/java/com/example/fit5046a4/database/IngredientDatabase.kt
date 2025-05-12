package com.example.fit5046a4.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [Ingredient::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class IngredientDatabase : RoomDatabase() {

    abstract fun ingredientDAO(): IngredientDAO

    companion object{
        @Volatile
        private var INSTANCE: IngredientDatabase? = null

        fun getDatabase(context: Context): IngredientDatabase {
            return INSTANCE ?: synchronized(this){
                val instance = Room.databaseBuilder(
                context.applicationContext,
                IngredientDatabase:: class.java,
                "ingredient_database" //file name for DB on disk
                )
                    .fallbackToDestructiveMigration(false)
                    .build()
                INSTANCE = instance
                instance
            }
        }

    }
}