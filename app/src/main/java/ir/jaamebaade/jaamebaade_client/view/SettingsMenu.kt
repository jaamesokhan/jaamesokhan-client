package ir.jaamebaade.jaamebaade_client.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircleOutline
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.PersonOutline
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import ir.jaamebaade.jaamebaade_client.R
import ir.jaamebaade.jaamebaade_client.constants.AppRoutes
import ir.jaamebaade.jaamebaade_client.ui.theme.neutralN95
import ir.jaamebaade.jaamebaade_client.view.components.setting.MenuItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsMenu(
    sheetState: SheetState,
    onDismiss: () -> Unit,
    navController: NavController
) {
    rememberCoroutineScope()
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = MaterialTheme.colorScheme.neutralN95,
    ) {
        Column {
            MenuItem(
                title = stringResource(R.string.RANDOM_POEM),
                showDivider = true,
                leadingIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.book_ribbon),
                        contentDescription = stringResource(R.string.RANDOM_POEM),
                        tint = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier.size(24.dp)
                    )
                },
                onClick = { //TODO
                }
            )

            MenuItem(
                title = stringResource(R.string.SEARCH),
                showDivider = true,
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = stringResource(R.string.SEARCH),
                        tint = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier.size(24.dp)
                    )
                },
                onClick = {
                    navController.navigate(AppRoutes.SEARCH_SCREEN.toString())
                    onDismiss()
                }
            )

            MenuItem(
                title = stringResource(R.string.ADD_NEW_POET),
                showDivider = false,
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.AddCircleOutline,
                        contentDescription = stringResource(R.string.RANDOM_POEM),
                        tint = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier.size(24.dp)
                    )
                },
                onClick = {
                    navController.navigate(AppRoutes.DOWNLOADABLE_POETS_SCREEN.toString())
                    onDismiss()
                }
            )


            HorizontalDivider(color = MaterialTheme.colorScheme.outline)

            MenuItem(
                title = stringResource(R.string.SETTINGS),
                showDivider = true,
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Outlined.Settings,
                        contentDescription = stringResource(R.string.SETTINGS),
                        tint = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier.size(24.dp)
                    )
                },
                onClick = {
                    navController.navigate(AppRoutes.SETTINGS_SCREEN.toString())
                    onDismiss()
                }
            )
            MenuItem(
                title = stringResource(R.string.ABOUT_US),
                showDivider = true,
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Outlined.PersonOutline,
                        contentDescription = stringResource(R.string.RANDOM_POEM),
                        tint = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier.size(24.dp)
                    )
                },
                onClick = {
                    navController.navigate(AppRoutes.ABOUT_US_SCREEN.toString())
                    onDismiss()
                }
            )
            MenuItem(
                title = stringResource(R.string.INTRO_TO_FRIEND),
                showDivider = false,
                leadingIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.intro_to_friends),
                        contentDescription = stringResource(R.string.INTRO_TO_FRIEND),
                        tint = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier.size(24.dp)
                    )
                },
                onClick = {
                    //TODO
                }
            )


        }


    }
}


