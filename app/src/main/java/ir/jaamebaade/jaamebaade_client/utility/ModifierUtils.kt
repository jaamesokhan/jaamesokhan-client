package ir.jaamebaade.jaamebaade_client.utility

import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp

fun Modifier.bottomBorder(strokeWidth: Dp, color: Color) = composed(
    factory = {
        val density = LocalDensity.current
        val strokeWidthPx = density.run { strokeWidth.toPx() }

        Modifier.drawBehind {
            val width = size.width
            val height = size.height - strokeWidthPx / 2

            drawRoundRect(
                color = color,
                topLeft = Offset(x = 0f, y = height),
                size = androidx.compose.ui.geometry.Size(width, strokeWidthPx),
                cornerRadius = CornerRadius(x = strokeWidthPx / 2, y = strokeWidthPx / 2)
            )
        }
    }
)