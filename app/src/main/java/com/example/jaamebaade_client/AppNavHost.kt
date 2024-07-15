package com.example.jaamebaade_client

import android.util.Log
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.dialog
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.jaamebaade_client.constants.AppRoutes
import com.example.jaamebaade_client.repository.FontRepository
import com.example.jaamebaade_client.ui.theme.FONT_SIZE_LIST
import com.example.jaamebaade_client.ui.theme.JaamebaadeclientTheme
import com.example.jaamebaade_client.ui.theme.getFontByFontFamilyName
import com.example.jaamebaade_client.utility.toIntArray
import com.example.jaamebaade_client.view.AccountScreen
import com.example.jaamebaade_client.view.ChangeFontScreen
import com.example.jaamebaade_client.view.CommentsScreen
import com.example.jaamebaade_client.view.DownloadablePoetsScreen
import com.example.jaamebaade_client.view.DownloadedPoetsScreen
import com.example.jaamebaade_client.view.FavoritesScreen
import com.example.jaamebaade_client.view.PoetCategoryPoemScreen
import com.example.jaamebaade_client.view.SearchScreen
import com.example.jaamebaade_client.view.SettingsScreen
import com.example.jaamebaade_client.view.VerseScreen
import com.example.jaamebaade_client.view.components.Navbar
import com.example.jaamebaade_client.view.components.TopBar

