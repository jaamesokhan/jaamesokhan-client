package ir.jaamebaade.jaamebaade_client.view


import android.content.Context
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import ir.jaamebaade.jaamebaade_client.model.Poet
import ir.jaamebaade.jaamebaade_client.utility.DownloadStatus
import ir.jaamebaade.jaamebaade_client.view.components.LoadingIndicator
import ir.jaamebaade.jaamebaade_client.view.components.PoetItem
import ir.jaamebaade.jaamebaade_client.view.components.ServerFailure
import ir.jaamebaade.jaamebaade_client.viewmodel.PoetViewModel
import java.io.File

@Composable
fun DownloadablePoetsScreen(
    modifier: Modifier = Modifier,
    poetViewModel: PoetViewModel = hiltViewModel(),
) {
    val poets = poetViewModel.poets
    val isLoading = poetViewModel.isLoading
    var searchQuery by remember { mutableStateOf("") }

    Column(modifier = modifier) {
        TextField(
            value = searchQuery,
            onValueChange = {
                searchQuery = it
                poetViewModel.updateSearchQuery(it)
            },
            label = { Text("نام شاعر", style = MaterialTheme.typography.labelSmall) },
            modifier = Modifier.fillMaxWidth(),
            textStyle = MaterialTheme.typography.bodyMedium,
        )
        if (poets.isEmpty() && isLoading) {
            LoadingIndicator()
        } else if (poets.isEmpty()) {
            ServerFailure()
        } else {
            Column {
                DownloadablePoetsList(poetViewModel, poets, isLoading)
            }
        }
    }
}

@Composable
fun DownloadablePoetsList(
    poetViewModel: PoetViewModel,
    poets: List<Poet>,
    isLoading: Boolean,
) {
    val context = LocalContext.current
    val listState = rememberLazyListState()

    LazyColumn(userScrollEnabled = true, modifier = Modifier.padding(8.dp), state = listState) {
        itemsIndexed(poets) { index, poet ->
            PoetItem(
                poet = poet,
                poetViewModel.downloadStatus[poet.id.toString()] ?: DownloadStatus.NotDownloaded
            ) {
                val targetDir = getInternalStorageDir(context)
                poetViewModel.importPoetData(poet.id.toString(), targetDir)
            }
            if (index != poets.size - 1) {
                HorizontalDivider()
            }
        }
        item {
            if (isLoading) {
                LoadingIndicator()
            }
        }
    }
    val shouldLoadMore = remember {
        derivedStateOf {
            val lastVisibleItem = listState.layoutInfo.visibleItemsInfo.lastOrNull()
            lastVisibleItem != null && lastVisibleItem.index == poetViewModel.poets.size - 1
        }
    }

    LaunchedEffect(shouldLoadMore.value) {
        if (shouldLoadMore.value) {
            poetViewModel.loadMorePoets()
        }
    }
}

fun getInternalStorageDir(context: Context): File {
    val dir = File(context.filesDir, "poets")
    if (!dir.exists()) {
        dir.mkdirs()
    }
    return dir
}