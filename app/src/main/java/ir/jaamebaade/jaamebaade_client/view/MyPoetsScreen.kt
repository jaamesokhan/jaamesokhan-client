package ir.jaamebaade.jaamebaade_client.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import ir.jaamebaade.jaamebaade_client.R
import ir.jaamebaade.jaamebaade_client.constants.AppRoutes
import ir.jaamebaade.jaamebaade_client.model.Poet
import ir.jaamebaade.jaamebaade_client.model.Status
import ir.jaamebaade.jaamebaade_client.ui.theme.neutralN95
import ir.jaamebaade.jaamebaade_client.utility.toNavArgs
import ir.jaamebaade.jaamebaade_client.view.components.PoetIconButton
import ir.jaamebaade.jaamebaade_client.view.components.PoetOptionsBottomSheet
import ir.jaamebaade.jaamebaade_client.view.components.RandomPoemBox
import ir.jaamebaade.jaamebaade_client.view.components.base.SquareButton
import ir.jaamebaade.jaamebaade_client.view.components.toast.ToastType
import ir.jaamebaade.jaamebaade_client.viewmodel.MyPoetsViewModel
import ir.jaamebaade.jaamebaade_client.viewmodel.ToastManager
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyPoetsScreen(
    modifier: Modifier = Modifier,
    viewModel: MyPoetsViewModel = hiltViewModel(),
    navController: NavController
) {
    val poets = viewModel.poets
    var fetchStatue by remember { mutableStateOf(Status.LOADING) }
    val coroutineScope = rememberCoroutineScope()
    var randomPoetPreviewFetchStatus by remember { mutableStateOf(Status.LOADING) }
    val randomPoemPreview = viewModel.randomPoemPreview

    val sheetState = rememberModalBottomSheetState()
    var showBottomSheet by remember { mutableStateOf(false) }
    var selectedPoet by remember { mutableStateOf<Poet?>(null) }

    LaunchedEffect(Unit) {
        viewModel.getRandomPoem(onSuccess = { randomPoetPreviewFetchStatus = Status.SUCCESS })
    }

    LaunchedEffect(key1 = poets) {
        if (poets != null) fetchStatue = Status.SUCCESS
    }

    if (showBottomSheet && selectedPoet != null) {
        PoetOptionsBottomSheet(
            poet = selectedPoet!!,
            onDismiss = { showBottomSheet = false },
            sheetState = sheetState
        ) {
            viewModel.deletePoet(
                selectedPoet!!
            ) {
                showBottomSheet = false
            }
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
    ) {
        if (randomPoetPreviewFetchStatus == Status.SUCCESS) {
            randomPoemPreview?.let {
                RandomPoemBox(randomPoemPreview = it, onCardClick = {
                    if (viewModel.poets?.find { poet -> randomPoemPreview.poemPath.poet.id == poet.id } != null) {
                        navController.navigate("${AppRoutes.POEM}/${randomPoemPreview.poemPath.poet.id}/${randomPoemPreview.poemPath.poem.id}/-1")
                    } else {
                        ToastManager.showToast(R.string.POEM_NOT_AVAILABLE, ToastType.ERROR)
                    }
                }) {
                    viewModel.getRandomPoem(refresh = true, onSuccess = {})
                }
            }
        }

        if (fetchStatue == Status.SUCCESS) {
            LazyVerticalGrid(columns = GridCells.Fixed(3)) {
                if (poets!!.isNotEmpty()) {
                    items(poets) { poet ->
                        PoetIconButton(poet = poet, onLongClick = {
                            selectedPoet = poet
                            showBottomSheet = true
                        }) {
                            coroutineScope.launch {
                                val poetCategoryId =
                                    viewModel.getPoetCategoryId(poet.id)
                                navController.navigate(
                                    "${AppRoutes.POET_CATEGORY_SCREEN}/${poet.id}/${
                                        intArrayOf(
                                            poetCategoryId
                                        ).toNavArgs()
                                    }"
                                )
                            }
                        }
                    }
                }
                item {
                    SquareButton(
                        modifier = Modifier,
                        icon = Icons.Filled.Add,
                        tint = MaterialTheme.colorScheme.neutralN95,
                        backgroundColor = MaterialTheme.colorScheme.outline,
                        contentDescription = stringResource(R.string.ADD_NEW_POET),
                        textStyle = MaterialTheme.typography.headlineMedium,
                    ) {
                        navController.navigate(AppRoutes.DOWNLOADABLE_POETS_SCREEN.toString()) {
                            popUpTo(AppRoutes.DOWNLOADED_POETS_SCREEN.toString()) {
                                inclusive = true
                            }
                        }
                    }
                }
            }
        }
    }
}