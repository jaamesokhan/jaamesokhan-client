package com.example.jaamebaade_client.view

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
import com.example.jaamebaade_client.ui.theme.JaamebaadeclientTheme
import com.example.jaamebaade_client.view.components.Navbar
import com.example.jaamebaade_client.view.components.TopBar

@Composable
fun MainScreen() {
    JaamebaadeclientTheme {
        CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
            Scaffold(
                modifier = Modifier.fillMaxSize(),
                bottomBar = { Navbar() },
                topBar = { TopBar(innerPadding = PaddingValues(16.dp, 16.dp, 16.dp, 16.dp)) }

            ) { innerPadding ->
                // TODO go to downloadedPoetsScreen
//                DownloadablePoetsScreen(modifier = Modifier.padding(innerPadding))
                DownloadedPoetsScreen(modifier = Modifier.padding(innerPadding))
            }
        }
    }
}
