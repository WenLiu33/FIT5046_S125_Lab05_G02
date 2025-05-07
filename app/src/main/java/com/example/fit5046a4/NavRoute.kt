package com.example.fit5046a4

import androidx.annotation.DrawableRes
import androidx.compose.ui.graphics.vector.ImageVector

data class NavRoute(
    val route:String,
    @DrawableRes val iconRes: Int,
    val label:String
)