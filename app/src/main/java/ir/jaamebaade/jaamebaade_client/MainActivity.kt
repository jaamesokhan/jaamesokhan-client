package ir.jaamebaade.jaamebaade_client

import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.WindowCompat
import dagger.hilt.android.AndroidEntryPoint
import ir.jaamebaade.jaamebaade_client.notifications.NotificationService
import ir.jaamebaade.jaamebaade_client.repository.FontRepository
import ir.jaamebaade.jaamebaade_client.repository.ThemeRepository
import ir.jaamebaade.jaamebaade_client.utility.SharedPrefManager
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var fontRepository: FontRepository

    @Inject
    lateinit var themeRepository: ThemeRepository

    @Inject
    lateinit var sharedPrefManager: SharedPrefManager

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission(),
    ) { _: Boolean ->
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        WindowCompat.setDecorFitsSystemWindows(window, false)

        createNotificationChannel()

        setContent {
            val startDestination = intent.getStringExtra("destination_route")

            AppNavHost(
                fontRepository,
                themeRepository,
                sharedPrefManager,
                requestPermissionLauncher::launch,
                startDestination,
            )
        }
    }

    private fun createNotificationChannel() {
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(
            NotificationService.CHANNEL_ID,
            NotificationService.CHANNEL_NAME,
            importance
        )
        val notificationManager: NotificationManager =
            getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }
}