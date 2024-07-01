package com.example.jaamebaade_client.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Download
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.jaamebaade_client.view.components.DownloadedPoet
import com.example.jaamebaade_client.viewmodel.DownloadedPoetViewModel
import kotlinx.coroutines.launch

@Composable
fun DownloadedPoetsScreen(
    modifier: Modifier = Modifier,
    downloadedPoetViewModel: DownloadedPoetViewModel = hiltViewModel(),
    navController: NavController
) {
    val poets = downloadedPoetViewModel.poets
    val coroutineScope = rememberCoroutineScope()
    if (poets.isEmpty()) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(
                text = "هیچ شاعری را دانلود نکرده‌ای!",
                style = MaterialTheme.typography.headlineMedium,
                modifier = modifier.padding(8.dp)
            )
        }
    } else {
        Box(
            modifier = modifier
                .fillMaxSize()
                .padding(8.dp, 16.dp)
        ) {
            LazyVerticalGrid(columns = GridCells.Fixed(3)) {
                items(poets) { poet ->
                    DownloadedPoet(poet = poet) {
                        coroutineScope.launch {
                            val poetCategoryId = downloadedPoetViewModel.getPoetCategoryId(poet.id)
                            navController.navigate("poetCategoryScreen/${poet.id}/${poetCategoryId}")
                        }
                    }
                }
            }

            IconButton(
                onClick = {
                    navController.navigate("downloadablePoetsScreen")
                },
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(16.dp)
                    .size(60.dp)
                    .background(MaterialTheme.colorScheme.primary, shape = CircleShape)

            ) {
                Icon(
                    imageVector = Icons.Filled.Download,
                    contentDescription = "Add Poet",
                )
            }
        }
    }
}

