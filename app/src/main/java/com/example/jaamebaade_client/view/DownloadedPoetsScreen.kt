package com.example.jaamebaade_client.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.jaamebaade_client.model.Poet
import com.example.jaamebaade_client.viewmodel.DownloadedPoetViewModel

@Composable
fun DownloadedPoetsScreen(
    modifier: Modifier = Modifier,
    downloadedPoetViewModel: DownloadedPoetViewModel = hiltViewModel(),
    navController: NavController
) {
    val poets = downloadedPoetViewModel.poets
    if (poets.isEmpty()) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Column (
                horizontalAlignment = Alignment.CenterHorizontally
            ){
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
                    // Navigate to the poet's page

                }
            }

        }
    }
}

@Composable
fun DownloadedPoet(poet: Poet, onClick: () -> Unit) {
    // FIXME make ripple effect round
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier
        .clickable { onClick() }) {
        Image(
            painter = rememberAsyncImagePainter(
                model = ImageRequest.Builder(LocalContext.current)
                    .data("https://ganjoor.net/image/gdap.png") // TODO change hardcoded url!
                    .size(coil.size.Size.ORIGINAL) // Set the target size to load the image at.
                    .build()
            ),
            contentDescription = poet.name,
            modifier = Modifier
                .size(100.dp) // adjust the size as needed
                .clip(CircleShape)
        )
        Text(text = poet.name, fontSize = 20.sp) // replace with your poet's name
    }
}
