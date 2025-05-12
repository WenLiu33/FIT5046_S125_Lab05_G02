package com.example.fit5046a4.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity
data class Ingredient(
    @PrimaryKey(autoGenerate = false)
    val name: String,
    val quantity: Int,
    val unit: String,
    val unitPrice: Float,
    val expiryDate: Date
)