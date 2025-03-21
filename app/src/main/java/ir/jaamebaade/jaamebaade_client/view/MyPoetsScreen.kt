package ir.jaamebaade.jaamebaade_client.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
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
import ir.jaamebaade.jaamebaade_client.view.components.RandomPoemBox
import ir.jaamebaade.jaamebaade_client.view.components.base.SquareButton
import ir.jaamebaade.jaamebaade_client.viewmodel.MyPoetsViewModel
import kotlinx.coroutines.launch

@Composable
fun IntroShowcaseScope.MyPoetsScreen(
    modifier: Modifier = Modifier,
    viewModel: MyPoetsViewModel = hiltViewModel(),
    navController: NavController
) {
    val poets = viewModel.poets
    var fetchStatue by remember { mutableStateOf(Status.LOADING) }
    val coroutineScope = rememberCoroutineScope()
    val selectedPoets = remember { mutableStateListOf<Poet>() }
    val randomPoemPreview = viewModel.randomPoemPreview

    LaunchedEffect(key1 = poets) {
        if (poets != null) fetchStatue = Status.SUCCESS
    }
    if (fetchStatue == Status.SUCCESS) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
        ) {
            randomPoemPreview?.let {
                RandomPoemBox(randomPoemPreview = it, onCardClick = {
                    navController.navigate("${AppRoutes.POEM}/${randomPoemPreview.poemPath.poem.id}/${randomPoemPreview.poemPath.poem.id}/-1")
                }) {
                    viewModel.getRandomPoem()
                }
            }

            LazyVerticalGrid(columns = GridCells.Fixed(3)) {
                if (poets!!.isNotEmpty()) {
                    items(poets) { poet ->
                        val isSelected = selectedPoets.contains(poet)
                        PoetIconButton(poet = poet, onLongClick = {
                            // TODO : Implement onLongClick
                            selectedPoets.add(poet)
                        }) {
                            if (selectedPoets.isEmpty()) {
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
                            } else {
                                if (isSelected) {
                                    selectedPoets.remove(poet)
                                } else {
                                    selectedPoets.add(poet)
                                }
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


