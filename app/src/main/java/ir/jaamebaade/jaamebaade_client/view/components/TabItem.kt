package ir.jaamebaade.jaamebaade_client.view.components

import androidx.compose.runtime.Composable

data class TabItem(
    val text: String,
    val screen: @Composable ()->Unit
)
