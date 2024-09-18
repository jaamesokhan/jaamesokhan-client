package ir.jaamebaade.jaamebaade_client.view

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Download
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.canopas.lib.showcase.IntroShowcase
import com.canopas.lib.showcase.component.ShowcaseStyle
import ir.jaamebaade.jaamebaade_client.R
import ir.jaamebaade.jaamebaade_client.constants.AppRoutes
import ir.jaamebaade.jaamebaade_client.model.Poet
import ir.jaamebaade.jaamebaade_client.model.Status
import ir.jaamebaade.jaamebaade_client.utility.toNavArgs
import ir.jaamebaade.jaamebaade_client.view.components.DownloadedPoet
import ir.jaamebaade.jaamebaade_client.view.components.LoadingIndicator
import ir.jaamebaade.jaamebaade_client.view.components.RoundButton
import ir.jaamebaade.jaamebaade_client.viewmodel.DownloadedPoetViewModel
import kotlinx.coroutines.launch

@Composable
fun DownloadedPoetsScreen(
    modifier: Modifier = Modifier,
    downloadedPoetViewModel: DownloadedPoetViewModel = hiltViewModel(),
    navController: NavController
) {
    val poets = downloadedPoetViewModel.poets
    var fetchStatue by remember { mutableStateOf(Status.LOADING) }
    val coroutineScope = rememberCoroutineScope()
    val selectedPoets = remember { mutableStateListOf<Poet>() }
    val showAppIntro by downloadedPoetViewModel.showAppIntro.collectAsState()

    LaunchedEffect(key1 = poets) {
        if (poets != null) fetchStatue = Status.SUCCESS
    }
    if (fetchStatue == Status.SUCCESS && poets!!.isNotEmpty()) {
        Box(
            modifier = modifier
                .fillMaxSize()
        ) {
            LazyVerticalGrid(columns = GridCells.Fixed(3)) {
                items(poets) { poet ->
                    val isSelected = selectedPoets.contains(poet)
                    DownloadedPoet(poet = poet, isSelected = isSelected, onLongClick = {
                        selectedPoets.add(poet)
                    }) {
                        if (selectedPoets.isEmpty()) {
                            coroutineScope.launch {
                                val poetCategoryId =
                                    downloadedPoetViewModel.getPoetCategoryId(poet.id)
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
        }
    }
    Box(
        modifier = modifier
            .fillMaxSize(), contentAlignment = Alignment.Center
    ) {
        if (fetchStatue == Status.SUCCESS && poets!!.isEmpty()) {
            Text(
                text = "هیچ شاعری را دانلود نکرده‌ای!",
                style = MaterialTheme.typography.headlineMedium,
                modifier = modifier.padding(8.dp)
            )
        } else if (fetchStatue == Status.LOADING) {
            LoadingIndicator()
        }
        if (selectedPoets.isEmpty()) {
            IntroShowcase(
                showIntroShowCase = showAppIntro,
                dismissOnClickOutside = true,
                onShowCaseCompleted = {
                    coroutineScope.launch {
                        downloadedPoetViewModel.setShowAppIntroState(false)
                    }
                },
            ) {
                RoundButton(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .introShowCaseTarget(
                            index = 0,
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
                    icon = Icons.Filled.Download,
                    contentDescription = "Add Poet"
                ) {
                    navController.navigate(AppRoutes.DOWNLOADABLE_POETS_SCREEN.toString()) {
                        popUpTo(AppRoutes.DOWNLOADED_POETS_SCREEN.toString()) {
                            inclusive = true
                        }
                    }
                }
            }
        } else if (fetchStatue == Status.SUCCESS) {

            RoundButton(
                modifier = Modifier.align(Alignment.BottomEnd),
                icon = Icons.Filled.Delete,
                contentDescription = "Delete Poet"
            ) {
                fetchStatue = Status.LOADING
                downloadedPoetViewModel.deletePoets(selectedPoets) {
                    selectedPoets.clear()
                    fetchStatue = Status.SUCCESS
                }
            }
        }
    }
}


@Composable
fun ButtonIntro(title: String, desc: String) {
    Column {
        Text(
            text = title,
            color = MaterialTheme.colorScheme.onPrimary,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = desc,
            color = MaterialTheme.colorScheme.onPrimary,
            fontSize = 16.sp
        )
        Spacer(modifier = Modifier.height(10.dp))
    }
}