package com.example.jaamebaade_client.view


import android.content.Context
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.jaamebaade_client.model.Poet
import com.example.jaamebaade_client.utility.DownloadStatus
import com.example.jaamebaade_client.view.components.LoadingIndicator
import com.example.jaamebaade_client.view.components.PoetItem
import com.example.jaamebaade_client.view.components.ServerFailure
import com.example.jaamebaade_client.viewmodel.PoetViewModel
import java.io.File

@Composable
fun DownloadablePoetsScreen(
    modifier: Modifier = Modifier,
    poetViewModel: PoetViewModel = hiltViewModel(),
) {
    val poets = poetViewModel.poets
    val isLoading = poetViewModel.isLoading
    Box(modifier = modifier) {
        if (isLoading) {
            LoadingIndicator()
        } else if (poets.isEmpty()) {
            ServerFailure()
        } else {
            DownloadablePoetsList(poetViewModel, poets)
        }
    }
}

@Composable
fun DownloadablePoetsList(
    poetViewModel: PoetViewModel,
    poets: List<Poet>,
) {
    val context = LocalContext.current
    LazyColumn(userScrollEnabled = true, modifier = Modifier.padding(8.dp)) {
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
    }
}

fun getInternalStorageDir(context: Context): File {
    val dir = File(context.filesDir, "poets")
    if (!dir.exists()) {
        dir.mkdirs()
    }
    return dir
}