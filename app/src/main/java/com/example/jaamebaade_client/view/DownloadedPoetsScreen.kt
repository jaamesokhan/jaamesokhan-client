package com.example.jaamebaade_client.view

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "هیچ شاعری را دانلود نکرده‌ای!",
                    fontSize = 20.sp,
                    modifier = modifier.padding(8.dp)
                )
                Button(onClick = { navController.navigate("DownloadablePoetsScreen") }) {
                    Text(text = "دانلود شاعران")
                }
            }
        }
    } else {
        LazyVerticalGrid(columns = GridCells.Fixed(3), modifier = modifier.padding(8.dp)) {
            items(poets) { poet ->
                DownloadedPoet(poet = poet) {
                    coroutineScope.launch {
                        val poetCategoryId = downloadedPoetViewModel.getPoetCategoryId(poet.id)
                        navController.navigate("poetCategoryScreen/${poet.id}/${poetCategoryId}")
                    }
                }
            }
        }
    }
}

