package ir.jaamebaade.jaamebaade_client.view.components.base

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import coil.request.CachePolicy
import coil.request.ImageRequest
import ir.jaamebaade.jaamebaade_client.R

@Composable
fun SquareImage(
    modifier: Modifier = Modifier,
    imageUrl: String?,
    contentDescription: String?,
    size: Int,
    scale: Float = 1.1f,
    roundedCornerShapeSize: Int = 25,
) {
    val painter = if (imageUrl != null) {
         rememberAsyncImagePainter(
            model = ImageRequest.Builder(LocalContext.current)
                .data(imageUrl)
                .size(coil.size.Size.ORIGINAL) // Set the target size to load the image at.
                .diskCachePolicy(CachePolicy.ENABLED) // Enable disk cache
                .memoryCachePolicy(CachePolicy.ENABLED) // Enable memory cache
                .build()
        )
    } else {
        painterResource(id = R.mipmap.logo)
    }
    Image(
        painter = painter,
        contentDescription = contentDescription,
        contentScale = ContentScale.Crop,
        modifier = modifier
            .size(size.dp)
            .clip(RoundedCornerShape(roundedCornerShapeSize.dp))
            .scale(scale)
    )
}