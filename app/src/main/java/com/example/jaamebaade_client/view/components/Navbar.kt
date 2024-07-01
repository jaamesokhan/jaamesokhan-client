package com.example.jaamebaade_client.view.components

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
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
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        NavbarItem(
            route = "downloadedPoetsScreen",
            currentRoute = currentRoute,
            icon = Icons.Filled.Home,
            contentDescription = "Home",
            navController = navController
        )

        NavbarItem(
            route = "favoriteScreen",
            currentRoute = currentRoute,
            icon = Icons.Filled.Favorite,
            contentDescription = "Favorites",
            navController = navController
        )

        NavbarItem(
            route = "searchScreen",
            currentRoute = currentRoute,
            icon = Icons.Filled.Search,
            contentDescription = "Search",
            navController = navController
        )

        NavbarItem(
            route = "settingsScreen",
            currentRoute = currentRoute,
            icon = Icons.Filled.Settings,
            contentDescription = "Settings",
            navController = navController
        )
    }
}

@Composable
fun NavbarItem(
    route: String,
    currentRoute: String?,
    icon: ImageVector,
    contentDescription: String,
    navController: NavController
) {
    val isSelected = currentRoute == route
    IconButton(
        onClick = { navController.navigate(route, navOptions {
            popUpTo("downloadedPoetsScreen") {
                inclusive = false
            }
        }) },
        modifier = Modifier
    ) {
        Icon(
            icon,
            contentDescription = contentDescription,
            modifier = Modifier.size(36.dp),
            tint = if (isSelected) MaterialTheme.colorScheme.onPrimary else LocalContentColor.current
        )
    }
}



@Composable
fun currentRoute(navController: NavController, routeMap: Map<String, String>): String? {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    Log.d("curr", "$currentRoute")
    return routeMap[currentRoute]
}
