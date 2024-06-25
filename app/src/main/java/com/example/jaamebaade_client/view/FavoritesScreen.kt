package com.example.jaamebaade_client.view

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.jaamebaade_client.view.components.BookmarkItem
import com.example.jaamebaade_client.viewmodel.FavoritesViewModel

@Composable
fun FavoritesScreen(
    modifier: Modifier,
    navController: NavController,
    viewModel: FavoritesViewModel = hiltViewModel()
) {
    val bookmarks = viewModel.bookmarks
    LazyColumn(modifier = modifier) {
        if (bookmarks.isEmpty()) {
            item {
                Text(text = "no bookmarks found")
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
