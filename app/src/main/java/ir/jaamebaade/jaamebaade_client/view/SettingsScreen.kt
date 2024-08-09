package ir.jaamebaade.jaamebaade_client.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ColorLens
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.People
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import ir.jaamebaade.jaamebaade_client.constants.AppRoutes
import ir.jaamebaade.jaamebaade_client.view.components.SettingsItem

@Composable
fun SettingsScreen(
    modifier: Modifier,
    navController: NavController,
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
            text = "تنظیمات فونت",
            icon = Icons.Default.Create
        ) {
            navController.navigate(AppRoutes.CHANGE_FONT_SCREEN.toString())
        }


        SettingsItem(text = "تغییر پوسته", icon = Icons.Filled.ColorLens) {
            navController.navigate(AppRoutes.CHANGE_THEME_SCREEN.toString())
        }

        SettingsItem(text = "درباره ما", icon = Icons.Default.People) {
            navController.navigate(AppRoutes.ABOUT_US_SCREEN.toString())
        }

    }

}