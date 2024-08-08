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
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.navOptions

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
fun Navbar(navController: NavController) {
    val currentRoute = currentRoute(navController, routeMap)

    Row(
        modifier = Modifier
            .background(color = MaterialTheme.colorScheme.primary)
            .fillMaxWidth()
            .padding(4.dp)
            .navigationBarsPadding(),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        NavbarItem(
            route = "downloadedPoetsScreen",
            currentRoute = currentRoute,
            idleIcon = Icons.Outlined.Home,
            selectedIcon = Icons.Filled.Home,
            contentDescription = "Home",
            navController = navController
        )

        NavbarItem(
            route = "favoriteScreen",
            currentRoute = currentRoute,
            idleIcon = Icons.Outlined.FavoriteBorder,
            selectedIcon = Icons.Filled.Favorite,
            contentDescription = "Favorites",
            navController = navController
        )

        NavbarItem(
            route = "searchScreen",
            currentRoute = currentRoute,
            idleIcon = Icons.Outlined.Search,
            selectedIcon = Icons.Filled.Search,
            contentDescription = "Search",
            navController = navController
        )

        NavbarItem(
            route = "settingsScreen",
            currentRoute = currentRoute,
            idleIcon = Icons.Outlined.Settings,
            selectedIcon = Icons.Filled.Settings,
            contentDescription = "Settings",
            navController = navController
        )
    }
}

@Composable
fun NavbarItem(
    route: String,
    currentRoute: String?,
    idleIcon: ImageVector,
    selectedIcon: ImageVector,
    contentDescription: String,
    navController: NavController
) {
    val isSelected = currentRoute == route
    IconButton(
        onClick = {
            var myInclusive = false
            if (currentRoute != route) {
                if (route == "downloadedPoetsScreen")
                    myInclusive = true
                navController.navigate(route, navOptions {
                    popUpTo("downloadedPoetsScreen") {
                        inclusive = myInclusive
                    }
                })
            }

        },
        modifier = Modifier
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
                            .padding(4.dp) // Adjust padding if needed
                    } else {
                        Modifier
                    }
                )
        ) {
            Icon(
                imageVector = if (isSelected) selectedIcon else idleIcon,
                contentDescription = contentDescription,
                modifier = Modifier.size(36.dp),
                tint = MaterialTheme.colorScheme.inversePrimary
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