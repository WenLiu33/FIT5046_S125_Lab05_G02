package com.example.fit5046a4.workManager

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Build.VERSION_CODES
import android.util.Log
import androidx.compose.ui.tooling.data.ContextCache
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.work.WorkManager
import com.example.fit5046a4.R
import com.example.fit5046a4.database.Ingredient
import com.example.fit5046a4.database.IngredientDatabase
import com.example.fit5046a4.database.isExpiringSoon

/**
 * Worker class to check for expiring ingredients and send notifications.
 * and sends a notification with the list of expiring items if any are found.
 *
 * @param context The application context.
 * @param workerParams Parameters for the worker.
 *
 * @author Sylvia
 * @version 2.0
 */
class FridgeWorker (
    private val context: Context,
    workerParams: WorkerParameters
): CoroutineWorker(context, workerParams) {

    /**
     * Perform the background task to check for expiring ingredients and send notifications.
     *
     * @return The result of the worker operation. Returns Result.success() if the task completes successfully.
     */
    override suspend fun doWork(): Result {

        //need to demo
        Log.d("FridgeWorker", "App started: Triggering scheduled job automatically.")
        // get all ingredients from room
        val db = IngredientDatabase.getDatabase(context)
        val ingredients: List<Ingredient> = db.ingredientDAO().getAllIngredientsOnce()

        // filter ingredients expiring less than 5 days
        val itemExpiringSoon = ingredients.filter { it.isExpiringSoon(5) }

        // push a notification if any item going to expire
        if (itemExpiringSoon.isNotEmpty()) {
            sendNotification(itemExpiringSoon)
        }

        // Return a success result
        return Result.success()
    }


    /**
     * Send a notification with the list of items that are expiring in 5 days.
     *
     * This method creates a notification if the device has the required permissions, including creating
     * a notification channel for devices running Android 8.0 (Oreo) or later.
     *
     * @param items A list of [Ingredient] objects that are expiring soon.
     * @return The result of the worker operation. Returns [Result.success()] after sending the notification.
     */
    private fun sendNotification(items: List<Ingredient>): Result {

        // AI-generated: if Android 13+, check for POST_NOTIFICATIONS permission
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
            ContextCompat.checkSelfPermission(
                context,
                android.Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // if Permission not granted, skip
            return Result.success()
        }

        // use a comma separated list of item
        val itemName = items.joinToString(", ") { it.name }

        // AI-generated: create channel (Android 8+)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "fridge_channel",
                "Fridge Notifications",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            val manager = context.getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channel)
        }

        // AI-generated: build and send the notification
        val notification = NotificationCompat.Builder(context, "fridge_channel")
            .setSmallIcon(R.drawable.expiry_date)
            .setContentTitle("Items Expiry Alert")
            .setContentText("Items expiring soon: $itemName")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT) // normal important
            .build()

        NotificationManagerCompat.from(context).notify(100, notification)

        //  return success
        return Result.success()
    }
}

