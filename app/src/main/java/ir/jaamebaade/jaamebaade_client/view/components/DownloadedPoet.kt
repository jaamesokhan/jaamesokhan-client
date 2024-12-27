package ir.jaamebaade.jaamebaade_client.view.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.size.Size
import ir.jaamebaade.jaamebaade_client.model.Poet
import ir.jaamebaade.jaamebaade_client.utility.DownloadStatus
import ir.jaamebaade.jaamebaade_client.utility.shake

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DownloadedPoet(poet: Poet, isSelected: Boolean, onLongClick: () -> Unit, onClick: () -> Unit) {
    val dominantColor = remember { mutableStateOf(Color.Transparent) }
    val painter = rememberAsyncImagePainter(
        model = ImageRequest.Builder(LocalContext.current)
            .data(poet.imageUrl ?: "https://ganjoor.net/image/gdap.png")
            .size(Size.ORIGINAL)
            .build()
    )

    Column(
        modifier = if (isSelected) {
            Modifier.background(color = MaterialTheme.colorScheme.inverseOnSurface)
        } else {
            Modifier
        }
            .clip(RoundedCornerShape(25.dp))
            .width(89.dp)
            .padding(8.dp)
            .shake(isSelected)
            .then(
                if (poet.downloadStatus == DownloadStatus.Downloaded) {
                    Modifier.combinedClickable(onClick = onClick, onLongClick = onLongClick)
                } else {
                    Modifier.alpha(0.5f)
                }
            ),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(modifier = Modifier.background(dominantColor.value)) {
            Image(
                painter = painter,
                contentDescription = poet.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(89.dp)
                    .clip(RoundedCornerShape(25.dp))
                    .scale(1.1f)
            )
        }
        Text(
            text = poet.name,
            style = MaterialTheme.typography.titleMedium,
            textAlign = TextAlign.Center,
        )
    }
}
