package ir.jaamebaade.jaamebaade_client.view.components.base

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImagePainter

@Composable
fun SquareImage(
    modifier: Modifier = Modifier,
    image: AsyncImagePainter,
    contentDescription: String?,
    size: Int,
    scale: Float = 1.1f,
    roundedCornerShapeSize: Int = 25,
) {
    Image(
        painter = image,
        contentDescription = contentDescription,
        contentScale = ContentScale.Crop,
        modifier = modifier
            .size(size.dp)
            .clip(RoundedCornerShape(roundedCornerShapeSize.dp))
            .scale(scale)
    )
}
