package ir.jaamebaade.jaamebaade_client.view.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import ir.jaamebaade.jaamebaade_client.constants.AppRoutes
import ir.jaamebaade.jaamebaade_client.model.toPathHeaderText
import ir.jaamebaade.jaamebaade_client.viewmodel.FavoritesViewModel

@OptIn(ExperimentalFoundationApi::class)
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
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(items = comments, key = { it.comment.id }) { commentInfo ->
                CardItem(
                    modifier = Modifier.animateItemPlacement(),
                    headerText = commentInfo.path.toPathHeaderText(),
                    bodyText = AnnotatedString(commentInfo.comment.text),
                    icon = Icons.Outlined.Delete,
                    onClick = { navController.navigate("${AppRoutes.POEM}/${commentInfo.path.poet.id}/${commentInfo.path.poem.id}/-1") },
                    onIconClick = { viewModel.deleteComment(commentInfo) }
                )
            }
        }
    }
}
