package com.example.jaamebaade_client

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.jaamebaade_client.ui.theme.JaamebaadeclientTheme
import com.example.jaamebaade_client.view.DownloadablePoetsScreen
import com.example.jaamebaade_client.view.DownloadedPoetsScreen
import com.example.jaamebaade_client.view.components.Navbar
import com.example.jaamebaade_client.view.components.TopBar

@Composable
fun AppNavHost() {
    val navController = rememberNavController()
    JaamebaadeclientTheme {
        CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
            Scaffold(
                modifier = Modifier.fillMaxSize(),
                bottomBar = { Navbar(navController = navController) },
                topBar = { TopBar(innerPadding = PaddingValues(16.dp, 16.dp, 16.dp, 16.dp)) }

            ) { innerPadding ->
                NavHost(navController = navController, startDestination = "downloadedPoetsScreen") {
                    // TODO find a way for referencing the routes NOT as a String
                    composable("downloadedPoetsScreen") {
                        DownloadedPoetsScreen(
                            modifier = Modifier.padding(
                                innerPadding
                            ), navController = navController
                        )
                    }
                    composable("downloadablePoetsScreen") {
                        DownloadablePoetsScreen(modifier = Modifier.padding(innerPadding))
                    }
                }
            }
        }
    }
}
