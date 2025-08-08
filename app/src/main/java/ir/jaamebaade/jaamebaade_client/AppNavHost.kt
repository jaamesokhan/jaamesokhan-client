package ir.jaamebaade.jaamebaade_client

import android.Manifest
import android.os.Build
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.dialog
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.canopas.lib.showcase.IntroShowcase
import ir.jaamebaade.jaamebaade_client.constants.AppRoutes
import ir.jaamebaade.jaamebaade_client.repository.FontRepository
import ir.jaamebaade.jaamebaade_client.repository.ThemeRepository
import ir.jaamebaade.jaamebaade_client.ui.theme.AppThemeType
import ir.jaamebaade.jaamebaade_client.ui.theme.JaamebaadeclientTheme
import ir.jaamebaade.jaamebaade_client.ui.theme.Typography
import ir.jaamebaade.jaamebaade_client.utility.SharedPrefManager
import ir.jaamebaade.jaamebaade_client.utility.animatedComposable
import ir.jaamebaade.jaamebaade_client.utility.toIntArray
import ir.jaamebaade.jaamebaade_client.view.AccountScreen
import ir.jaamebaade.jaamebaade_client.view.DownloadablePoetsScreen
import ir.jaamebaade.jaamebaade_client.view.FavoritesScreen
import ir.jaamebaade.jaamebaade_client.view.HistoryScreen
import ir.jaamebaade.jaamebaade_client.view.MyBookmarkScreen
import ir.jaamebaade.jaamebaade_client.view.MyNotesScreen
import ir.jaamebaade.jaamebaade_client.view.MyPoetsScreen
import ir.jaamebaade.jaamebaade_client.view.PoemScreen
import ir.jaamebaade.jaamebaade_client.view.PoetDetailScreen
import ir.jaamebaade.jaamebaade_client.view.SearchScreen
import ir.jaamebaade.jaamebaade_client.view.SettingsListScreen
import ir.jaamebaade.jaamebaade_client.view.components.AboutUsScreen
import ir.jaamebaade.jaamebaade_client.view.components.Navbar
import ir.jaamebaade.jaamebaade_client.view.components.PermissionRationaleDialog
import ir.jaamebaade.jaamebaade_client.view.components.RandomPoemOptions
import ir.jaamebaade.jaamebaade_client.view.components.TopBar
import ir.jaamebaade.jaamebaade_client.viewmodel.AudioViewModel
import ir.jaamebaade.jaamebaade_client.viewmodel.MainIntroViewModel

