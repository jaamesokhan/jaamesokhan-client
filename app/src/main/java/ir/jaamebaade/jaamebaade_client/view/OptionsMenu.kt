package ir.jaamebaade.jaamebaade_client.view

import android.content.Intent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import ir.jaamebaade.jaamebaade_client.R
import ir.jaamebaade.jaamebaade_client.constants.AppRoutes
import ir.jaamebaade.jaamebaade_client.ui.theme.neutralN95
import ir.jaamebaade.jaamebaade_client.view.components.setting.MenuItem
import ir.jaamebaade.jaamebaade_client.viewmodel.MyPoetsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OptionsMenu(
    sheetState: SheetState,
    onDismiss: () -> Unit,
    navController: NavController,
    myPoetsViewModel: MyPoetsViewModel,
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = MaterialTheme.colorScheme.neutralN95,
    ) {
        val context = LocalContext.current
        Column {
            MenuItem(
                title = stringResource(R.string.ADD_NEW_POET),
                showDivider = true,
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.AddCircleOutline,
                        contentDescription = stringResource(R.string.ADD_NEW_POET),
                        tint = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier.size(32.dp)
                    )
                },
                onClick = {
                    navController.navigate(AppRoutes.DOWNLOADABLE_POETS_SCREEN.toString())
                    onDismiss()
                }
            )

            MenuItem(
                title = stringResource(R.string.RANDOM_POEM),
                showDivider = true,
                leadingIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.book_ribbon),
                        contentDescription = stringResource(R.string.RANDOM_POEM),
                        tint = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier.size(32.dp)
                    )
                },
                onClick = {
                    myPoetsViewModel.getRandomPoem(refresh = true) {
                        myPoetsViewModel.randomPoemPreview?.let { preview ->
                            if (myPoetsViewModel.poets?.any { it.id == preview.poemPath.poet.id } == true) {
                                android.os.Handler(android.os.Looper.getMainLooper()).post {
                                    navController.navigate("${AppRoutes.POEM}/${preview.poemPath.poet.id}/${preview.poemPath.poem.id}/-1")
                                }
                            }
                        }
                    }
                    onDismiss()
                }
            )

            MenuItem(
                title = stringResource(R.string.SEARCH),
                showDivider = false,
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = stringResource(R.string.SEARCH),
                        tint = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier.size(32.dp)
                    )
                },
                onClick = {
                    navController.navigate(AppRoutes.SEARCH_SCREEN.toString())
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
                        modifier = Modifier.size(32.dp)
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
                        modifier = Modifier.size(32.dp)
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
                        modifier = Modifier.size(32.dp)
                    )
                },
                onClick = {
                    val sendIntent = Intent().apply {
                        action = Intent.ACTION_SEND
                        putExtra(
                            Intent.EXTRA_TEXT,
                            context.getString(R.string.INTRO_TO_FRIENDS_TEXT)
                        )
                        type = "text/plain"
                    }

                    val shareIntent = Intent.createChooser(sendIntent, null)
                    context.startActivity(shareIntent)
                }
            )


        }
        // A box which contains name-logo
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 40.dp),
            verticalArrangement = Arrangement.SpaceAround,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                painter = painterResource(id = R.drawable.name_logo),
                modifier = Modifier
                    .size(width = 150.dp, height = 50.dp)
                    .scale(scaleX = -1f, scaleY = 1f),
                tint = MaterialTheme.colorScheme.primary,
                contentDescription = "Logo"
            )

            Text(
                text = stringResource(R.string.APP_NAME),
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.primary,
            )
        }


    }
}
