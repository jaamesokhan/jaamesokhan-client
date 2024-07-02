package com.example.jaamebaade_client

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.ui.graphics.toArgb
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.example.jaamebaade_client.repository.FontRepository
import com.example.jaamebaade_client.ui.theme.primaryDark
import com.example.jaamebaade_client.ui.theme.primaryLight
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var fontRepository: FontRepository
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            val isDarkTheme = isSystemInDarkTheme()
            val navBarColor = if (isDarkTheme) primaryDark else primaryLight
            window.navigationBarColor = navBarColor.toArgb()
            WindowInsetsControllerCompat(window, window.decorView).isAppearanceLightNavigationBars =
                true
            AppNavHost(fontRepository)
        }
    }
}