package com.example.fit5046a4.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity
data class Ingredient(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val quantity: Int,
    val unit: String,
    val unitPrice: Float,
    val insertDate: Date,
    val expiryDate: Date,
    val category: String,
    val originalQuantity: Int,
    val originalUnit:String,
    val isDeleted: Boolean = false
)