@Composable
fun AppNavHost(fontRepository: FontRepository) {
    val navController =
        rememberNavController()
    val fontSize by fontRepository.fontSize.collectAsState()
    val fontFamily by fontRepository.fontFamily.collectAsState()

    fun createTextStyle(size: String, type: String): TextStyle {
        val selectedFontFamily = getFontByFontFamilyName(fontFamily)
        val selectedSize = FONT_SIZE_LIST[fontSize] ?: 16
        val sizeBasedFontSize: TextUnit = when (size) {
            "small" -> (selectedSize - 3.5).sp

            "large" -> (selectedSize + 3.5).sp

            else ->
                (selectedSize).sp
        }

        val typeBasedFontWeight = when (type) {
            "headline" -> FontWeight.Medium
            "title" -> FontWeight.Bold
            "label" -> FontWeight.Light
            else -> FontWeight.Normal
        }
        Log.d("size", "$sizeBasedFontSize")
        return TextStyle(
            fontFamily = selectedFontFamily,
            fontSize = sizeBasedFontSize,
            fontWeight = typeBasedFontWeight
        )
    }

    val typography = remember {
        Typography(
            bodyLarge = createTextStyle("large", "body"),
            bodyMedium = createTextStyle("medium", "body"),
            bodySmall = createTextStyle("small", "body"),
            headlineLarge = createTextStyle("large", "headline"),
            headlineMedium = createTextStyle("medium", "headline"),
            headlineSmall = createTextStyle("small", "headline"),
            titleLarge = createTextStyle("large", "title"),
            titleMedium = createTextStyle("medium", "title"),
            titleSmall = createTextStyle("small", "title"),
            labelLarge = createTextStyle("large", "label"),
            labelMedium = createTextStyle("medium", "label"),
            labelSmall = createTextStyle("small", "label"),

            // Define other text styles as needed
        )
    }
    JaamebaadeclientTheme(typography = typography) {
        CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
            Scaffold(modifier = Modifier
                .fillMaxSize()
                .systemBarsPadding()
                .imePadding()
                .background(MaterialTheme.colorScheme.background),
                bottomBar = { Navbar(navController = navController) },
                topBar = {
                    TopBar(
                        navController = navController
                    )
                }) { innerPadding ->
                NavHost(navController = navController, startDestination = "downloadedPoetsScreen") {
                    composable(route = AppRoutes.DOWNLOADED_POETS_SCREEN.toString(),
                        enterTransition = {
                            slideIntoContainer(
                                AnimatedContentTransitionScope.SlideDirection.Left,
                                animationSpec = tween(400)
                            )
                        }, exitTransition = {
                            slideOutOfContainer(
                                AnimatedContentTransitionScope.SlideDirection.Right,
                                animationSpec = tween(400)
                            )
                        }) {
                        DownloadedPoetsScreen(
                            modifier = Modifier.padding(
                                innerPadding
                            ), navController = navController
                        )
                    }
                    composable(route = AppRoutes.DOWNLOADABLE_POETS_SCREEN.toString(),
                        enterTransition = {
                            slideIntoContainer(
                                AnimatedContentTransitionScope.SlideDirection.Left,
                                animationSpec = tween(400)
                            )
                        }, exitTransition = {
                            slideOutOfContainer(
                                AnimatedContentTransitionScope.SlideDirection.Right,
                                animationSpec = tween(400)
                            )
                        }) {
                        DownloadablePoetsScreen(modifier = Modifier.padding(innerPadding))
                    }
                    composable(route = "${AppRoutes.POET_CATEGORY_SCREEN}/{poetId}/{parentIds}",
                        arguments = listOf(
                            navArgument("poetId") { type = NavType.IntType },
                            navArgument("parentIds") { type = NavType.StringType }
                        ),
                        enterTransition = {
                            slideIntoContainer(
                                AnimatedContentTransitionScope.SlideDirection.Left,
                                animationSpec = tween(400)
                            )
                        },
                        exitTransition = {
                            slideOutOfContainer(
                                AnimatedContentTransitionScope.SlideDirection.Right,
                                animationSpec = tween(400)
                            )
                        }
                    ) { backStackEntry ->
                        val poetId = backStackEntry.arguments?.getInt("poetId")
                        val parentIds =
                            backStackEntry.arguments?.getString("parentIds")?.toIntArray()
                        PoetCategoryPoemScreen(
                            modifier = Modifier.padding(innerPadding),
                            poetId = poetId!!,
                            parentIds = parentIds ?: intArrayOf(),
                            navController = navController
                        )
                    }
                    composable(AppRoutes.SETTINGS_SCREEN.toString(),
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
                    composable(route = AppRoutes.SEARCH_SCREEN.toString(),
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
                    composable(AppRoutes.FAVORITE_SCREEN.toString()) {
                        FavoritesScreen(
                            modifier = Modifier.padding(innerPadding), navController = navController
                        )
                    }
                    composable(
                        "${AppRoutes.POEM}/{poetId}/{poemId}",
                        arguments = listOf(
                            navArgument("poetId") { type = NavType.IntType },
                            // TODO add parentIds here (or at least the last of them)
                            navArgument("poemId") { type = NavType.IntType },
                        )
                    ) { backStackEntry ->
                        val poetId = backStackEntry.arguments?.getInt("poetId")
                        val poemId = backStackEntry.arguments?.getInt("poemId")

                        VerseScreen(
                            navController,
                            poemId = poemId!!,
                            poetId = poetId!!,
                            modifier = Modifier.padding(innerPadding),
                        )
                    }
                    composable(
                        "${AppRoutes.COMMENTS}/{poetId}/{poemId}",
                        arguments = listOf(
                            navArgument("poetId") { type = NavType.IntType },
                            navArgument("poemId") { type = NavType.IntType },
                        )
                    ) { backStackEntry ->
                        val poemId = backStackEntry.arguments?.getInt("poemId")

                        CommentsScreen(
                            poemId = poemId!!,
                            modifier = Modifier.padding(innerPadding),
                        )
                    }
                    composable(AppRoutes.CHANGE_FONT_SCREEN.toString()) {
                        ChangeFontScreen(
                            modifier = Modifier.padding(innerPadding),
                            fontRepository = fontRepository
                        )
                    }
                    dialog(AppRoutes.ACCOUNT_SCREEN.toString()) {
                        AccountScreen(navController = navController)
                    }
                }
            }
        }
    }
}

