package com.example.fit5046a4   // ← put your actual package here

val Int.dayInMillis: Long
    get() = this * 24L * 60L * 60L * 1000L
