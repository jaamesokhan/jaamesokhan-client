package ir.jaamebaade.jaamebaade_client.view

import android.widget.Toast
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.canopas.lib.showcase.IntroShowcaseScope
import com.canopas.lib.showcase.component.ShowcaseStyle
import ir.jaamebaade.jaamebaade_client.R
import ir.jaamebaade.jaamebaade_client.constants.AppRoutes
import ir.jaamebaade.jaamebaade_client.model.Poet
import ir.jaamebaade.jaamebaade_client.model.Status
import ir.jaamebaade.jaamebaade_client.ui.theme.neutralN95
import ir.jaamebaade.jaamebaade_client.utility.toNavArgs
import ir.jaamebaade.jaamebaade_client.view.components.ButtonIntro
import ir.jaamebaade.jaamebaade_client.view.components.PoetIconButton
import ir.jaamebaade.jaamebaade_client.view.components.PoetOptionsBottomSheet
import ir.jaamebaade.jaamebaade_client.view.components.RandomPoemBox
import ir.jaamebaade.jaamebaade_client.view.components.base.SquareButton
import ir.jaamebaade.jaamebaade_client.viewmodel.MyPoetsViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IntroShowcaseScope.MyPoetsScreen(
    modifier: Modifier = Modifier,
    viewModel: MyPoetsViewModel = hiltViewModel(),
    navController: NavController
) {
    val poets = viewModel.poets
    var fetchStatue by remember { mutableStateOf(Status.LOADING) }
    val coroutineScope = rememberCoroutineScope()
    val randomPoemPreview = viewModel.randomPoemPreview

    val sheetState = rememberModalBottomSheetState()
    var showBottomSheet by remember { mutableStateOf(false) }
    var selectedPoet by remember { mutableStateOf<Poet?>(null) }

    val context = LocalContext.current

    LaunchedEffect(key1 = poets) {
        if (poets != null) fetchStatue = Status.SUCCESS
    }

    if (showBottomSheet && selectedPoet != null) {
        PoetOptionsBottomSheet(
            poet = selectedPoet!!,
            onDismiss = { showBottomSheet = false },
            sheetState = sheetState,
            onDeleteClick = {
                viewModel.deletePoets(
                    selectedPoet!!
                ) {
                    showBottomSheet = false
                }
            }
        )
    }

    if (fetchStatue == Status.SUCCESS) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
        ) {
            randomPoemPreview?.let {
                val onPoemOpenErrorText = stringResource(R.string.POEM_NOT_AVAILABLE)
                RandomPoemBox(randomPoemPreview = it, onCardClick = {
                    if (viewModel.poets?.find { randomPoemPreview.poemPath.poet.id == it.id } != null) {
                        navController.navigate("${AppRoutes.POEM}/${randomPoemPreview.poemPath.poet.id}/${randomPoemPreview.poemPath.poem.id}/-1")
                    } else {
                        // TODO : we have to customize toast later. https://medium.com/@mahbooberezaee68/custom-toast-in-jetpack-compose-7608b541fd5f
                        Toast.makeText(context, onPoemOpenErrorText, Toast.LENGTH_SHORT).show()
                    }
                }) {
                    viewModel.getRandomPoem()
                }
            }

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
                        modifier = Modifier
                            .introShowCaseTarget(
                                index = 4,
                                style = ShowcaseStyle.Default.copy(
                                    backgroundColor = MaterialTheme.colorScheme.primary,
                                    backgroundAlpha = 0.98f,
                                    targetCircleColor = MaterialTheme.colorScheme.onPrimary
                                ),
                                content = {
                                    ButtonIntro(
                                        stringResource(R.string.INTRO_DOWNLOAD_TITLE),
                                        stringResource(R.string.INTRO_DOWNLOAD_DESC)
                                    )
                                }
                            ),
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