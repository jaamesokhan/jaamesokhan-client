package com.example.jaamebaade_client.view

import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Create
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.jaamebaade_client.view.components.SettingsItem
import com.example.jaamebaade_client.viewmodel.SettingsViewModel

@Composable
fun SettingsScreen(
    modifier: Modifier, navController: NavController,
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val username by viewModel.username

    Column(modifier = modifier) {
        SettingsItem(
            modifier = Modifier,
            text = username ?: "حساب کاربری",
            icon = Icons.Default.AccountCircle
        ) {
            if (username == null) {
                navController.navigate("accountScreen")
            }
        }
        if (username != null) {
            SettingsItem(
                modifier = Modifier,
                text = "خروج از حساب کاربری",
                icon = Icons.Default.Close,
            ) {
                viewModel.logout()

            }
        }
        SettingsItem(
            modifier = Modifier,
            text = "دانلود شاعر جدید",
            icon = Icons.Default.AddCircle
        ) {
            navController.navigate("downloadablePoetsScreen")
        }

        SettingsItem(
            modifier = Modifier,
            text = "تنظیمات فونت",
            icon = Icons.Default.Create
        ) {
            navController.navigate("changeFontScreen")
        }
    }

}