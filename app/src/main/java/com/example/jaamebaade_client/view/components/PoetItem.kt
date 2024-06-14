package com.example.jaamebaade_client.view.components

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.media3.exoplayer.offline.Download
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.jaamebaade_client.model.Poet
import com.example.jaamebaade_client.utility.DownloadStatus

@Composable
fun PoetItem(poet: Poet, status: DownloadStatus, onClick: () -> Unit) {
    val painter = rememberAsyncImagePainter(
        model = ImageRequest.Builder(LocalContext.current)
            .data("https://ganjoor.net/image/gdap.png") // TODO change hardcoded url!
            .size(coil.size.Size.ORIGINAL) // Set the target size to load the image at.
            .build()
    )

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp, horizontal = 4.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.surface
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween

        ) {
            if (painter.state is AsyncImagePainter.State.Loading) {
                CircularProgressIndicator()
            } else {
                Image(
                    painter = painter,
                    modifier = Modifier
                        .size(80.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop,
                    contentDescription = poet.name
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = poet.name,
                style = MaterialTheme.typography.headlineMedium
            ) // TODO constant?

            Spacer(modifier = Modifier.width(16.dp))

            when (status) {
                DownloadStatus.NotDownloaded -> {
                    Icon(imageVector = Icons.Filled.Add, contentDescription = "Download")
                }
                DownloadStatus.Downloading -> {
                    CircularProgressIndicator()
                }
                DownloadStatus.Downloaded -> {
                    Icon(imageVector = Icons.Default.Check, contentDescription = "Downloaded")
                }
                DownloadStatus.Failed -> {
                    Toast.makeText(LocalContext.current, "Failed to download ${poet.name} ", Toast.LENGTH_SHORT).show() // TODO
                    Icon(imageVector = Icons.Filled.Add, contentDescription = "Download")
                }
            }
        }
    }
}
