package com.example.jaamebaade_client.view.components

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.example.jaamebaade_client.viewmodel.FavoritesViewModel

@Composable
fun BookmarkList(viewModel: FavoritesViewModel, navController: NavController) {
    val bookmarks = viewModel.bookmarks
    LazyColumn {
        if (bookmarks.isEmpty()) {
            item {
                Text(text = "شعری را لایک نکرده‌اید!")
            }
        } else {
            items(bookmarks) { bookmark ->
                BookmarkItem(
                    poem = bookmark.poem,
                    poet = bookmark.poet,
                    navController = navController
                )
            }
        }
    }
}