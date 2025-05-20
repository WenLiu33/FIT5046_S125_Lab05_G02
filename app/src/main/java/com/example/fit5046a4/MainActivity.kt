package com.example.fit5046a4

import android.content.Context
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.impl.WorkManagerImpl
import com.example.fit5046a4.ui.theme.FIT5046A4Theme
import com.example.fit5046a4.workManager.FridgeWorker
import java.util.concurrent.TimeUnit
import java.util.stream.DoubleStream.builder
import java.time.Duration
import java.time.LocalDateTime
import androidx.work.OneTimeWorkRequestBuilder
import androidx.core.content.ContextCompat
import androidx.core.app.ActivityCompat
import android.content.pm.PackageManager
import android.util.Log



class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // notification permission
        requestNotificationPermission()

        // Schedule WorkManager (real schedule)
        scheduleFridgeWorker(this)

//         AI-generated: TEST Run OneTime request immediately for testing
//        val testRequest = OneTimeWorkRequestBuilder<FridgeWorker>().build()
//        WorkManager.getInstance(this).enqueue(testRequest)


        enableEdgeToEdge()
        setContent {
            FIT5046A4Theme {
                AppRoot()
            }
        }
    }

    /**
     * Requests notification permission for Android versions TIRAMISU (API 33) and above.
     * If the permission is not granted, it prompts the user to allow it.
     */
    // AI-generated: ask user for notification permission
    private fun requestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    android.Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(android.Manifest.permission.POST_NOTIFICATIONS),
                    100
                )
            }
        }
    }
}

/**
 * Calculates the delay in milliseconds until the next 12 PM (noon).
 * The delay is adjusted depending on whether the current time is already past 12 PM.
 *
 * @return The delay in milliseconds until the next 12 PM.
 */
@RequiresApi(Build.VERSION_CODES.O)
fun calculateDelayUntil2PM(): Long {
    val now = LocalDateTime.now()
    val targetTime = now.withHour(12).withMinute(0).withSecond(0).withNano(0)

    val delay = if (now.isAfter(targetTime)) {
        Duration.between(now, targetTime.plusDays(1))  // Schedule for the next day if already past 12 PM
    } else {
        Duration.between(now, targetTime) // Schedule for the same day if before 12 PM
    }
    return delay.toMillis()
}

/**
 * Schedules the `FridgeWorker` to run periodically every 24 hours at 12 PM.
 * The worker will check for items in the fridge that are expiring soon and notify the user.
 *
 * @param context The application context.
 */
//AI-generated: schedule FridgeWorker notification
@RequiresApi(Build.VERSION_CODES.O)
fun scheduleFridgeWorker(context: Context) {
    val delayInMillis = calculateDelayUntil2PM()

    val request = PeriodicWorkRequestBuilder<FridgeWorker>(1, TimeUnit.DAYS)
        .setInitialDelay(delayInMillis, TimeUnit.MILLISECONDS)
        .build()

    WorkManager.getInstance(context).enqueueUniquePeriodicWork(
        "fridge_worker",
        ExistingPeriodicWorkPolicy.UPDATE,
        request
    )

}


//@Composable
//fun Greeting(name: String, modifier: Modifier = Modifier) {
//    Text(
//        text = "Hello $name!",
//        modifier = modifier
//    )
//}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    FIT5046A4Theme {
        BottomNavigationBarAndTopBar()
    }
}