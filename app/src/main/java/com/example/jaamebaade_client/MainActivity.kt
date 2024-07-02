package com.example.jaamebaade_client

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.example.jaamebaade_client.repository.FontRepository
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

        // Set the navigation bar color
        window.navigationBarColor = android.graphics.Color.BLACK// Replace with your desired color
        WindowInsetsControllerCompat(window, window.decorView).isAppearanceLightNavigationBars = true

        setContent {
            AppNavHost(fontRepository)
        }
    }
}