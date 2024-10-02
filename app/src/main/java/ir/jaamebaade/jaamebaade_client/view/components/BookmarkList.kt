package ir.jaamebaade.jaamebaade_client.view.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import ir.jaamebaade.jaamebaade_client.viewmodel.FavoritesViewModel

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun BookmarkList(viewModel: FavoritesViewModel, navController: NavController) {
    val bookmarks = viewModel.bookmarks
    if (bookmarks.isEmpty()) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .padding(10.dp)
                .fillMaxSize(),
        ) {
            Text(
                text = "شعری به علاقه‌مندی‌ها اضافه نکرده‌اید!",
                style = MaterialTheme.typography.headlineMedium
            )
        }
    } else {
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(bookmarks, key = { it.bookmark.id }) { bookmark ->
                BookmarkItem(
                    modifier = Modifier.animateItemPlacement(),
                    poem = bookmark.poem,
                    poet = bookmark.poet,
                    categories = bookmark.categories,
                    navController = navController,
                    onDeleteClick = {
                        viewModel.deleteBookmark(bookmark)
                    })
            }
        }
    }
}