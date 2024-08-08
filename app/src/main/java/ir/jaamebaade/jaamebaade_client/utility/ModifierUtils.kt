package ir.jaamebaade.jaamebaade_client.utility

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.debugInspectorInfo

fun Modifier.shake(enabled: Boolean) = composed(

    factory = {
        val infiniteTransition = rememberInfiniteTransition(label = "")
        val scaleInfinite by infiniteTransition.animateFloat(
            initialValue = 1f,
            targetValue = .95f,
            animationSpec = infiniteRepeatable(
                animation = tween(1000, easing = LinearEasing),
                repeatMode = RepeatMode.Reverse
            ), label = ""
        )
        val rotation by infiniteTransition.animateFloat(
            initialValue = -8f,
            targetValue = 8f,
            animationSpec = infiniteRepeatable(
                animation = tween(100, easing = LinearEasing),
                repeatMode = RepeatMode.Reverse
            ), label = ""
        )

        this.graphicsLayer {
            scaleX = if (enabled) scaleInfinite else 1f
            scaleY = if (enabled) scaleInfinite else 1f
            rotationZ = if (enabled) rotation else 0f
        }
    },
    inspectorInfo = debugInspectorInfo {
        name = "shake"
        properties["enabled"] = enabled
    }
)