@Composable
fun AppNavHost(
    fontRepository: FontRepository,
    themeRepository: ThemeRepository,
    sharedPrefManager: SharedPrefManager,
    requestPermissionLauncher: (String) -> Unit,
    mainIntroViewModel: MainIntroViewModel = hiltViewModel()
) {
    val audioViewModel: AudioViewModel = hiltViewModel()

    val navController =
        rememberNavController()
    val showIntroShowcase by mainIntroViewModel.showAppIntro.collectAsState()

    val appTheme by themeRepository.appTheme.collectAsState()

    var showPermissionRationale by remember { mutableStateOf(sharedPrefManager.getNotificationPermissionPreference()) }

    JaamebaadeclientTheme(
        darkTheme = when (appTheme) {
            AppThemeType.LIGHT -> false
            AppThemeType.DARK -> true
            AppThemeType.SYSTEM_AUTO -> isSystemInDarkTheme()
        }, typography = Typography
    ) {
        CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
            if (showPermissionRationale) {
                PermissionRationaleDialog(
                    onDismiss = {
                        showPermissionRationale = false
                        sharedPrefManager.setNotificationPermissionPreference(false)
                    },
                    onConfirm = {
                        showPermissionRationale = false
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                            requestPermissionLauncher(Manifest.permission.POST_NOTIFICATIONS)
                        }
                        sharedPrefManager.setNotificationPermissionPreference(false)
                    }
                )
            }
            IntroShowcase(
                showIntroShowCase = showIntroShowcase,
                dismissOnClickOutside = true,
                onShowCaseCompleted = {
                    mainIntroViewModel.setIntroShown()
                },
            ) {
                Scaffold(
                    modifier = Modifier
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
                    NavHost(
                        navController = navController,
                        startDestination = AppRoutes.DOWNLOADED_POETS_SCREEN.toString()
                    ) {
                        animatedComposable(route = AppRoutes.DOWNLOADED_POETS_SCREEN.toString()) {
                            MyPoetsScreen(
                                modifier = Modifier.padding(
                                    innerPadding
                                ), navController = navController
                            )
                        }
                        animatedComposable(route = AppRoutes.DOWNLOADABLE_POETS_SCREEN.toString()) {
                            DownloadablePoetsScreen(modifier = Modifier.padding(innerPadding))
                        }
                        animatedComposable(
                            route = "${AppRoutes.POET_CATEGORY_SCREEN}/{poetId}/{parentIds}",
                            arguments = listOf(
                                navArgument("poetId") { type = NavType.IntType },
                                navArgument("parentIds") { type = NavType.StringType }
                            ),
                        ) { backStackEntry ->
                            val poetId = backStackEntry.arguments?.getInt("poetId")
                            val parentIds =
                                backStackEntry.arguments?.getString("parentIds")?.toIntArray()
                            PoetDetailScreen(
                                modifier = Modifier.padding(innerPadding),
                                poetId = poetId!!,
                                parentIds = parentIds ?: intArrayOf(),
                                navController = navController
                            )
                        }
                        animatedComposable(AppRoutes.SETTINGS_SCREEN.toString()) {
                            SettingsListScreen(modifier = Modifier.padding(innerPadding), fontRepository, themeRepository)
                        }
                        animatedComposable(AppRoutes.RANDOM_POEM_OPTIONS.toString()) {
                            //TODO must implement onSave
                            RandomPoemOptions(modifier = Modifier.padding(innerPadding), onSave = {})
                        }
                        animatedComposable(route = AppRoutes.SEARCH_SCREEN.toString()) {
                            SearchScreen(
                                modifier = Modifier.padding(innerPadding),
                                navController = navController
                            )
                        }
                        animatedComposable(AppRoutes.BOOKMARKS_SCREEN.toString()) {
                            MyBookmarkScreen(
                                modifier = Modifier.padding(innerPadding),
                                navController = navController
                            )
                        }
                        animatedComposable(AppRoutes.HIGHLIGHTS_SCREEN.toString()) {
                            // TODO this is temporary
                            FavoritesScreen(
                                modifier = Modifier.padding(innerPadding),
                                navController = navController
                            )
                        }
                        animatedComposable(AppRoutes.NOTES_SCREEN.toString()) {
                            MyNotesScreen(Modifier.padding(innerPadding), navController)

                        }
                        animatedComposable(
                            "${AppRoutes.POEM}/{poetId}/{poemId}/{verseId}",
                            arguments = listOf(
                                navArgument("poetId") { type = NavType.IntType },
                                // TODO add parentIds here (or at least the last of them)
                                navArgument("poemId") { type = NavType.IntType },
                                navArgument("verseId") { type = NavType.LongType },
                            )
                        ) { backStackEntry ->
                            val poetId = backStackEntry.arguments?.getInt("poetId")
                            val poemId = backStackEntry.arguments?.getInt("poemId")
                            val verseId = backStackEntry.arguments?.getLong("verseId")
                                ?.let { if (it == -1L) null else it }

                            PoemScreen(
                                navController,
                                poemId = poemId!!,
                                poetId = poetId!!,
                                focusedVerseId = verseId,
                                audioViewModel = audioViewModel,
                                modifier = Modifier.padding(innerPadding),
                            )
                        }

                        animatedComposable(AppRoutes.ABOUT_US_SCREEN.toString()) {
                            AboutUsScreen(
                                modifier = Modifier.padding(innerPadding),
                            )
                        }
                        dialog(AppRoutes.ACCOUNT_SCREEN.toString()) {
                            AccountScreen(navController = navController)
                        }
                        animatedComposable(AppRoutes.HISTORY.toString()) {
                            HistoryScreen(Modifier.padding(innerPadding), navController)
                        }
                    }
                }
            }
        }
    }
}

