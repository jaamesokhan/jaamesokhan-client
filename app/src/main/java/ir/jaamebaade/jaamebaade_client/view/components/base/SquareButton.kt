package ir.jaamebaade.jaamebaade_client.view.components.base

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImagePainter

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SquareButton(
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
    image: AsyncImagePainter? = null,
    tint: Color,
    contentDescription: String,
    backgroundColor: Color,
    size: Int,
    onLongClick: (() -> Unit)? = null,
    onClick: () -> Unit,
) {
    assert(icon != null || image != null) { "Icon and image can't be null at the same time" }

    Column(
        modifier = modifier
            .clip(RoundedCornerShape(25.dp))
            .padding(8.dp)
            .combinedClickable(
                onClick = onClick,
                onLongClick = onLongClick
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(25.dp))
                .background(backgroundColor)
                .padding(4.dp)
                .size(size.dp)
        ) {
            icon?.let {
                Icon(
                    imageVector = it,
                    contentDescription = contentDescription,
                    modifier = Modifier
                        .fillMaxSize(),
                    tint = tint,
                )
            }
            image?.let {
                SquareImage(
                    image = it,
                    contentDescription = contentDescription,
                    size = size
                )
            }
        }
        Text(
            text = contentDescription,
            style = MaterialTheme.typography.titleMedium,
            textAlign = TextAlign.Center,
        )
    }
}
