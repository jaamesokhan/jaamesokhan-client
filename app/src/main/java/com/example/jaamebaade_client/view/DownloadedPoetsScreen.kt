package com.example.jaamebaade_client.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.jaamebaade_client.model.Poet
import com.example.jaamebaade_client.viewmodel.DownloadedPoetViewModel

@Composable
fun DownloadedPoetsScreen(
    downloadedPoetViewModel: DownloadedPoetViewModel = hiltViewModel(),
    modifier: Modifier
) {
    val poets = downloadedPoetViewModel.poets
    LazyVerticalGrid(columns = GridCells.Fixed(3), modifier = modifier) {
        items(poets) { poet ->
            DownloadedPoet(poet = poet)
        }

    }
}
@Composable
fun DownloadedPoet(poet: Poet) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Image(
            painter = rememberAsyncImagePainter(
                model = ImageRequest.Builder(LocalContext.current)
                    .data("https://ganjoor.net/image/gdap.png") // TODO change hardcoded url!
                    .size(coil.size.Size.ORIGINAL) // Set the target size to load the image at.
                    .build()),
            contentDescription = "Poet Image",
            modifier = Modifier
                .size(100.dp) // adjust the size as needed
                .clip(CircleShape)
        )
        Text(text = poet.name) // replace with your poet's name
    }
}
