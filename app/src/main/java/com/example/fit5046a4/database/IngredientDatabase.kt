package com.example.fit5046a4.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import ingredients
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(entities = [Ingredient::class], version = 3, exportSchema = false)
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
                ).addCallback(object : RoomDatabase.Callback() {
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)
                        CoroutineScope(Dispatchers.IO).launch {
                            // Now this will run once the DB is first created
                            getDatabase(context)
                                .ingredientDAO()
                                .insertIngredients(ingredients)
                        }
                    }
                })
                .fallbackToDestructiveMigration(false)
                .build()
                INSTANCE = instance
                instance
            }
        }

    }
}