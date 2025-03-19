package ir.jaamebaade.jaamebaade_client.view.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.size.Size
import ir.jaamebaade.jaamebaade_client.model.Poet
import ir.jaamebaade.jaamebaade_client.view.components.base.SquareButton

@Composable
fun PoetIconButton(poet: Poet, onLongClick: () -> Unit, onClick: () -> Unit) {
    val painter = rememberAsyncImagePainter(
        model = ImageRequest.Builder(LocalContext.current)
            .data(poet.imageUrl ?: "https://ganjoor.net/image/gdap.png")
            .size(Size.ORIGINAL)
            .build()
    )
    SquareButton(
        image = painter,
        tint = Color.White,
        contentDescription = poet.name,
        backgroundColor = Color.Transparent,
        size = 89,
        onClick = onClick,
        onLongClick = onLongClick
    )
}