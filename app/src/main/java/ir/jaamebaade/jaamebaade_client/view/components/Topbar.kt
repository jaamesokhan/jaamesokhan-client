package ir.jaamebaade.jaamebaade_client.view.components

import android.util.Log
import android.widget.ImageView
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.canopas.lib.showcase.IntroShowcaseScope
import com.canopas.lib.showcase.component.ShowcaseStyle
import ir.jaamebaade.jaamebaade_client.R
import ir.jaamebaade.jaamebaade_client.constants.AppRoutes
import ir.jaamebaade.jaamebaade_client.ui.theme.neutralN50
import ir.jaamebaade.jaamebaade_client.view.SettingsMenu
import ir.jaamebaade.jaamebaade_client.viewmodel.AudioViewModel
import ir.jaamebaade.jaamebaade_client.viewmodel.MyPoetsViewModel
import ir.jaamebaade.jaamebaade_client.viewmodel.TopBarViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IntroShowcaseScope.TopBar(
    navController: NavController,
    viewModel: TopBarViewModel = hiltViewModel(),
    myPoetsViewModel: MyPoetsViewModel = hiltViewModel(),
    audioViewModel: AudioViewModel
) {
    val backStackEntry by navController.currentBackStackEntryAsState()
    val canPop =
        (backStackEntry?.destination?.route != AppRoutes.DOWNLOADED_POETS_SCREEN.toString()  && backStackEntry?.destination?.route != AppRoutes.BOOKMARKS_SCREEN.toString())
    Log.d("Ttt", backStackEntry.toString())


    LaunchedEffect(key1 = backStackEntry) {
        viewModel.updateBreadCrumbs(backStackEntry)
        viewModel.shouldShowHistory(backStackEntry)
        viewModel.shouldShowOptions(backStackEntry)
        viewModel.shouldShowSearch(backStackEntry)
        viewModel.shouldExtendTopBar(backStackEntry)
        viewModel.shouldShowDownArrow(backStackEntry)
    }

    val breadCrumbs = viewModel.breadCrumbs
    val poet = viewModel.poet
    val showHistory = viewModel.showHistoryIcon
    val showSearch = viewModel.showSearchIcon
    val showOptions = viewModel.showOptionsIcon
    val settingBottomSheetState = rememberModalBottomSheetState()
    var showSettingBottomSheet by remember { mutableStateOf(false) }
    val topBarIsExtended = viewModel.topBarIsExtended
    val downArrow = viewModel.downArrow

    val sheetState = rememberModalBottomSheetState()
    var showPoetOptionModal by remember { mutableStateOf(false) }

    val coroutineScope = rememberCoroutineScope()

    if (showPoetOptionModal) {
        PoetOptionsBottomSheet(
            poet = poet!!,
            onDismiss = { showPoetOptionModal = false },
            sheetState = sheetState,
            showHeader = false,
            showDescription = false
        ) {
            myPoetsViewModel.deletePoet(poet, onSuccess = {
                coroutineScope.launch {
                    showPoetOptionModal = false
                    navController.navigate(AppRoutes.DOWNLOADED_POETS_SCREEN.toString())
                }
            })
        }
    }

    if(showSettingBottomSheet) {
        SettingsMenu(
            sheetState = settingBottomSheetState,
            onDismiss = {showSettingBottomSheet = false},
            navController
        )
    }


    BackHandler(enabled = backStackEntry?.destination?.route == AppRoutes.DOWNLOADABLE_POETS_SCREEN.toString()) {
        onBackButtonClicked(backStackEntry, navController)
    }
    Surface(
        shadowElevation = if (topBarIsExtended) 0.dp else 4.dp
    ) {
        Column {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                ),
                title = {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                        ) {

                            if (canPop) {
                                IconButton(
                                    onClick = {
                                        onBackButtonClicked(backStackEntry, navController)
                                    }) {
                                    Icon(
                                        imageVector = if(downArrow) ImageVector.vectorResource(R.drawable.down_back) else Icons.AutoMirrored.Filled.ArrowBack,
                                        tint = MaterialTheme.colorScheme.onSurface,
                                        contentDescription = "Back",
                                        modifier = Modifier.size(32.dp),
                                    )
                                }
                            } else {
                                IconButton(onClick = {
                                    showSettingBottomSheet = true
                                }) {
                                    Icon(
                                        imageVector = Icons.Default.Menu,
                                        contentDescription = "Settings Menu",
                                        modifier = Modifier.size(32.dp),
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = breadCrumbs,
                                style = MaterialTheme.typography.titleLarge,
                                color = MaterialTheme.colorScheme.neutralN50,
                                overflow = TextOverflow.Ellipsis,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.sizeIn(
                                    maxWidth = 260.dp
                                ),
                                maxLines = 1
                            )
                        }
                        Row {
                            if (showHistory) {
                                IconButton(
                                    modifier = Modifier.introShowCaseTarget(
                                        index = 5, style = ShowcaseStyle.Default.copy(
                                            backgroundColor = MaterialTheme.colorScheme.primary,
                                            backgroundAlpha = 0.98f,
                                            targetCircleColor = MaterialTheme.colorScheme.onPrimary
                                        ), content = {
                                            ButtonIntro(
                                                stringResource(R.string.INTRO_HISTORY_TITLE),
                                                stringResource(R.string.INTRO_HISTORY_DESC)
                                            )
                                        }),
                                    onClick = { navController.navigate("${AppRoutes.HISTORY}") }) {
                                    Icon(
                                        imageVector = Icons.Filled.History,
                                        contentDescription = "History",
                                        tint = MaterialTheme.colorScheme.onSurface,
                                        modifier = Modifier.size(32.dp),
                                    )
                                }
                            }
                            if (showSearch) {
                                IconButton(
                                    modifier = Modifier.introShowCaseTarget(
                                        index = 5, style = ShowcaseStyle.Default.copy(
                                            backgroundColor = MaterialTheme.colorScheme.primary,
                                            backgroundAlpha = 0.98f,
                                            targetCircleColor = MaterialTheme.colorScheme.onPrimary
                                        ), content = {
                                            ButtonIntro(
                                                stringResource(R.string.INTRO_SEARCH_TITLE),
                                                stringResource(R.string.INTRO_SHARE_DESC),
                                            )
                                        }),
                                    onClick = { navController.navigate("${AppRoutes.SEARCH_SCREEN}") }) {
                                    Icon(
                                        imageVector = Icons.Default.Search,
                                        contentDescription = stringResource(R.string.SEARCH),
                                        tint = MaterialTheme.colorScheme.onSurface,
                                        modifier = Modifier.size(32.dp),
                                    )
                                }
                            }
                            if (showOptions) {
                                IconButton(
                                    // TODO this might need an intro
                                    onClick = { showPoetOptionModal = true }) {
                                    Icon(
                                        imageVector = Icons.Default.MoreVert,
                                        contentDescription = stringResource(R.string.SEARCH),
                                        tint = MaterialTheme.colorScheme.onSurface,
                                        modifier = Modifier.size(32.dp),
                                    )
                                }

                            }
                        }
                    }

                },
            )
            poet?.let { PoetInformationBox(poet = it) }
            AudioControlBar(navController = navController, viewModel = audioViewModel)
        }
    }
}


private fun onBackButtonClicked(
    backStackEntry: NavBackStackEntry?, navController: NavController
) {
    if (backStackEntry?.destination?.route == AppRoutes.DOWNLOADABLE_POETS_SCREEN.toString()) {
        navController.navigate(AppRoutes.DOWNLOADED_POETS_SCREEN.toString()) {
            popUpTo(AppRoutes.DOWNLOADED_POETS_SCREEN.toString()) {
                inclusive = true
            }
            popUpTo(AppRoutes.DOWNLOADABLE_POETS_SCREEN.toString()) {
                inclusive = true
            }
        }
    } else {
        navController.popBackStack()
    }
}
