package com.example.jaamebaade_client.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
        Box(
            modifier = modifier
                .fillMaxSize()
                .padding(8.dp)
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
                    .background(MaterialTheme.colorScheme.primary, shape = CircleShape)

            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add Poet",
                    tint = Color.White // TODO hardcoded color?
                )
            }
        }
    }
}

