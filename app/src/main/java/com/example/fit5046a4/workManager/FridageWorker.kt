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


class FridageWorker (
    private val context: Context,
    workerParams: WorkerParameters
): CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        Log.d("FridageWorker", "Worker is running...") // ✅ Log here to check

        // get all ingredients from room
        val db = IngredientDatabase.getDatabase(context)
        val ingredients: List<Ingredient> = db.ingredientDAO().getAllIngredientsOnce()

        // filter ingredients expiring less than 3 days
        val itemExpiringSoon = ingredients.filter { it.isExpiringSoon(3) }

        // push a notification if any item going to expire
        if (itemExpiringSoon.isNotEmpty()) {
            sendNoticication(itemExpiringSoon)
        }

        // Later: add sync to Firebase here

        // Return a success result
        return Result.success()
    }

    private fun sendNoticication(items: List<Ingredient>): Result {
        // if Android 13+, check permission first
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
            ContextCompat.checkSelfPermission(
                context,
                android.Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // Permission not granted, skip
            return Result.success()
        }

        val itemName = items.joinToString(", ") { it.name }

        // create channel (Android 8+)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "fridge_channel",
                "Fridge Notifications",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            val manager = context.getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channel)
        }

        //  Build and send the notification
        val notification = NotificationCompat.Builder(context, "fridge_channel")
            .setSmallIcon(R.drawable.expiry_date)
            .setContentTitle("Items Expiry Alert")
            .setContentText("Items expiring soon: $itemName")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()

        NotificationManagerCompat.from(context).notify(1001, notification)

        //  return success
        return Result.success()
    }
}





