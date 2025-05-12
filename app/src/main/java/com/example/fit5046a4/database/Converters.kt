package com.example.fit5046a4.database

import androidx.room.TypeConverter
import java.util.Date

//this class help to convert Long to Date
class Converters {
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? =
        value?.let { Date(it) }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? =
        date?.time
}