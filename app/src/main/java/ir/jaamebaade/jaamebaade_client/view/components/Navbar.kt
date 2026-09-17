package ir.jaamebaade.jaamebaade_client.view.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.navOptions
import ir.jaamebaade.jaamebaade_client.R
import ir.jaamebaade.jaamebaade_client.constants.AppRoutes
import ir.jaamebaade.jaamebaade_client.ui.theme.Dimens
import ir.jaamebaade.jaamebaade_client.ui.theme.PillShape
import ir.jaamebaade.jaamebaade_client.ui.theme.neutralN70
import ir.jaamebaade.jaamebaade_client.view.components.toast.ToastMessage
import ir.jaamebaade.jaamebaade_client.viewmodel.ToastManager

val routeMap = mapOf(
    "downloadedPoetsScreen" to "downloadedPoetsScreen",
    "downloadablePoetsScreen" to "downloadablePoetsScreen",
    "changeFontScreen" to "settingsScreen",
    "settingsScreen" to "settingsScreen",
    "searchScreen" to "searchScreen",
    "favoriteScreen" to "favoriteScreen",
    "bookmarkCategoriesScreen/{tab}" to "bookmarkCategoriesScreen",
    "notesScreen" to "notesScreen",
)

@Immutable
data class NavbarItemData(
    val route: AppRoutes,
    private val idleIconId: Int,
    private val selectedIconId: Int,
    val contentDescriptionResId: Int,
    val tabArg: String? = null,
) {
    fun getIcon(isSelected: Boolean): Int {
        return if (isSelected) selectedIconId else idleIconId
    }

    fun targetRoute(): String = tabArg?.let { "$route/$it" } ?: route.toString()
}


// TODO change intro texts accordingly
val navbarItems = listOf(
    NavbarItemData(
        route = AppRoutes.DOWNLOADED_POETS_SCREEN,
        idleIconId = R.drawable.my_poets,
        selectedIconId = R.drawable.my_poets_selected,
        contentDescriptionResId = R.string.MY_POETS_TITLE,
    ),
    NavbarItemData(
        route = AppRoutes.BOOKMARK_CATEGORIES_SCREEN,
        idleIconId = R.drawable.bookmark,
        selectedIconId = R.drawable.bookmark_selected,
        contentDescriptionResId = R.string.BOOKMARK_TITLE,
        tabArg = "save",
    ),
    NavbarItemData(
        route = AppRoutes.BOOKMARK_CATEGORIES_SCREEN,
        idleIconId = R.drawable.highlight,
        selectedIconId = R.drawable.highlight_selected,
        contentDescriptionResId = R.string.HIGHLIGHT_TITLE,
        tabArg = "hi",
    ),
    NavbarItemData(
        route = AppRoutes.NOTES_SCREEN,
        idleIconId = R.drawable.note,
        selectedIconId = R.drawable.note_selected,
        contentDescriptionResId = R.string.NOTE_TITLE,
    )
)

@Composable
fun Navbar(navController: NavController) {
    val currentRoute = currentRoute(navController, routeMap)
    // TODO : this may not be the best practice to handle this kind of situation
    if (currentRoute in navbarItems.map { it.route.toString() }) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentTabArg = navBackStackEntry?.arguments?.getString("tab")
        val showMessage by ToastManager.showMessage.collectAsState()

        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = Dimens.screenGutter)
                    .padding(bottom = Dimens.space16)
                    .clip(MaterialTheme.shapes.extraLarge)
                    .navigationBarsPadding(),
            ) {
                Surface(
                    shadowElevation = 20.dp,
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(88.dp)
                            .background(color = MaterialTheme.colorScheme.surface)
                            .padding(horizontal = Dimens.space8),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        navbarItems.forEachIndexed { _, item ->
                            val isSelected = if (item.tabArg != null) {
                                currentRoute == item.route.toString() && currentTabArg == item.tabArg
                            } else {
                                currentRoute == item.route.toString()
                            }
                            NavbarItem(
                                targetRoute = item.targetRoute(),
                                iconId = item.getIcon(isSelected = isSelected),
                                contentDescription = stringResource(item.contentDescriptionResId),
                                isSelected = isSelected,
                                navController = navController,
                                modifier = Modifier,
                            )
                        }
                    }
                }
            }

            if (showMessage) {
                ToastMessage()
            }
        }
    }

}

@Composable
fun NavbarItem(
    targetRoute: String,
    iconId: Int,
    contentDescription: String,
    isSelected: Boolean,
    navController: NavController,
    modifier: Modifier = Modifier
) {
    Button(
        modifier = modifier,
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent,
        ),
        contentPadding = PaddingValues(0.dp),
        shape = RectangleShape,
        onClick = {
            if (!isSelected) {
                navController.navigate(targetRoute, navOptions {
                    popUpTo(AppRoutes.DOWNLOADABLE_POETS_SCREEN.toString()) {
                        inclusive = false
                    }
                })
            }

        },
    ) {
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .wrapContentWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .clip(PillShape)
                    .background(
                        if (isSelected) MaterialTheme.colorScheme.secondaryContainer else Color.Transparent
                    )
                    .padding(horizontal = 20.dp, vertical = 4.dp),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = iconId),
                    contentDescription = contentDescription,
                    modifier = Modifier.size(28.dp),
                    tint = if (isSelected) MaterialTheme.colorScheme.onSecondaryContainer else MaterialTheme.colorScheme.neutralN70
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = contentDescription,
                style = MaterialTheme.typography.labelSmall,
                color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.neutralN70
            )

        }
    }
}


@Composable
fun currentRoute(navController: NavController, routeMap: Map<String, String>): String? {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    return routeMap[currentRoute]
}
