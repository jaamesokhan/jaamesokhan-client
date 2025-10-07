package ir.jaamebaade.jaamebaade_client.view.components

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import ir.jaamebaade.jaamebaade_client.R
import ir.jaamebaade.jaamebaade_client.constants.AppRoutes
import ir.jaamebaade.jaamebaade_client.view.HistoryScreen
import ir.jaamebaade.jaamebaade_client.view.OptionsMenu
import ir.jaamebaade.jaamebaade_client.viewmodel.AppNavHostViewModel
import ir.jaamebaade.jaamebaade_client.viewmodel.MyPoetsViewModel
import ir.jaamebaade.jaamebaade_client.viewmodel.TopBarViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    navController: NavController,
    viewModel: TopBarViewModel = hiltViewModel(),
    myPoetsViewModel: MyPoetsViewModel = hiltViewModel(),
    appNavHostViewModel: AppNavHostViewModel,
) {
    val backStackEntry by navController.currentBackStackEntryAsState()
    val canPop =
        (backStackEntry?.destination?.route != AppRoutes.DOWNLOADED_POETS_SCREEN.toString() && backStackEntry?.destination?.route != AppRoutes.BOOKMARKS_SCREEN.toString() &&
                backStackEntry?.destination?.route != AppRoutes.HIGHLIGHTS_SCREEN.toString() && backStackEntry?.destination?.route != AppRoutes.NOTES_SCREEN.toString())
    val context = LocalContext.current


    LaunchedEffect(key1 = backStackEntry) {
        viewModel.updateBreadCrumbs(backStackEntry, context)
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
    var showSettingBottomSheet by remember { mutableStateOf(false) }
    var showHistoryBottomSheet by remember { mutableStateOf(false) }
    val historyBottomSheetState = rememberModalBottomSheetState()

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

    if (showSettingBottomSheet) {
        OptionsMenu(
            onDismiss = { showSettingBottomSheet = false },
            navController = navController,
            myPoetsViewModel = myPoetsViewModel,
        )
    }

    if (showHistoryBottomSheet) {
        HistoryScreen(
            sheetState = historyBottomSheetState,
            onDismiss = {
                showHistoryBottomSheet = false

            },
            navController = navController,

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
                                        imageVector = if (downArrow) ImageVector.vectorResource(R.drawable.back_arrow_down) else Icons.AutoMirrored.Filled.ArrowBack,
                                        tint = MaterialTheme.colorScheme.onBackground,
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
                                        tint = MaterialTheme.colorScheme.onBackground,
                                        modifier = Modifier.size(32.dp),
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            breadCrumbs?.let {
                                Text(
                                    text = it,
                                    style = MaterialTheme.typography.titleLarge,
                                    color = MaterialTheme.colorScheme.onBackground,
                                    overflow = TextOverflow.Ellipsis,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.sizeIn(
                                        maxWidth = 260.dp
                                    ),
                                    maxLines = 1
                                )
                            }
                        }
                        Row(modifier = Modifier.padding(horizontal = 12.dp)) {
                            if (showHistory) {
                                IconButton(
                                    modifier = Modifier,
                                    onClick = { showHistoryBottomSheet = true }) {
                                    Icon(
                                        imageVector = Icons.Filled.History,
                                        contentDescription = "History",
                                        tint = MaterialTheme.colorScheme.onBackground,
                                        modifier = Modifier.size(32.dp),
                                    )
                                }
                            }
                            if (showSearch) {
                                IconButton(
                                    modifier = Modifier,
                                    onClick = { navController.navigate("${AppRoutes.SEARCH_SCREEN}") }) {
                                    Icon(
                                        imageVector = Icons.Default.Search,
                                        contentDescription = stringResource(R.string.SEARCH_BAR_HINT),
                                        tint = MaterialTheme.colorScheme.onBackground,
                                        modifier = Modifier.size(32.dp),
                                    )
                                }
                            }
                            if (showOptions) {
                                IconButton(
                                    onClick = { showPoetOptionModal = true }) {
                                    Icon(
                                        imageVector = Icons.Default.MoreVert,
                                        contentDescription = stringResource(R.string.SEARCH_BAR_HINT),
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
            AudioControlBar(navController = navController, viewModel = appNavHostViewModel)
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
        val popped = navController.popBackStack()
        if (!popped) {
            navController.navigate(AppRoutes.DOWNLOADED_POETS_SCREEN.toString()) {
                popUpTo(AppRoutes.DOWNLOADED_POETS_SCREEN.toString()) {
                    inclusive = true
                }
            }
        }
    }
}
