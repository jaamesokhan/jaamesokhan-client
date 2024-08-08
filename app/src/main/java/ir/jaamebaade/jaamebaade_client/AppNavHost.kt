package ir.jaamebaade.jaamebaade_client

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.dialog
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import ir.jaamebaade.jaamebaade_client.constants.AppRoutes
import ir.jaamebaade.jaamebaade_client.repository.FontRepository
import ir.jaamebaade.jaamebaade_client.repository.ThemeRepository
import ir.jaamebaade.jaamebaade_client.ui.theme.AppThemeType
import ir.jaamebaade.jaamebaade_client.ui.theme.JaamebaadeclientTheme
import ir.jaamebaade.jaamebaade_client.utility.toIntArray
import ir.jaamebaade.jaamebaade_client.view.AccountScreen
import ir.jaamebaade.jaamebaade_client.view.ChangeFontScreen
import ir.jaamebaade.jaamebaade_client.view.ChangeThemeScreen
import ir.jaamebaade.jaamebaade_client.view.CommentsScreen
import ir.jaamebaade.jaamebaade_client.view.DownloadablePoetsScreen
import ir.jaamebaade.jaamebaade_client.view.DownloadedPoetsScreen
import ir.jaamebaade.jaamebaade_client.view.FavoritesScreen
import ir.jaamebaade.jaamebaade_client.view.PoetCategoryPoemScreen
import ir.jaamebaade.jaamebaade_client.view.SearchScreen
import ir.jaamebaade.jaamebaade_client.view.SettingsScreen
import ir.jaamebaade.jaamebaade_client.view.VerseScreen
import ir.jaamebaade.jaamebaade_client.view.components.Navbar
import ir.jaamebaade.jaamebaade_client.view.components.TopBar
import ir.jaamebaade.jaamebaade_client.viewmodel.AudioViewModel

@Composable
fun AppNavHost(fontRepository: FontRepository, themeRepository: ThemeRepository) {
    val audioViewModel: AudioViewModel = hiltViewModel()

    val navController =
        rememberNavController()
    val fontSize by fontRepository.fontSizeIndex.collectAsState()
    val fontFamily by fontRepository.fontFamily.collectAsState()

    fun createTextStyle(size: String, type: String): TextStyle {
        val selectedFontFamily = fontFamily
        val selectedSize = fontFamily.getFontSizes()[fontSize]
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
        return TextStyle(
            fontFamily = selectedFontFamily.getFontFamily(),
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
        )
    }

    val appTheme by themeRepository.appTheme.collectAsState()

    JaamebaadeclientTheme(
        darkTheme = when (appTheme) {
            AppThemeType.LIGHT -> false
            AppThemeType.DARK -> true
            AppThemeType.SYSTEM_AUTO -> isSystemInDarkTheme()
        }, typography = typography
    ) {
        CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
            Scaffold(modifier = Modifier
                .fillMaxSize()
                .systemBarsPadding()
                .imePadding()
                .background(MaterialTheme.colorScheme.background),
                bottomBar = { Navbar(navController = navController) },
                topBar = {
                    TopBar(
                        navController = navController,
                        audioViewModel = audioViewModel,
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
                            modifier = Modifier.padding(innerPadding),
                            navController = navController,
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
                        "${AppRoutes.POEM}/{poetId}/{poemId}/{verseId}",
                        arguments = listOf(
                            navArgument("poetId") { type = NavType.IntType },
                            // TODO add parentIds here (or at least the last of them)
                            navArgument("poemId") { type = NavType.IntType },
                            navArgument("verseId") { type = NavType.IntType },
                        )
                    ) { backStackEntry ->
                        val poetId = backStackEntry.arguments?.getInt("poetId")
                        val poemId = backStackEntry.arguments?.getInt("poemId")
                        val verseId = backStackEntry.arguments?.getInt("verseId")
                            ?.let { if (it == -1) null else it }

                        VerseScreen(
                            navController,
                            poemId = poemId!!,
                            poetId = poetId!!,
                            focusedVerseId = verseId,
                            audioViewModel = audioViewModel,
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
                            fontRepository = fontRepository,
                        )
                    }
                    composable(AppRoutes.CHANGE_THEME_SCREEN.toString()) {
                        ChangeThemeScreen(
                            modifier = Modifier.padding(innerPadding),
                            themeRepository = themeRepository,
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