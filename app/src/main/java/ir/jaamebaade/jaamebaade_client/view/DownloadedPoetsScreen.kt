package ir.jaamebaade.jaamebaade_client.view

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
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

    LaunchedEffect(key1 = poets) {
        if (poets != null) fetchStatue = Status.SUCCESS
    }
    if (fetchStatue == Status.SUCCESS && poets!!.isNotEmpty()) {
        Box(
            modifier = modifier
                .padding(4.dp)
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
            RoundButton(
                modifier = Modifier.align(Alignment.BottomEnd),
                icon = Icons.Filled.Download,
                contentDescription = "Add Poet"
            ) {
                navController.navigate(AppRoutes.DOWNLOADABLE_POETS_SCREEN.toString())
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

