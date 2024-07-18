package com.example.jaamebaade_client.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.jaamebaade_client.view.components.SettingsItem

@Composable
fun SettingsScreen(
    modifier: Modifier, navController: NavController,
//    viewModel: SettingsViewModel = hiltViewModel()
) {
//    val username by viewModel.username

    Column(modifier = modifier.padding(8.dp)) {
//        SettingsItem(
//            modifier = Modifier,
//            text = username ?: "حساب کاربری",
//            icon = Icons.Default.AccountCircle
//        ) {
//            if (username == null) {
//                navController.navigate("accountScreen")
//            }
//        }
//        if (username != null) {
//            SettingsItem(
//                modifier = Modifier,
//                text = "خروج از حساب کاربری",
//                icon = Icons.Default.Close,
//            ) {
//                viewModel.logout()
//
//            }
//        }
        SettingsItem(
            modifier = Modifier.padding(4.dp),
            text = "تنظیمات فونت",
            icon = Icons.Default.Create
        ) {
            navController.navigate("changeFontScreen")
        }
    }

}