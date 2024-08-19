package ir.jaamebaade.jaamebaade_client.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ColorLens
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.People
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
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

    val settingsItems = listOf(
        SettingsItemData(
            text = "تنظیمات فونت",
            icon = Icons.Default.Create,
            onClick = { navController.navigate(AppRoutes.CHANGE_FONT_SCREEN.toString()) }
        ),
        SettingsItemData(
            text = "تغییر پوسته",
            icon = Icons.Filled.ColorLens,
            onClick = { navController.navigate(AppRoutes.CHANGE_THEME_SCREEN.toString()) }
        ),
        SettingsItemData(
            text = "درباره ما",
            icon = Icons.Default.People,
            onClick = { navController.navigate(AppRoutes.ABOUT_US_SCREEN.toString()) }
        )
    )

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

        settingsItems.forEachIndexed { index, item ->
            SettingsItem(
                text = item.text,
                icon = item.icon
            ) {
                item.onClick()
            }

            if (index < settingsItems.size - 1) {
                HorizontalDivider()
            }
        }

    }

}

data class SettingsItemData(
    val text: String,
    val icon: ImageVector,
    val onClick: () -> Unit
)