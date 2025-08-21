package ir.jaamebaade.jaamebaade_client.view.components.base

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import ir.jaamebaade.jaamebaade_client.view.components.ComposableSquareButtonStyle

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SquareButton(
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
    imageUrl: String? = null,
    tint: Color,
    contentDescription: String,
    text: String? = contentDescription,
    textStyle: TextStyle = MaterialTheme.typography.titleMedium,
    backgroundColor: Color,
    size: Int = 89,
    iconSize: Int = size,
    roundedCornerShapeSize: Int = 25,
    onLongClick: (() -> Unit)? = null,
    onClick: () -> Unit,
) {
    assert(icon != null || imageUrl != null) { "Icon and image can't be null at the same time" }
    assert(icon == null || imageUrl == null) { "Icon and image can't be set at the same time" }

    Column(
        modifier = modifier
            .clip(RoundedCornerShape(roundedCornerShapeSize.dp))
            .padding(horizontal = 12.dp)
            .combinedClickable(
                onClick = onClick,
                onLongClick = onLongClick
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(roundedCornerShapeSize.dp))
                .background(backgroundColor)
                .padding(4.dp)
                .size(size.dp)
        ) {
            icon?.let {
                Icon(
                    imageVector = it,
                    contentDescription = contentDescription,
                    modifier = Modifier
                        .size(iconSize.dp)
                        .align(Alignment.Center),
                    tint = tint,
                )
            }
            imageUrl?.let {
                SquareImage(
                    imageUrl = it,
                    contentDescription = contentDescription,
                    size = size
                )
            }
        }
        text?.let {
            Text(
                text = it,
                style = textStyle,
                modifier = Modifier.padding(top = 4.dp),
                textAlign = TextAlign.Center,
            )
        }
    }
}


@Composable
fun ComposableSquareButton(
    modifier: Modifier = Modifier,
    style: ComposableSquareButtonStyle = ComposableSquareButtonStyle.Filled,
    backgroundColor: Color = MaterialTheme.colorScheme.primary, // your green shade
    contentColor: Color = Color.White,
    borderColor: Color = MaterialTheme.colorScheme.primary,
    buttonWidth: Dp = Dp.Unspecified,
    buttonHeight: Dp = Dp.Unspecified,
    borderWidth: Dp = 1.dp,
    onClick: () -> Unit,
    content: @Composable RowScope.() -> Unit
) {
    val containerColor =
        if (style == ComposableSquareButtonStyle.Filled) backgroundColor else Color.Transparent

    val contentColorFinal =
        if (style == ComposableSquareButtonStyle.Filled) contentColor else backgroundColor

    Button(
        modifier = modifier
            .then(
                if (buttonWidth != Dp.Unspecified && buttonHeight != Dp.Unspecified) {
                    Modifier.size(buttonWidth, buttonHeight)
                } else Modifier
            )
            .border(
                width = borderWidth,
                color = if (style == ComposableSquareButtonStyle.Outlined) borderColor else Color.Transparent,
                shape = RoundedCornerShape(15.dp)
            ),
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = containerColor,
            contentColor = contentColorFinal
        ),
        shape = RoundedCornerShape(15.dp)
    ) {
        content()
    }
}