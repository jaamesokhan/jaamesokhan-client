package com.example.jaamebaade_client

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.jaamebaade_client.ui.theme.JaamebaadeclientTheme
import com.example.jaamebaade_client.view.DownloadablePoetsScreen
import com.example.jaamebaade_client.view.DownloadedPoetsScreen
import com.example.jaamebaade_client.view.FavoritesScreen
import com.example.jaamebaade_client.view.PoemListScreen
import com.example.jaamebaade_client.view.PoetCategoryScreen
import com.example.jaamebaade_client.view.SearchScreen
import com.example.jaamebaade_client.view.SettingsScreen
import com.example.jaamebaade_client.view.VerseScreen
import com.example.jaamebaade_client.view.components.Navbar
import com.example.jaamebaade_client.view.components.TopBar

@Composable
fun AppNavHost() {
    val navController = rememberNavController()
    JaamebaadeclientTheme {
        CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
            Scaffold(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background),
                bottomBar = { Navbar(navController = navController) },
                topBar = {
                    TopBar(
                        innerPadding = PaddingValues(16.dp, 16.dp, 16.dp, 16.dp),
                        navController = navController
                    )
                }
            ) { innerPadding ->
                NavHost(navController = navController, startDestination = "downloadedPoetsScreen") {
                    // TODO find a way for referencing the routes NOT as a String
                    composable("downloadedPoetsScreen") {
                        DownloadedPoetsScreen(
                            modifier = Modifier.padding(
                                innerPadding
                            ), navController = navController
                        )
                    }
                    composable("downloadablePoetsScreen") {
                        DownloadablePoetsScreen(modifier = Modifier.padding(innerPadding))
                    }
                    composable("poetCategoryScreen/{poetId}/{parentId}") { backStackEntry ->
                        val poetId = backStackEntry.arguments?.getString("poetId")?.toInt()
                        val parentId = backStackEntry.arguments?.getString("parentId")?.toInt()
                        PoetCategoryScreen(
                            modifier = Modifier.padding(innerPadding),
                            poetId = poetId!!,
                            parentId = parentId ?: 0,
                            navController = navController
                        )
                    }
                    composable("settingsScreen") {
                        SettingsScreen(
                            modifier = Modifier.padding(innerPadding),
                            navController = navController
                        )
                    }
                    composable("searchScreen") {
                        SearchScreen(
                            modifier = Modifier.padding(innerPadding),
                            navController = navController
                        )
                    }
                    composable("favoriteScreen") {
                        FavoritesScreen(
                            modifier = Modifier.padding(innerPadding),
                            navController = navController
                        )
                    }
                    composable("poemsListScreen/{poetId}/{categoryId}") { backStackEntry ->
                        val categoryId = backStackEntry.arguments?.getString("categoryId")?.toInt()
                        val poetId = backStackEntry.arguments?.getString("poetId")?.toInt()

                        PoemListScreen(
                            categoryId = categoryId!!,
                            poetId = poetId!!,
                            modifier = Modifier.padding(innerPadding),
                            navController
                        )

                    }
                    composable("poem/{poetId}/{poemId}") { backStackEntry ->
                        val poemId = backStackEntry.arguments?.getString("poemId")?.toInt()
                        val poetId = backStackEntry.arguments?.getString("poetId")?.toInt()

                        VerseScreen(
                            poemId = poemId!!,
                            poetId = poetId!!,
                            modifier = Modifier.padding(innerPadding),
                        )
                    }
                }
            }
        }
    }
}
