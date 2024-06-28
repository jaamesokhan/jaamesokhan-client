package com.example.jaamebaade_client.view.components

import android.util.Log
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.example.jaamebaade_client.viewmodel.FavoritesViewModel

@Composable
fun HighlightList(viewModel: FavoritesViewModel, navController: NavController) {
    val highlights = viewModel.highlightedText
    LazyColumn {
        if (highlights.isEmpty()) {
            item {
                Text(text = "متنی را هایلایت نکرده‌اید!")
            }
        } else {
            items(highlights) { highlight ->
                HighlightItem(
                    highlightVersePoemPoet = highlight,
                    navController = navController
                )
            }
        }
    }
}