package com.example.fit5046a4

import android.content.Context
import android.widget.Toast

// A utility object to hold common reusable functions across the app
object AppUtil {

    fun showToast(context: Context, message: String){
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }
}