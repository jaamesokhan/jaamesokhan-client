package ir.jaamebaade.jaamebaade_client

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.toArgb
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

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission(),
    ) { _: Boolean ->
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
            AppNavHost(
                fontRepository,
                themeRepository,
                sharedPrefManager,
                requestPermissionLauncher::launch
            )
        }
    }
}