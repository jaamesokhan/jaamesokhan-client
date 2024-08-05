package com.example.jaamebaade_client.view.components

import android.widget.Toast
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.DownloadDone
import androidx.compose.material.icons.filled.FileDownloadOff
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.example.jaamebaade_client.model.Poet
import com.example.jaamebaade_client.utility.DownloadStatus

@Composable
fun PoetItem(poet: Poet, status: DownloadStatus, onClick: () -> Unit) {
    var expanded by remember { mutableStateOf(false) }

    val painter = rememberAsyncImagePainter(
        model = ImageRequest.Builder(LocalContext.current)
            .data(
                poet.imageUrl ?: "https://ganjoor.net/image/gdap.png"
            )
            .size(coil.size.Size.ORIGINAL) // Set the target size to load the image at.
            .diskCachePolicy(CachePolicy.ENABLED) // Enable disk cache
            .memoryCachePolicy(CachePolicy.ENABLED) // Enable memory cache
            .build()
    )

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp, horizontal = 4.dp),
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.surface
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .heightIn(min = 200.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween

        ) {
            if (painter.state is AsyncImagePainter.State.Loading) {
                CircularProgressIndicator(
                    modifier = Modifier.widthIn(min = 100.dp)
                )
            } else {
                Image(
                    painter = painter,
                    modifier = Modifier
                        .size(100.dp)
                        .widthIn(min = 100.dp),
                    contentDescription = poet.name
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(4.dp),
                ) {
                    Text(
                        text = poet.name,
                        modifier = Modifier.weight(0.8f),
                        style = MaterialTheme.typography.titleLarge,
                        maxLines = 3
                    )

                    Box(
                        modifier = Modifier
                            .weight(0.2f)
                            .clickable { onClick() },
                    ) {
                        when (status) {
                            DownloadStatus.NotDownloaded -> {
                                Icon(
                                    imageVector = Icons.Filled.Download,
                                    contentDescription = "Download"
                                )
                            }

                            DownloadStatus.Downloading -> {
                                CircularProgressIndicator()
                            }

                            DownloadStatus.Downloaded -> {
                                Icon(
                                    imageVector = Icons.Filled.DownloadDone,
                                    contentDescription = "Downloaded"
                                )
                            }

                            DownloadStatus.Failed -> {
                                Toast.makeText(
                                    LocalContext.current,
                                    "Failed to download ${poet.name} ",
                                    Toast.LENGTH_SHORT
                                ).show()
                                Icon(
                                    imageVector = Icons.Filled.FileDownloadOff,
                                    contentDescription = "Download"
                                )
                            }
                        }
                    }
                }

                Text(
                    text = poet.description,
                    modifier = Modifier
//                        .weight(1f)
                        .animateContentSize()
                        .clickable { expanded = !expanded },
                    style = MaterialTheme.typography.bodySmall,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = if (expanded) Int.MAX_VALUE else 3
                )
            }

        }
    }
}
