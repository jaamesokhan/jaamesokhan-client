package com.example.jaamebaade_client.view.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.size.Size
import com.example.jaamebaade_client.model.Poet

@Composable
fun DownloadedPoet(poet: Poet, onClick: () -> Unit) {
    // FIXME make ripple effect round
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier
        .padding(8.dp)
        .clickable { onClick() }) {
        Image(
            painter = rememberAsyncImagePainter(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(poet.imageUrl ?: "https://ganjoor.net/image/gdap.png")
                    .size(Size.ORIGINAL) // Set the target size to load the image at.
                    .build()
            ),
            contentDescription = poet.name,
            modifier = Modifier
                .size(100.dp) // adjust the size as needed
                .clip(CircleShape)
        )
        Text(text = poet.name, style = MaterialTheme.typography.headlineSmall) // replace with your poet's name
    }
}
