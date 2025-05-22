package com.example.fit5046a4.bottomNavigationBarAndTopBar

import androidx.annotation.DrawableRes

data class NavRoute(
    val route:String,
    @DrawableRes val iconRes: Int,
    val label:String
)