package com.example.fit5046a4.database

import java.util.Date
import java.util.concurrent.TimeUnit

fun Ingredient.isExpiringSoon(days: Int = 3): Boolean{
    val today = Date()
    val diffMillis = expiryDate.time - today.time
    val diffDays = TimeUnit.MILLISECONDS.toDays(diffMillis)

    return diffDays in 0..days
}