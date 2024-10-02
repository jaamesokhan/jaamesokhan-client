package ir.jaamebaade.jaamebaade_client.view.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.navOptions
import com.canopas.lib.showcase.IntroShowcaseScope
import com.canopas.lib.showcase.component.ShowcaseStyle
import ir.jaamebaade.jaamebaade_client.R
import ir.jaamebaade.jaamebaade_client.constants.AppRoutes

val routeMap = mapOf(
    "downloadedPoetsScreen" to "downloadedPoetsScreen",
    "downloadablePoetsScreen" to "downloadablePoetsScreen",
    "poetCategoryScreen/{poetId}/{parentId}" to "downloadedPoetsScreen",
    "poemsListScreen/{poetId}/{categoryId}" to "downloadedPoetsScreen",
    "poem/{poetId}/{poemId}" to "downloadedPoetsScreen",
    "changeFontScreen" to "settingsScreen",
    "accountScreen" to "settingsScreen",
    "settingsScreen" to "settingsScreen",
    "searchScreen" to "searchScreen",
    "favoriteScreen" to "favoriteScreen",
)

@Composable
fun IntroShowcaseScope.Navbar(navController: NavController) {
    val currentRoute = currentRoute(navController, routeMap)
    Row(
        modifier = Modifier
            .background(color = MaterialTheme.colorScheme.primary)
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .navigationBarsPadding(),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {

        NavbarItem(
            route = AppRoutes.DOWNLOADED_POETS_SCREEN,
            currentRoute = currentRoute,
            idleIcon = Icons.Outlined.Home,
            selectedIcon = Icons.Filled.Home,
            contentDescription = "Home",
            navController = navController,
            modifier = Modifier.introShowCaseTarget(
                index = 0,
                style = ShowcaseStyle.Default.copy(
                    backgroundColor = MaterialTheme.colorScheme.primary,
                    backgroundAlpha = 0.98f,
                    targetCircleColor = MaterialTheme.colorScheme.onPrimary
                ),
                content = {
                    ButtonIntro(
                        stringResource(R.string.INTRO_HOME_TITLE),
                        stringResource(R.string.INTRO_HOME_DESC)
                    )
                }
            )
        )

        NavbarItem(
            route = AppRoutes.FAVORITE_SCREEN,
            currentRoute = currentRoute,
            idleIcon = Icons.Outlined.FavoriteBorder,
            selectedIcon = Icons.Filled.Favorite,
            contentDescription = "Favorites",
            navController = navController,
            modifier = Modifier.introShowCaseTarget(
                index = 1,
                style = ShowcaseStyle.Default.copy(
                    backgroundColor = MaterialTheme.colorScheme.primary,
                    backgroundAlpha = 0.98f,
                    targetCircleColor = MaterialTheme.colorScheme.onPrimary
                ),
                content = {
                    ButtonIntro(
                        stringResource(R.string.INTRO_LIKE_TITLE),
                        stringResource(R.string.INTRO_LIKE_DESC)
                    )
                }
            )
        )

        NavbarItem(
            route = AppRoutes.SEARCH_SCREEN,
            currentRoute = currentRoute,
            idleIcon = Icons.Outlined.Search,
            selectedIcon = Icons.Filled.Search,
            contentDescription = "Search",
            navController = navController,
            modifier = Modifier.introShowCaseTarget(
                index = 2,
                style = ShowcaseStyle.Default.copy(
                    backgroundColor = MaterialTheme.colorScheme.primary,
                    backgroundAlpha = 0.98f,
                    targetCircleColor = MaterialTheme.colorScheme.onPrimary
                ),
                content = {
                    ButtonIntro(
                        stringResource(R.string.INTRO_SEARCH_TITLE),
                        stringResource(R.string.INTRO_SEARCH_DESC)
                    )
                }
            )
        )

        NavbarItem(
            route = AppRoutes.SETTINGS_SCREEN,
            currentRoute = currentRoute,
            idleIcon = Icons.Outlined.Settings,
            selectedIcon = Icons.Filled.Settings,
            contentDescription = "Settings",
            navController = navController,
            modifier = Modifier.introShowCaseTarget(
                index = 3,
                style = ShowcaseStyle.Default.copy(
                    backgroundColor = MaterialTheme.colorScheme.primary,
                    backgroundAlpha = 0.98f,
                    targetCircleColor = MaterialTheme.colorScheme.onPrimary
                ),
                content = {
                    ButtonIntro(
                        stringResource(R.string.INTRO_SETTING_TITLE),
                        stringResource(R.string.INTRO_SETTING_DESC)
                    )
                }
            )
        )

    }
}

@Composable
fun NavbarItem(
    route: AppRoutes,
    currentRoute: String?,
    idleIcon: ImageVector,
    selectedIcon: ImageVector,
    contentDescription: String,
    navController: NavController,
    modifier: Modifier = Modifier
) {
    val isSelected = currentRoute == route.toString()
    IconButton(
        modifier = modifier,
        onClick = {
            var myInclusive = false
            if (currentRoute != route.toString()) {
                if (route == AppRoutes.DOWNLOADABLE_POETS_SCREEN)
                    myInclusive = true
                navController.navigate(route.toString(), navOptions {
                    popUpTo("downloadedPoetsScreen") {
                        inclusive = myInclusive
                    }
                })
            }

        },
    ) {
        Box(
            modifier = Modifier
                .then(
                    if (isSelected) {
                        Modifier
                            .size(40.dp)
                            .background(
                                color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.2f),
                                shape = RoundedCornerShape(8.dp)
                            )
                            .padding(2.dp)
                    } else {
                        Modifier
                    }
                )
        ) {
            Icon(
                imageVector = if (isSelected) selectedIcon else idleIcon,
                contentDescription = contentDescription,
                modifier = Modifier.size(36.dp),
                tint = MaterialTheme.colorScheme.onPrimary
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
