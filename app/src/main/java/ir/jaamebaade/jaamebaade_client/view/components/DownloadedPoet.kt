package ir.jaamebaade.jaamebaade_client.view.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.size.Size
import ir.jaamebaade.jaamebaade_client.model.Poet
import ir.jaamebaade.jaamebaade_client.utility.shake

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DownloadedPoet(poet: Poet, isSelected: Boolean, onLongClick: () -> Unit, onClick: () -> Unit) {
    // FIXME make ripple effect round
    Column(
        modifier = if (isSelected) {
            Modifier.background(color = MaterialTheme.colorScheme.inverseOnSurface)
        } else {
            Modifier
        }
            .padding(8.dp)
            .shake(isSelected)
            .combinedClickable(onClick = onClick, onLongClick = onLongClick),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Image(
            painter = rememberAsyncImagePainter(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(poet.imageUrl ?: "https://ganjoor.net/image/gdap.png")
                    .size(Size.ORIGINAL)
                    .build()
            ),
            contentDescription = poet.name,
            modifier = Modifier
                .size(100.dp)
                .clip(CircleShape)
        )
        Text(
            text = poet.name,
            style = MaterialTheme.typography.titleMedium,
            textAlign = TextAlign.Center,
        )
    }
}
