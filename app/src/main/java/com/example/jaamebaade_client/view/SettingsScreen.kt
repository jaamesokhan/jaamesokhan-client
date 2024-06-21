package com.example.jaamebaade_client.view

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.jaamebaade_client.view.components.SettingsItem

@Composable
fun SettingsScreen(modifier: Modifier, navController: NavController) {
    Column(modifier = modifier) {
        SettingsItem(modifier = Modifier, text = "دانلود شاعر جدید") {
            navController.navigate("downloadablePoetsScreen")
        }
    }

}