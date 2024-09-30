package ir.jaamebaade.jaamebaade_client

import android.Manifest
import android.app.AlertDialog
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.toArgb
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat
import dagger.hilt.android.AndroidEntryPoint
import ir.jaamebaade.jaamebaade_client.repository.FontRepository
import ir.jaamebaade.jaamebaade_client.repository.ThemeRepository
import ir.jaamebaade.jaamebaade_client.ui.theme.AppThemeType
import ir.jaamebaade.jaamebaade_client.ui.theme.primaryDark
import ir.jaamebaade.jaamebaade_client.ui.theme.primaryLight
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

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun showPermissionRationale() {
        AlertDialog.Builder(this)
            .setTitle(getString(R.string.NOTIFICATION_ACCESS_TITLE))
            .setMessage(getString(R.string.NOTIFICATION_ACCESS_BODY))
            .setPositiveButton(getString(R.string.YES)) { _, _ ->
                // Directly ask for the permission
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
            .setNegativeButton(getString(R.string.NOT_NOW)) { dialog, _ ->
                dialog.dismiss()
                sharedPrefManager.setNotificationPermissionPreference(false)
            }
            .create()
            .show()
    }

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission(),
    ) { _: Boolean ->
    }


    private fun askNotificationPermission() {
        // This is only necessary for API level >= 33 (TIRAMISU)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) ==
                PackageManager.PERMISSION_GRANTED
            ) {
                // FCM SDK can post notifications.
            } else if (sharedPrefManager.getNotificationPermissionPreference() &&
                shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS)
            ) {
                showPermissionRationale()
            } else {
                // Directly ask for the permission
                if (sharedPrefManager.getNotificationPermissionPreference()) {
                    requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            val appTheme by themeRepository.appTheme.collectAsState()
            val isInDark = isSystemInDarkTheme()
            val navBarColor = when (appTheme) {
                AppThemeType.LIGHT -> primaryLight
                AppThemeType.DARK -> primaryDark
                AppThemeType.SYSTEM_AUTO -> {
                    if (isInDark) primaryDark
                    else primaryLight
                }
            }
            window.navigationBarColor = navBarColor.toArgb()
            WindowInsetsControllerCompat(window, window.decorView).isAppearanceLightNavigationBars =
                true
            AppNavHost(fontRepository, themeRepository)
        }
        askNotificationPermission()
    }
}