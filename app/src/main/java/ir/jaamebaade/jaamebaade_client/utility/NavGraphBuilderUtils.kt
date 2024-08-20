package ir.jaamebaade.jaamebaade_client.utility

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable

fun NavGraphBuilder.animatedComposable(
    route: String,
    arguments: List<NamedNavArgument> = emptyList(),
    content: @Composable (AnimatedContentScope.(NavBackStackEntry) -> Unit)
) {
    composable(route = route,
        arguments = arguments,
        enterTransition = {
            fadeIn(
                animationSpec = tween(400)
            )
        }, exitTransition = {
            fadeOut(
                animationSpec = tween(400)
            )
        }) {
        content(it)
    }
}