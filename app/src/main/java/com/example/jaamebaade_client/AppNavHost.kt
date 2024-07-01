package com.example.jaamebaade_client

import android.util.Log
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.dialog
import androidx.navigation.compose.rememberNavController
import com.example.jaamebaade_client.repository.FontRepository
import com.example.jaamebaade_client.ui.theme.FONT_SIZE_LIST
import com.example.jaamebaade_client.ui.theme.JaamebaadeclientTheme
import com.example.jaamebaade_client.ui.theme.getFontByFontFamilyName
import com.example.jaamebaade_client.view.AccountScreen
import com.example.jaamebaade_client.view.ChangeFontScreen
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
fun AppNavHost(fontRepository: FontRepository) {
    val navController =
        rememberNavController() // TODO explore the possibility of using a single instance of NavController
    val fontSize by fontRepository.fontSize.collectAsState()
    val fontFamily by fontRepository.fontFamily.collectAsState()

    fun createTextStyle(type: String): TextStyle {
        val selectedFontFamily = getFontByFontFamilyName(fontFamily)
        val selectedSize = FONT_SIZE_LIST[fontSize] ?: 16
        Log.d("size", "$fontSize")
        return when (type) {
            "small" -> TextStyle(
                fontFamily = selectedFontFamily, fontSize = (selectedSize - 5).sp
            )

            "large" -> TextStyle(
                fontFamily = selectedFontFamily, fontSize = (selectedSize + 5).sp
            )

            else -> TextStyle(
                fontFamily = selectedFontFamily, fontSize = (selectedSize).sp
            )
        }
    }

    val typography = remember {
        Typography(
            bodyLarge = createTextStyle("large"),
            bodyMedium = createTextStyle("medium"),
            bodySmall = createTextStyle("small"),
            headlineLarge = createTextStyle("large"),
            headlineMedium = createTextStyle("medium"),
            headlineSmall = createTextStyle("small"),
            titleLarge = createTextStyle("large"),
            titleMedium = createTextStyle("medium"),
            titleSmall = createTextStyle("small"),
            labelLarge = createTextStyle("large"),
            labelMedium = createTextStyle("medium"),
            labelSmall = createTextStyle("small"),
            // Define other text styles as needed
        )
    }
    JaamebaadeclientTheme(typography = typography) {
        CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
            Scaffold(modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
                bottomBar = { Navbar(navController = navController) },
                topBar = {
                    TopBar(
                        navController = navController
                    )
                }) { innerPadding ->
                NavHost(navController = navController, startDestination = "downloadedPoetsScreen") {
                    // TODO find a way for referencing the routes NOT as a String
                    composable(route = "downloadedPoetsScreen",
                        enterTransition = {
                            slideIntoContainer(
                                AnimatedContentTransitionScope.SlideDirection.Left,
                                animationSpec = tween(700)
                            )
                        }, exitTransition = {
                            slideOutOfContainer(
                                AnimatedContentTransitionScope.SlideDirection.Right,
                                animationSpec = tween(700)
                            )
                        }) {
                        DownloadedPoetsScreen(
                            modifier = Modifier.padding(
                                innerPadding
                            ), navController = navController
                        )
                    }
                    composable(route = "downloadablePoetsScreen",
                        enterTransition = {
                            slideIntoContainer(
                                AnimatedContentTransitionScope.SlideDirection.Left,
                                animationSpec = tween(700)
                            )
                        }, exitTransition = {
                            slideOutOfContainer(
                                AnimatedContentTransitionScope.SlideDirection.Right,
                                animationSpec = tween(700)
                            )
                        }) {
                        DownloadablePoetsScreen(modifier = Modifier.padding(innerPadding))
                    }
                    composable(route = "poetCategoryScreen/{poetId}/{parentId}",
                        enterTransition = {
                            slideIntoContainer(
                                AnimatedContentTransitionScope.SlideDirection.Left,
                                animationSpec = tween(700)
                            )
                        },
                        exitTransition = {
                            slideOutOfContainer(
                                AnimatedContentTransitionScope.SlideDirection.Right,
                                animationSpec = tween(700)
                            )
                        }
                    ) { backStackEntry ->
                        val poetId = backStackEntry.arguments?.getString("poetId")?.toInt()
                        val parentId = backStackEntry.arguments?.getString("parentId")?.toInt()
                        PoetCategoryScreen(
                            modifier = Modifier.padding(innerPadding),
                            poetId = poetId!!,
                            parentId = parentId ?: 0,
                            navController = navController
                        )
                    }
                    composable("settingsScreen",
                        enterTransition = {
                            slideIntoContainer(
                                AnimatedContentTransitionScope.SlideDirection.Left,
                                animationSpec = tween(700)
                            )
                        },
                        exitTransition = {
                            slideOutOfContainer(
                                AnimatedContentTransitionScope.SlideDirection.Right,
                                animationSpec = tween(700)
                            )
                        }
                    ) {
                        SettingsScreen(
                            modifier = Modifier.padding(innerPadding), navController = navController
                        )
                    }
                    composable(route = "searchScreen",
                        enterTransition = {
                            slideIntoContainer(
                                AnimatedContentTransitionScope.SlideDirection.Left,
                                animationSpec = tween(700)
                            )
                        },
                        exitTransition = {
                            slideOutOfContainer(
                                AnimatedContentTransitionScope.SlideDirection.Right,
                                animationSpec = tween(700)
                            )
                        }
                    ) {
                        SearchScreen(
                            modifier = Modifier.padding(innerPadding), navController = navController
                        )
                    }
                    composable("favoriteScreen") {
                        FavoritesScreen(
                            modifier = Modifier.padding(innerPadding), navController = navController
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
                            navController,
                            poemId = poemId!!,
                            poetId = poetId!!,
                            modifier = Modifier.padding(innerPadding),
                        )
                    }
                    composable("changeFontScreen") {
                        ChangeFontScreen(
                            modifier = Modifier.padding(innerPadding),
                            fontRepository = fontRepository
                        )
                    }
                    dialog("accountScreen") {
                        AccountScreen(navController = navController)
                    }
                }
            }
        }
    }
}

