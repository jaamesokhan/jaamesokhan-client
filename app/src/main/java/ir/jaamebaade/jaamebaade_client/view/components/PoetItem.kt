package ir.jaamebaade.jaamebaade_client.view.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material.icons.outlined.Download
import androidx.compose.material.icons.outlined.DownloadDone
import androidx.compose.material.icons.outlined.FileDownloadOff
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.request.CachePolicy
import coil.request.ImageRequest
import ir.jaamebaade.jaamebaade_client.model.Poet
import ir.jaamebaade.jaamebaade_client.utility.DownloadStatus

@Composable
fun PoetItem(poet: Poet, status: DownloadStatus, onButtonClick: () -> Unit) {
    val iconSize = 28.dp

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
                        style = MaterialTheme.typography.titleLarge,
                        maxLines = 3,
                        modifier = Modifier.weight(1f)
                    )

                    Button(
                        onClick = onButtonClick,

                        ) {
                        when (status) {
                            DownloadStatus.NotDownloaded -> {
                                Icon(
                                    modifier = Modifier.size(iconSize),
                                    imageVector = Icons.Outlined.Download,
                                    contentDescription = "Download"
                                )
                                Spacer(Modifier.width(4.dp))
                                Text("افزودن")
                            }

                            DownloadStatus.Downloading -> {
                                CircularProgressIndicator(modifier = Modifier.size(iconSize))
                            }

                            DownloadStatus.Downloaded -> {
                                Icon(
                                    modifier = Modifier.size(iconSize),
                                    imageVector = Icons.Outlined.DownloadDone,
                                    contentDescription = "Downloaded"
                                )
                                Spacer(Modifier.width(4.dp))
                                Text("باز کردن")
                            }

                            DownloadStatus.Failed -> {
                                Icon(
                                    modifier = Modifier.size(iconSize),
                                    imageVector = Icons.Outlined.FileDownloadOff,
                                    contentDescription = "Download"
                                )
                            }
                        }
                    }
                }

//                Text(
//                    text = poet.description,
//                    modifier = Modifier
//                        .animateContentSize()
//                        .clickable { expanded = !expanded },
//                    style = MaterialTheme.typography.bodySmall,
//                    overflow = TextOverflow.Ellipsis,
//                    maxLines = if (expanded) Int.MAX_VALUE else 3
//                )
            }

        }
    }
}
