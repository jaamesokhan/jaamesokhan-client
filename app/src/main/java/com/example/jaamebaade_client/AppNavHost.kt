package com.example.jaamebaade_client

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.dialog
import androidx.navigation.compose.rememberNavController
import com.example.jaamebaade_client.repository.FontRepository
import com.example.jaamebaade_client.ui.theme.FONT_SCALE
import com.example.jaamebaade_client.ui.theme.JaamebaadeclientTheme
import com.example.jaamebaade_client.ui.theme.Nastaliq
import com.example.jaamebaade_client.ui.theme.VazirMatn
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
        return when (type) {
            "small" -> TextStyle(
                fontFamily = selectedFontFamily,
                fontSize = (fontSize * FONT_SCALE - 5).sp
            )
            "large" -> TextStyle(
                fontFamily = selectedFontFamily,
                fontSize = (fontSize * FONT_SCALE + 5).sp
            )
            else -> TextStyle(
                fontFamily = selectedFontFamily,
                fontSize = (fontSize * FONT_SCALE).sp
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
            // Define other text styles as needed
        )
    }
    JaamebaadeclientTheme(typography = typography) {
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

