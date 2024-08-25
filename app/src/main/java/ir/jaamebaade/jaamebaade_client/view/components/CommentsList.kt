package ir.jaamebaade.jaamebaade_client.view.components

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

@Composable
fun CommentsList(viewModel: FavoritesViewModel, navController: NavController) {
    val comments = viewModel.comments

    if (comments.isEmpty()) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .padding(10.dp)
                .fillMaxSize(),
        ) {
            Text(
                text = "یادداشتی پیدا نشد!",
                style = MaterialTheme.typography.headlineMedium
            )
        }
    } else {
        LazyColumn {
            items(comments) { commentInfo ->
                CommentListItem(
                    comment = commentInfo.comment,
                    path = commentInfo.path,
                    navController = navController,
                    onDeleteClicked = {
                        viewModel.deleteComment(commentInfo)
                    }
                )
            }
        }
    }
}
